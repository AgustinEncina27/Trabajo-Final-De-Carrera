package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.app.backend.turismo.model.Preferencia;


public interface PreferenciaRepository extends JpaRepository<Preferencia, Integer>  {

}
