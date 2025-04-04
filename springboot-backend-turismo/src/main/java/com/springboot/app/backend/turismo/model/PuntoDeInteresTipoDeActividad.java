package com.springboot.app.backend.turismo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "punto_de_interes_tipo_actividad")
public class PuntoDeInteresTipoDeActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_punto_de_interes", nullable = false)
    private PuntoDeInteres puntoDeInteres;

    @ManyToOne
    @JoinColumn(name = "idTipoDeActividad", nullable = false)
    private TipoDeActividad tipoDeActividad;

    @Column(nullable = false)
    private Double valor; // Peso o coste del atributo

}

