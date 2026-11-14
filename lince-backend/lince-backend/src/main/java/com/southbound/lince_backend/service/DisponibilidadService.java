package com.southbound.lince_backend.service;

import com.southbound.lince_backend.dto.DisponibilidadRequest;
import com.southbound.lince_backend.model.Disponibilidad;
import com.southbound.lince_backend.repository.DisponibilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private static final ZoneId ZONA_CHILE = ZoneId.of("America/Santiago");

    public Disponibilidad bloquearFecha(DisponibilidadRequest req, String email) {
        LocalDate hoy = LocalDate.now(ZONA_CHILE);
        long diasAnticipacion = ChronoUnit.DAYS.between(hoy, req.getFechaBloqueada());

        
        if (diasAnticipacion < 7) {
            throw new IllegalArgumentException(
                    "Solo puedes bloquear fechas con al menos 7 días de anticipación.");
        }

       
        if (req.getFechasConServiciosAsignados() != null
                && req.getFechasConServiciosAsignados().contains(req.getFechaBloqueada())) {
            throw new IllegalArgumentException(
                    "No puedes bloquear esta fecha porque ya tienes servicios asignados.");
        }

        if (disponibilidadRepository.existsByUsuarioEmailAndFechaBloqueada(email, req.getFechaBloqueada())) {
            throw new IllegalArgumentException("Esa fecha ya se encuentra bloqueada en tu calendario.");
        }

        Disponibilidad nueva = Disponibilidad.builder()
                .usuarioEmail(email)
                .fechaBloqueada(req.getFechaBloqueada())
                .franjaHoraria(req.getFranjaHoraria() != null ? req.getFranjaHoraria() : "DIA_COMPLETO")
                .motivo(req.getMotivo())
                .build();

        return disponibilidadRepository.save(nueva);
    }

    public List<Disponibilidad> listarPorUsuario(String email) {
        return disponibilidadRepository.findByUsuarioEmailOrderByFechaBloqueadaAsc(email);
    }

    public void eliminarBloqueo(Long id) {
        disponibilidadRepository.deleteById(id);
    }
}