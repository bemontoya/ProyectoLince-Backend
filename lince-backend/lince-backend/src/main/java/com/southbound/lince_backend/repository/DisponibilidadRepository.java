package com.southbound.lince_backend.repository;

import com.southbound.lince_backend.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    List<Disponibilidad> findByUsuarioEmailOrderByFechaBloqueadaAsc(String usuarioEmail);

    boolean existsByUsuarioEmailAndFechaBloqueada(String usuarioEmail, LocalDate fechaBloqueada);
}