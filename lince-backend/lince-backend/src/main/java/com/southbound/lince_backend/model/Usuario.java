package com.southbound.lince_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // ID en API Lince (transportistaId para DRIVER, guiaId para GUIA, null para COORDINADOR)
    private Integer linceRecursoId;

    // Oficina asignada ( SCL, PUX, PUQ)
    private String oficinaCodigo;
}