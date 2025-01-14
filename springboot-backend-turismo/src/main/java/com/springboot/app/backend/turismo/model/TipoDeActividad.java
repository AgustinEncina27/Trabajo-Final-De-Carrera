package com.springboot.app.backend.turismo.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tiposDeActividades")
public class TipoDeActividad {
	
	  @Id
	  @GeneratedValue
	  private Integer idTipoDeActividad;
	
	  @Column(nullable = false)
	  private String nombreActividad;
	
	  @Column(nullable = false)
	  private String descripcion;
	  
	  @ManyToMany(mappedBy = "tiposDeActividades")
	  private List<Destino> destinos;
	  
	  @ManyToMany(mappedBy = "tiposDeActividades")
	  private List<Preferencia> preferencias;
}
