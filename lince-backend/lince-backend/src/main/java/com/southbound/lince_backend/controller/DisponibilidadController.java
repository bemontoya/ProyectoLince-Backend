package com.southbound.lince_backend.controller;

import com.southbound.lince_backend.dto.DisponibilidadRequest;
import com.southbound.lince_backend.model.Disponibilidad;
import com.southbound.lince_backend.service.DisponibilidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/disponibilidad")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @GetMapping
    public ResponseEntity<List<Disponibilidad>> listar(Authentication auth) {
        return ResponseEntity.ok(disponibilidadService.listarPorUsuario(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<?> bloquear(@Valid @RequestBody DisponibilidadRequest req, Authentication auth) {
        try {
            Disponibilidad creada = disponibilidadService.bloquearFecha(req, auth.getName());
            return ResponseEntity.ok(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        disponibilidadService.eliminarBloqueo(id);
        return ResponseEntity.noContent().build();
    }
}