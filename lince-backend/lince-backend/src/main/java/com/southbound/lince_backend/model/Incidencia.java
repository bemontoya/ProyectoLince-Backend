package com.southbound.lince_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String servicioId;

    @Column(nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private String tipoIncidencia; // FUERA_DE_RADIO, ATRASO_15_MIN, GPS_IMPRECISO, MANUAL

    @Column(nullable = false, length = 500)
    private String descripcion;

    private Double latitud;
    private Double longitud;
    private Double distanciaMetros;

    @Column(nullable = false)
    private LocalDateTime fechaReporte;
}