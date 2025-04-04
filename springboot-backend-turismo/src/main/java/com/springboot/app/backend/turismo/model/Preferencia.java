package com.springboot.app.backend.turismo.model;


import java.util.List;

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
@Table(name = "preferencias")
public class Preferencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private boolean accesibilidadRequerida;
		
	@Column
	private Integer popularidad;
	
	@JsonIgnore
	@OneToOne(mappedBy = "preferencia")
	private Usuario usuario;
	
	// Relación con la nueva tabla intermedia
    @OneToMany(mappedBy = "preferencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferenciaTipoDeActividad> preferenciasActividades;
}
