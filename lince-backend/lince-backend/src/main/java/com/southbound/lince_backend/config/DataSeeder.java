package com.southbound.lince_backend.config;

import com.southbound.lince_backend.model.Rol;
import com.southbound.lince_backend.model.Usuario;
import com.southbound.lince_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            // Driver vinculado a Catalina Ibáñez Campos 
            usuarioRepository.save(Usuario.builder()
                    .email("driver01@demo.lince")
                    .password(passwordEncoder.encode("Lince2026!"))
                    .nombre("Catalina Ibáñez Campos")
                    .rol(Rol.DRIVER)
                    .linceRecursoId(1)
                    .oficinaCodigo("SCL")
                    .build());

            // Guía vinculado a Cristóbal Godoy Zúñiga 
            usuarioRepository.save(Usuario.builder()
                    .email("guia01@demo.lince")
                    .password(passwordEncoder.encode("Lince2026!"))
                    .nombre("Cristóbal Godoy Zúñiga")
                    .rol(Rol.GUIA)
                    .linceRecursoId(4)
                    .oficinaCodigo("SCL")
                    .build());

            // Coordinador operativo general
            usuarioRepository.save(Usuario.builder()
                    .email("coord01@demo.lince")
                    .password(passwordEncoder.encode("Lince2026!"))
                    .nombre("Coordinador Operaciones SCL")
                    .rol(Rol.COORDINADOR)
                    .linceRecursoId(null)
                    .oficinaCodigo("SCL")
                    .build());
        }
    }
}