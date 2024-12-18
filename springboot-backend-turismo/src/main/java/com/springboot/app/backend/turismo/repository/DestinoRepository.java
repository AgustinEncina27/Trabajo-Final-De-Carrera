package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.app.backend.turismo.model.Destino;

public interface DestinoRepository extends JpaRepository<Destino, Integer>  {

}
