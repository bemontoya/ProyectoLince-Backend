package com.southbound.lince_backend.controller;

import com.southbound.lince_backend.dto.CheckInOutRequest;
import com.southbound.lince_backend.dto.ConfirmacionRequest;
import com.southbound.lince_backend.model.CheckInOut;
import com.southbound.lince_backend.model.ConfirmacionServicio;
import com.southbound.lince_backend.model.Incidencia;
import com.southbound.lince_backend.repository.ConfirmacionServicioRepository;
import com.southbound.lince_backend.repository.IncidenciaRepository;
import com.southbound.lince_backend.service.OperacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operaciones")
@RequiredArgsConstructor
public class OperacionController {

    private final OperacionService operacionService;
    private final ConfirmacionServicioRepository confirmacionRepository;
    private final IncidenciaRepository incidenciaRepository;

    @PostMapping("/confirmaciones")
    public ResponseEntity<ConfirmacionServicio> confirmarServicio(@Valid @RequestBody ConfirmacionRequest req, Authentication auth) {
        return ResponseEntity.ok(operacionService.registrarConfirmacion(req, auth.getName()));
    }

    @GetMapping("/confirmaciones")
    public ResponseEntity<List<ConfirmacionServicio>> misConfirmaciones(Authentication auth) {
        return ResponseEntity.ok(confirmacionRepository.findByUsuarioEmail(auth.getName()));
    }

    @PostMapping("/checkin-out")
    public ResponseEntity<CheckInOut> checkInOut(@Valid @RequestBody CheckInOutRequest req, Authentication auth) {
        return ResponseEntity.ok(operacionService.registrarCheckInOut(req, auth.getName()));
    }

    @GetMapping("/checkin-out/{servicioId}")
    public ResponseEntity<List<CheckInOut>> historialCheckIn(@PathVariable String servicioId) {
        return ResponseEntity.ok(operacionService.historialServicio(servicioId));
    }

    @GetMapping("/checkin-out/{servicioId}/ultima-posicion")
    public ResponseEntity<?> ultimaPosicion(@PathVariable String servicioId) {
        return operacionService.ultimaPosicionServicio(servicioId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/incidencias")
    public ResponseEntity<List<Incidencia>> listarIncidencias() {
        return ResponseEntity.ok(incidenciaRepository.findAll());
    }
}