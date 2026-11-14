package com.southbound.lince_backend.repository;

import com.southbound.lince_backend.model.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
    List<Incidencia> findByServicioId(String servicioId);
    List<Incidencia> findByUsuarioEmail(String usuarioEmail);
}