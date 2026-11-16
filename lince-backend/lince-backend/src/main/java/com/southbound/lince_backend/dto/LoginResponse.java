package com.southbound.lince_backend.dto;

public record LoginResponse(
        String token,
        String email,
        String nombre,
        String rol,
        Integer linceRecursoId,
        String oficinaCodigo) {
}