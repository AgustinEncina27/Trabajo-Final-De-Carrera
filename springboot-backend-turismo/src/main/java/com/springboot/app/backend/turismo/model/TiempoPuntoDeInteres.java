package com.springboot.app.backend.turismo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tiempos_punto_interes")
public class TiempoPuntoDeInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_origen", nullable = false)
    private PuntoDeInteres origen;

    @ManyToOne
    @JoinColumn(name = "id_destino", nullable = false)
    private PuntoDeInteres destino;

    @Column(nullable = false)
    private Double tiempo; // Tiempo en minutos

}
