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
@Table(name = "destinos")
public class Destino {
	  @Id
	  @GeneratedValue
	  private Integer idDestino;
	
	  @Column(nullable = false)
	  private String nombreDestino;
	
	  @Column(nullable = false)
	  private String descripcion;
	
	  @Column(nullable = false)
	  private String horario;
	
	  @Column(nullable = false)
	  private Integer coste;
	  
	  @Column(nullable = false)
	  private boolean accesibilidad;
	 
	  @Column(nullable = false)
	  private boolean techado;
	  
	  @OneToOne
	  @JoinColumn(name = "fkCoordenada") 
	  private Coordenada coordenada;
	  
	  @ManyToMany(mappedBy = "destinos")
	  private List<Ruta> rutas;
	  
	  @OneToMany(mappedBy = "destino", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<Comentario> comentarios;
	  
	  @ManyToMany
	    @JoinTable(
	      name = "destino_TipoDeActividad", 
	      joinColumns = @JoinColumn(name = "idDestino"), 
	      inverseJoinColumns = @JoinColumn(name = "idTipoDeActividad"))
	    private List<TipoDeActividad> tiposDeActividades;
}
