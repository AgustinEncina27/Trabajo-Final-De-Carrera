package com.springboot.app.backend.turismo.repository;

import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PuntoDeInteresTraduccionRepository extends JpaRepository<PuntoDeInteresTraduccion, Integer> {
	@Query("SELECT t FROM PuntoDeInteresTraduccion t WHERE t.puntoDeInteres.id = :puntoDeInteresId AND t.idioma = :idioma")
	PuntoDeInteresTraduccion findByPuntoDeInteresAndIdioma(@Param("puntoDeInteresId") Integer puntoDeInteresId, @Param("idioma") String idioma);
}