package com.southbound.lince_backend.service;

import com.southbound.lince_backend.dto.CheckInOutRequest;
import com.southbound.lince_backend.dto.ConfirmacionRequest;
import com.southbound.lince_backend.model.*;
import com.southbound.lince_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperacionService {

    private final ConfirmacionServicioRepository confirmacionRepository;
    private final CheckInOutRepository checkInOutRepository;
    private final GeocercaRepository geocercaRepository;
    private final IncidenciaRepository incidenciaRepository;

    private static final ZoneId ZONA_CHILE = ZoneId.of("America/Santiago");

    // fórmula de Haversine: distancia en metros entre dos coordenadas GPS
    public double calcularDistanciaHaversineMetros(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000.0; // Radio de la Tierra en metros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public ConfirmacionServicio registrarConfirmacion(ConfirmacionRequest req, String email) {
        Optional<ConfirmacionServicio> existente = confirmacionRepository.findByIdempotencyKey(req.getIdempotencyKey());
        if (existente.isPresent()) {
            return existente.get();
        }

        ConfirmacionServicio nueva = ConfirmacionServicio.builder()
                .idempotencyKey(req.getIdempotencyKey())
                .servicioId(req.getServicioId())
                .usuarioEmail(email)
                .estado(req.getEstado().toUpperCase())
                .motivoRechazo(req.getMotivoRechazo())
                .fechaRegistro(LocalDateTime.now(ZONA_CHILE))
                .build();

        return confirmacionRepository.save(nueva);
    }

    public CheckInOut registrarCheckInOut(CheckInOutRequest req, String email) {
        Optional<CheckInOut> existente = checkInOutRepository.findByIdempotencyKey(req.getIdempotencyKey());
        if (existente.isPresent()) {
            return existente.get();
        }

        LocalDateTime ahora = LocalDateTime.now(ZONA_CHILE);
        Double distanciaMetros = null;
        double radioPermitido = 150.0;
        boolean dentroDeRadio = true;

        // Buscar geocerca por nombre del lugar si viene informado
        if (req.getNombreLugar() != null && !req.getNombreLugar().isBlank()) {
            Optional<Geocerca> geocercaOpt = geocercaRepository
                    .findByNombreLugarIgnoreCase(req.getNombreLugar().trim());
            if (geocercaOpt.isPresent()) {
                Geocerca g = geocercaOpt.get();
                radioPermitido = g.getRadioMetros();
                distanciaMetros = calcularDistanciaHaversineMetros(
                        req.getLatitud(), req.getLongitud(), g.getLatitud(), g.getLongitud());
                dentroDeRadio = distanciaMetros <= radioPermitido;
            }
        }

        // Validar precisión GPS (si es mayor a 50m se considera impreciso)
        boolean gpsPreciso = req.getPrecisionGpsMetros() == null || req.getPrecisionGpsMetros() <= 50.0;

        // Validar regla de los 15 minutos antes del pickup (solo aplica en CHECK_IN)
        boolean cumplePuntualidad = true;
        if ("CHECK_IN".equalsIgnoreCase(req.getTipoEvento()) && req.getHoraPickupProgramada() != null
                && !req.getHoraPickupProgramada().isBlank()) {
            try {
                LocalTime horaPickup = LocalTime.parse(req.getHoraPickupProgramada());
                LocalTime limiteAnticipacion = horaPickup.minusMinutes(15);
                if (ahora.toLocalTime().isAfter(limiteAnticipacion)) {
                    cumplePuntualidad = false;
                }
            } catch (Exception ignored) {
            }
        }

        boolean validado = true;
        String observacion = "Llegada verificada correctamente";

        if ("CHECK_IN".equalsIgnoreCase(req.getTipoEvento())) {
            if (!gpsPreciso) {
                validado = false;
                observacion = "Bloqueado: Precisión GPS insuficiente (" + req.getPrecisionGpsMetros() + "m)";
                registrarIncidenciaAutomatica(req, email, "GPS_IMPRECISO", observacion, distanciaMetros, ahora);
            } else if (!dentroDeRadio) {
                validado = false;
                observacion = String.format("Bloqueado: Fuera de geocerca (%.1f m > %.0f m)", distanciaMetros,
                        radioPermitido);
                registrarIncidenciaAutomatica(req, email, "FUERA_DE_RADIO", observacion, distanciaMetros, ahora);
            } else if (!cumplePuntualidad) {
                validado = false;
                long minutosDiff = ChronoUnit.MINUTES.between(ahora.toLocalTime(),
                        LocalTime.parse(req.getHoraPickupProgramada()));
                observacion = "Bloqueado: No cumple anticipación mínima de 15 min (faltan " + minutosDiff + " min)";
                registrarIncidenciaAutomatica(req, email, "ATRASO_15_MIN", observacion, distanciaMetros, ahora);
            }
        } else {
            observacion = "Check-out registrado en destino";
        }

        CheckInOut registro = CheckInOut.builder()
                .idempotencyKey(req.getIdempotencyKey())
                .servicioId(req.getServicioId())
                .usuarioEmail(email)
                .tipoEvento(req.getTipoEvento().toUpperCase())
                .latitud(req.getLatitud())
                .longitud(req.getLongitud())
                .precisionGpsMetros(req.getPrecisionGpsMetros())
                .distanciaGeocercaMetros(distanciaMetros)
                .validado(validado)
                .observacionValidacion(observacion)
                .fechaHoraRegistro(ahora)
                .build();

        return checkInOutRepository.save(registro);
    }

    private void registrarIncidenciaAutomatica(CheckInOutRequest req, String email, String tipo, String desc,
            Double dist, LocalDateTime fecha) {
        incidenciaRepository.save(Incidencia.builder()
                .servicioId(req.getServicioId())
                .usuarioEmail(email)
                .tipoIncidencia(tipo)
                .descripcion(desc)
                .latitud(req.getLatitud())
                .longitud(req.getLongitud())
                .distanciaMetros(dist)
                .fechaReporte(fecha)
                .build());
    }

    public List<CheckInOut> historialServicio(String servicioId) {
        return checkInOutRepository.findByServicioIdOrderByFechaHoraRegistroDesc(servicioId);
    }

    public Optional<CheckInOut> ultimaPosicionServicio(String servicioId) {
        return checkInOutRepository.findTopByServicioIdOrderByFechaHoraRegistroDesc(servicioId);
    }
}