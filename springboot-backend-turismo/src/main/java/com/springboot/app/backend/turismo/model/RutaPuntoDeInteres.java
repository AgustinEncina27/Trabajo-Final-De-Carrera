package com.springboot.app.backend.turismo.model;

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
@Table(name = "ruta_punto_de_interes")
public class RutaPuntoDeInteres {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	 	
		@JsonIgnore
	    @ManyToOne
	    @JoinColumn(name = "idRuta", nullable = false)
	    private Ruta ruta;

	    @ManyToOne
	    @JoinColumn(name = "idPuntoDeInteres", nullable = false)
	    private PuntoDeInteres puntoDeInteres;
	    
	    @Column(name = "orden")
	    private Integer orden;

	    @Column(nullable = false)
	    private boolean visitado; // Indica si el usuario llegó a este destino
}
