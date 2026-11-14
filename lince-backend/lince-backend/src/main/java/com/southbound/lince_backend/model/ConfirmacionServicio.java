package com.southbound.lince_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "confirmaciones_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmacionServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // clave única generada en Android para evitar duplicados al sincronizar con WorkManager
    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private String servicioId;

    @Column(nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private String estado; // CONFIRMADO o RECHAZADO

    private String motivoRechazo;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;
}
