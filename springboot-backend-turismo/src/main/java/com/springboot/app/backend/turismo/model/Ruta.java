package com.springboot.app.backend.turismo.model;

import java.time.LocalDate;
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
@Table(name = "rutas")
public class Ruta {
	  @Id
	  @GeneratedValue
	  private Integer idRuta;
	  
	  @Column(nullable = false)
	  private Integer duracionEstimada;
	  
	  @Column(nullable = false)
	  private Integer distanciaTotal;
	  
	  @Column(nullable = false)
	  private LocalDate fechaCreacion;
	  
	  @ManyToOne
	  @JoinColumn(name = "fkUsuario")
	  private Usuario usuario;
	
	  @ManyToOne
	  @JoinColumn(name = "fkEstado") 
	  private EstadoRuta estado;
	  
	  @ManyToMany
	    @JoinTable(
	      name = "ruta_destino", 
	      joinColumns = @JoinColumn(name = "idRuta"), 
	      inverseJoinColumns = @JoinColumn(name = "idDestino"))
	    private List<Destino> destinos;
}
