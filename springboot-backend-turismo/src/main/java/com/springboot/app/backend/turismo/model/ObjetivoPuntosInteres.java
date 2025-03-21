package com.springboot.app.backend.turismo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "objetivo_puntos_interes")
public class ObjetivoPuntosInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "punto_de_interes_id", nullable = false)
    private PuntoDeInteres puntoDeInteres;

    @Column(nullable = false)
    private boolean visitado; // Indica si el usuario visitó el punto de interés

}
