package com.southbound.lince_backend.repository;

import com.southbound.lince_backend.model.Geocerca;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GeocercaRepository extends JpaRepository<Geocerca, Long> {
    Optional<Geocerca> findByNombreLugarIgnoreCase(String nombreLugar);
}