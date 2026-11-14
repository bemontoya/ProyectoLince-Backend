package com.southbound.lince_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckInOutRequest {
    @NotBlank(message = "idempotencyKey es obligatorio")
    private String idempotencyKey;

    @NotBlank(message = "servicioId es obligatorio")
    private String servicioId;

    @NotBlank(message = "tipoEvento es obligatorio (CHECK_IN o CHECK_OUT)")
    private String tipoEvento;

    @NotNull(message = "latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "longitud es obligatoria")
    private Double longitud;

    private Double precisionGpsMetros;

    // Nombre del lugar/hotel (pickup o dropoff) para buscar su geocerca
    private String nombreLugar;

    // Hora programada del pickup (formato HH:mm) 
    private String horaPickupProgramada;
}