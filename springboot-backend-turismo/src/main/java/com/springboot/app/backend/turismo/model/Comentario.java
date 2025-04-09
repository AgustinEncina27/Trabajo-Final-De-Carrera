package com.springboot.app.backend.turismo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "comentarios")
public class Comentario {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer idComentario;
	
	  @Column(nullable = false)
	  private String contenido;
	
	  @Column(nullable = false)
	  private Integer  calificacion;
	  
	  @Column(nullable = false)
	  private LocalDate  fechaComentario;
	  
	  @JsonIgnore
	  @ManyToOne
	  @JoinColumn(name = "fkRuta") 
	  private Ruta ruta;
}
