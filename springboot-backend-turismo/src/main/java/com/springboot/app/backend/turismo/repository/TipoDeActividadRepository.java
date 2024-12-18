package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.app.backend.turismo.model.TipoDeActividad;

public interface TipoDeActividadRepository extends JpaRepository<TipoDeActividad, Integer> {

}
