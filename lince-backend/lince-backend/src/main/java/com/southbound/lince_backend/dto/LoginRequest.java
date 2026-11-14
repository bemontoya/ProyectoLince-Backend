package com.southbound.lince_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}