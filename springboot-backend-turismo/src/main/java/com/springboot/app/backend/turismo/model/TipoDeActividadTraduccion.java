package com.springboot.app.backend.turismo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_de_actividad_traducciones")
public class TipoDeActividadTraduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idTipoDeActividad", nullable = false)
    private TipoDeActividad tipoDeActividad;

    @Column(nullable = false)
    private String idioma;  // Ejemplo: "es", "en", "pt"

    @Column(nullable = false)
    private String nombreActividad;

    @Column(nullable = false, length = 1000)
    private String descripcion;
}