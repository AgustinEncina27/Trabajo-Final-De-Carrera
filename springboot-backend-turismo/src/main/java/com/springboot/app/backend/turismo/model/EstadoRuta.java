package com.springboot.app.backend.turismo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "EstadosRuta")
public class EstadoRuta {
	  @Id
	  @GeneratedValue
	  private Integer idEstadoRuta;
	
	  @Column(nullable = false)
	  private String descripcion;
	
	  @OneToMany(mappedBy = "estado", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<Ruta> rutas;
}