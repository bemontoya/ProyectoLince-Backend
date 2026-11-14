package com.southbound.lince_backend.repository;

import com.southbound.lince_backend.model.CheckInOut;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CheckInOutRepository extends JpaRepository<CheckInOut, Long> {
    Optional<CheckInOut> findByIdempotencyKey(String idempotencyKey);
    List<CheckInOut> findByServicioIdOrderByFechaHoraRegistroDesc(String servicioId);
    Optional<CheckInOut> findTopByServicioIdOrderByFechaHoraRegistroDesc(String servicioId);
}