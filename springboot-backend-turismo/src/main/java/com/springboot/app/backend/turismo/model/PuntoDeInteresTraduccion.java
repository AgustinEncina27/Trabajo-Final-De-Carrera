package com.springboot.app.backend.turismo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "punto_de_interes_traducciones")
public class PuntoDeInteresTraduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_punto_de_interes", nullable = false)
    private PuntoDeInteres puntoDeInteres;

    @Column(nullable = false)
    private String idioma;  // Ejemplo: "es", "en", "pt"

    @Column(nullable = false)
    private String nombrePuntoDeInteres;

    @Column(nullable = false, length = 1000)
    private String descripcion;

}