package com.southbound.lince_backend.controller;

import com.southbound.lince_backend.dto.LoginRequest;
import com.southbound.lince_backend.dto.LoginResponse;
import com.southbound.lince_backend.model.Usuario;
import com.southbound.lince_backend.repository.UsuarioRepository;
import com.southbound.lince_backend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());

        return ResponseEntity.ok(LoginResponse.builder()
                .token(token)
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol().name())
                .linceStaffId(usuario.getLinceStaffId())
                .build());
    }
}