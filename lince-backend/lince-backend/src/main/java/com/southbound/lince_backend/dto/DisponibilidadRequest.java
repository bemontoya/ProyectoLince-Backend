package com.southbound.lince_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DisponibilidadRequest {
    @NotNull(message = "fechaBloqueada es obligatoria (YYYY-MM-DD)")
    private LocalDate fechaBloqueada;

    private String franjaHoraria; // DIA_COMPLETO, MAÑANA, TARDE
    private String motivo;

    // Lista de fechas (YYYY-MM-DD) donde el usuario ya tiene servicios en Lince
    private java.util.List<LocalDate> fechasConServiciosAsignados;
}