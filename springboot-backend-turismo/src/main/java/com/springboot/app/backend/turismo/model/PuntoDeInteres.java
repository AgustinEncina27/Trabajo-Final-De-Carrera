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
@Table(name = "punto_de_interes")
public class PuntoDeInteres {
	  
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;
		  
	  @OneToOne
	  @JoinColumn(name = "fkCoordenada") 
	  private Coordenada coordenada;
	  
	  @Column
	  private int duracionVisita;
	  
	  @Column
	  private boolean accesibilidad;
	  
	  @Column
	  private int popularidad;
	  
	  @Column
	  private String horario;
	  
	  @Column
	  @Enumerated(EnumType.STRING)  // Guarda el nombre del enum como texto
	  private ClimaIdeal climaIdeal;
	
	  @Column
	  private Integer coste;
	  
	  @Column
	  private boolean techado;
	  	  
	  @JsonIgnore
	  @OneToMany(mappedBy = "puntoDeInteres", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<RutaPuntoDeInteres> rutas;
	  
	  @JsonIgnore
	  @OneToMany(mappedBy = "puntoDeInteres", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<PuntoDeInteresTraduccion> traducciones;
	  
	  @OneToMany(mappedBy = "puntoDeInteres", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<PuntoDeInteresTipoDeActividad> actividades;
	  
	  public enum ClimaIdeal {
		    SOLEADO,
		    NUBLADO,
		    LLUVIOSO,
		    VENTOSO,
		    CUALQUIERA
		}
}
