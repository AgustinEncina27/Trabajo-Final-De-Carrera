package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.TipoDeActividad;

@Repository
public interface TipoDeActividadRepository extends JpaRepository<TipoDeActividad, Integer> {

}
