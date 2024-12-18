package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.app.backend.turismo.model.Coordenada;

public interface CoordenadaRepository extends JpaRepository<Coordenada, Integer>  {

}
