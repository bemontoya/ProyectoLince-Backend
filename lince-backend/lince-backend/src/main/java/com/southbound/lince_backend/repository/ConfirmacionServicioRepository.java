package com.southbound.lince_backend.repository;

import com.southbound.lince_backend.model.ConfirmacionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConfirmacionServicioRepository extends JpaRepository<ConfirmacionServicio, Long> {
    Optional<ConfirmacionServicio> findByIdempotencyKey(String idempotencyKey);
    List<ConfirmacionServicio> findByUsuarioEmail(String usuarioEmail);
    Optional<ConfirmacionServicio> findTopByServicioIdAndUsuarioEmailOrderByFechaRegistroDesc(String servicioId, String usuarioEmail);
}