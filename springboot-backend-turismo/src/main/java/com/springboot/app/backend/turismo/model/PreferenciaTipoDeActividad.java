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
@Table(name = "preferencia_tipo_actividad")
public class PreferenciaTipoDeActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "idPreferencia", nullable = false)
    private Preferencia preferencia;

    @ManyToOne
    @JoinColumn(name = "idTipoDeActividad", nullable = false)
    private TipoDeActividad tipoDeActividad;

    @Column(nullable = false)
    private Double peso; // Valor entre 0 y 1 que indica la preferencia del usuario
}
