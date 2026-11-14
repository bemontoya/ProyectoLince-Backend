package com.southbound.lince_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String nombre;
    private String rol;
    private String linceStaffId;
}