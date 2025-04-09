package com.springboot.app.backend.turismo.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.app.backend.turismo.model.PuntoDeInteres.ClimaIdeal;

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
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer idRuta;
	  
	  @Column(nullable = false)
	  private Double duracionEstimada;
	  
	  @Column(nullable = false)
	  private Double distanciaTotal;
	  
	  @Column
	  private Integer costeMaximo;
	  
	  @Column(nullable = false)
	  private LocalDate fechaCreacion;
	  
	  @Column
	  private ClimaIdeal clima;
	  
	  @Column
	  private int calificacion;
	  
	  @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<Comentario> comentarios;
	  
	  @JsonIgnore
	  @ManyToOne
	  @JoinColumn(name = "fkUsuario")
	  private Usuario usuario;
	
	  @ManyToOne
	  @JoinColumn(name = "fkEstado") 
	  private EstadoRuta estado;
	  
	  @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	  private List<RutaPuntoDeInteres> puntosDeInteres;
}
