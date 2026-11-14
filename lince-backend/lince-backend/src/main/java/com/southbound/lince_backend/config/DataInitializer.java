package com.southbound.lince_backend.config;

import com.southbound.lince_backend.model.Geocerca;
import com.southbound.lince_backend.model.Rol;
import com.southbound.lince_backend.model.Usuario;
import com.southbound.lince_backend.repository.GeocercaRepository;
import com.southbound.lince_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final GeocercaRepository geocercaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Precargar usuarios de prueba para los 3 roles
        if (!usuarioRepository.existsByEmail("driver@southbound.cl")) {
            usuarioRepository.save(Usuario.builder()
                    .email("driver@southbound.cl")
                    .password(passwordEncoder.encode("123456"))
                    .nombre("Driver Demo")
                    .rol(Rol.DRIVER)
                    .linceStaffId("TR-01")
                    .build());
        }
        if (!usuarioRepository.existsByEmail("guia@southbound.cl")) {
            usuarioRepository.save(Usuario.builder()
                    .email("guia@southbound.cl")
                    .password(passwordEncoder.encode("123456"))
                    .nombre("Guía Demo")
                    .rol(Rol.GUIA)
                    .linceStaffId("GU-01")
                    .build());
        }
        if (!usuarioRepository.existsByEmail("coord@southbound.cl")) {
            usuarioRepository.save(Usuario.builder()
                    .email("coord@southbound.cl")
                    .password(passwordEncoder.encode("123456"))
                    .nombre("Coordinador Central")
                    .rol(Rol.COORDINADOR)
                    .build());
        }

        // Precargar geocercas (radio inicial 150m)
        if (geocercaRepository.count() == 0) {
            geocercaRepository.save(Geocerca.builder()
                    .nombreLugar("Hotel Barrio Lastarria")
                    .latitud(-33.4372)
                    .longitud(-70.6401)
                    .radioMetros(150.0)
                    .build());
            geocercaRepository.save(Geocerca.builder()
                    .nombreLugar("Aeropuerto Arturo Merino Benítez (SCL)")
                    .latitud(-33.3930)
                    .longitud(-70.7858)
                    .radioMetros(150.0)
                    .build());
            geocercaRepository.save(Geocerca.builder()
                    .nombreLugar("Hotel Cumbres Puerto Varas")
                    .latitud(-41.3186)
                    .longitud(-72.9781)
                    .radioMetros(150.0)
                    .build());
            geocercaRepository.save(Geocerca.builder()
                    .nombreLugar("Hotel Cabo de Hornos Punta Arenas")
                    .latitud(-53.1627)
                    .longitud(-70.9078)
                    .radioMetros(150.0)
                    .build());
        }
    }
}