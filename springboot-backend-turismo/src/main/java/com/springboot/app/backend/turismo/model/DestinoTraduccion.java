package com.springboot.app.backend.turismo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "destino_traducciones")
public class DestinoTraduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idDestino", nullable = false)
    private Destino destino;

    @Column(nullable = false)
    private String idioma;  // Ejemplo: "es", "en", "pt"

    @Column(nullable = false)
    private String nombreDestino;

    @Column(nullable = false, length = 1000)
    private String descripcion;

}