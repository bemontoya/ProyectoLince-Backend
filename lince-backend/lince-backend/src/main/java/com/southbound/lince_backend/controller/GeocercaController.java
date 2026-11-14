package com.southbound.lince_backend.controller;

import com.southbound.lince_backend.model.Geocerca;
import com.southbound.lince_backend.repository.GeocercaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geocercas")
@RequiredArgsConstructor
public class GeocercaController {

    private final GeocercaRepository geocercaRepository;

    @GetMapping
    public ResponseEntity<List<Geocerca>> listarTodas() {
        return ResponseEntity.ok(geocercaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Geocerca> crear(@RequestBody Geocerca geocerca) {
        if (geocerca.getRadioMetros() == null) {
            geocerca.setRadioMetros(150.0);
        }
        return ResponseEntity.ok(geocercaRepository.save(geocerca));
    }
}