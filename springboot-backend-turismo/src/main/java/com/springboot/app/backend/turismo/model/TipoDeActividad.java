package com.springboot.app.backend.turismo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;
		  
	  @JsonIgnore
	  @OneToMany(mappedBy = "tipoDeActividad", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<PreferenciaTipoDeActividad> preferenciasActividades;
	  
	  @JsonIgnore
	  @OneToMany(mappedBy = "tipoDeActividad", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<TipoDeActividadTraduccion> traducciones;
	  
	  @JsonIgnore
	  @OneToMany(mappedBy = "tipoDeActividad", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<PuntoDeInteresTipoDeActividad> puntosDeInteres;
}
