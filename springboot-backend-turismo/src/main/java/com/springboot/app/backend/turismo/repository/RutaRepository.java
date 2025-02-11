package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.Ruta;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer>  {

}
