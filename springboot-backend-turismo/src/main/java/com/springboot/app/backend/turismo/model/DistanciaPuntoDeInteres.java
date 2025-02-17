package com.springboot.app.backend.turismo.model;

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
@Table(name = "distancia_puntos_de_interes")
public class DistanciaPuntoDeInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_punto_de_interes_origen", nullable = false)
    private PuntoDeInteres puntoDeInteresOrigen;

    @ManyToOne
    @JoinColumn(name = "id_punto_de_interes_destino", nullable = false)
    private PuntoDeInteres puntoDeInteresDestino;

    @Column(nullable = false)
    private Double distancia; // Distancia en kilómetros
}
