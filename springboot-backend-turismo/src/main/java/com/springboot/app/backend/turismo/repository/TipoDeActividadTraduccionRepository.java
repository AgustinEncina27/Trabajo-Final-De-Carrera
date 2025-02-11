package com.springboot.app.backend.turismo.repository;

import com.springboot.app.backend.turismo.model.TipoDeActividadTraduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoDeActividadTraduccionRepository extends JpaRepository<TipoDeActividadTraduccion, Integer> {
    Optional<TipoDeActividadTraduccion> findByTipoDeActividadIdAndIdioma(Integer idTipoDeActividad, String idioma);
}