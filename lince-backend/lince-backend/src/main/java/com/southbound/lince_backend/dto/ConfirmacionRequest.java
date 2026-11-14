package com.southbound.lince_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmacionRequest {
    @NotBlank(message = "idempotencyKey es obligatorio")
    private String idempotencyKey;

    @NotBlank(message = "servicioId es obligatorio")
    private String servicioId;

    @NotBlank(message = "estado es obligatorio (CONFIRMADO o RECHAZADO)")
    private String estado;

    private String motivoRechazo;
}