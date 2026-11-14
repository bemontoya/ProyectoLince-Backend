package com.southbound.lince_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "disponibilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private LocalDate fechaBloqueada;

    private String franjaHoraria; // DIA_COMPLETO, MAÑANA, TARDE

    private String motivo;
}
