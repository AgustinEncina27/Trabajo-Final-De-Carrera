package com.springboot.app.backend.turismo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "Coordenadas")
public class Coordenada {
	  @Id
	  @GeneratedValue
	  private Integer idCoordenada;
	
	  @Column(nullable = false)
	  private long latitud;
	
	  @Column(nullable = false)
	  private long longitud;
	  
	  @OneToOne(mappedBy = "coordenada")
	  private Destino destino;
}
