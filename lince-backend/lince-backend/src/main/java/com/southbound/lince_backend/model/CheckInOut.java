package com.southbound.lince_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkin_out")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private String servicioId;

    @Column(nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private String tipoEvento; // CHECK_IN o CHECK_OUT

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    private Double precisionGpsMetros;

    private Double distanciaGeocercaMetros;

    @Column(nullable = false)
    private Boolean validado; // true si cumplió radio <= 150m y >= 15 min antes

    private String observacionValidacion;

    @Column(nullable = false)
    private LocalDateTime fechaHoraRegistro;
}