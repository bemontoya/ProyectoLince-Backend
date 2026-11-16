package com.southbound.lince_backend.controller;

import com.southbound.lince_backend.dto.LoginRequest;
import com.southbound.lince_backend.model.Usuario;
import com.southbound.lince_backend.repository.UsuarioRepository;
import com.southbound.lince_backend.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.southbound.lince_backend.dto.LoginResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login JWT y consulta de perfil por rol")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email().trim().toLowerCase())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.password(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }

        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponse(
                token,
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getRol().name(),
                usuario.getLinceRecursoId(),
                usuario.getOficinaCodigo()));
    }
}