package com.springboot.app.backend.turismo.model;


import java.util.List;

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
	@GeneratedValue
	private Integer idPreferencia;
	
	@Column(nullable = false)
	private long distanciaPreferida;
	
	@Column(nullable = false)
	private long tiempoDisponible;
	
	@OneToOne(mappedBy = "preferencia")
	private Usuario usuario;
	
	@ManyToMany
    @JoinTable(
      name = "Preferencia_TipoDeActividad", 
      joinColumns = @JoinColumn(name = "idPreferencia"), 
      inverseJoinColumns = @JoinColumn(name = "idTipoDeActividad"))
    private List<TipoDeActividad> tiposDeActividades;
}
