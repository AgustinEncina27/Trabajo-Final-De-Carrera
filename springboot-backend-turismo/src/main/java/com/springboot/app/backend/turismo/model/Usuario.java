package com.springboot.app.backend.turismo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.app.backend.turismo.auth.repository.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String nombreUsuario;

  @Column(nullable = false, unique = true)
  private String correoUsuario;

  @Column(nullable = false)
  private String contrasena;
  
  @Column(nullable = false)
  private String celular;
  
  @Column(nullable = false)
  private LocalDate fechaDeCreacion;
  
  @Column
  private Long distanciarecorrida;
  
  @Column
  private Integer puntosObtenidos;
  
  @OneToOne
  @JoinColumn(name = "fkPreferencia")  
  private Preferencia preferencia;
  
  @JsonIgnore
  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Ruta> rutas;
  
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;
  
  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ObjetivoPuntosInteres> objetivosPuntosInteres;
  
}
