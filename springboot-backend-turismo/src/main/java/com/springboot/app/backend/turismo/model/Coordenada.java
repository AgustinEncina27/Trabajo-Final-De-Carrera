package com.springboot.app.backend.turismo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
@Table(name = "coordenadas")
public class Coordenada {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer idCoordenada;
	
	  @Column(nullable = false)
	  private Double latitud;
	
	  @Column(nullable = false)
	  private Double longitud;
	  
	  @JsonIgnore
	  @OneToOne(mappedBy = "coordenada")
	  private Destino destino;
}
