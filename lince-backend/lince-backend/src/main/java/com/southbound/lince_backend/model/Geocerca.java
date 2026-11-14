package com.southbound.lince_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "geocercas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Geocerca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombreLugar; // debe coincidir o contener el nombre del pickup/hotel en Lince

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Builder.Default
    @Column(nullable = false)
    private Double radioMetros = 150.0;
}