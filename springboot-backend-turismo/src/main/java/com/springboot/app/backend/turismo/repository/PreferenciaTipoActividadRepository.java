package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.app.backend.turismo.model.PreferenciaTipoDeActividad;

import java.util.List;

public interface PreferenciaTipoActividadRepository extends JpaRepository<PreferenciaTipoDeActividad, Integer> {
    List<PreferenciaTipoDeActividad> findByPreferenciaId(Integer idPreferencia);
}
