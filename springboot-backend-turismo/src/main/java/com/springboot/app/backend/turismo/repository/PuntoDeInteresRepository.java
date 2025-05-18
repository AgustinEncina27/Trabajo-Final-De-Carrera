package com.springboot.app.backend.turismo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.PuntoDeInteres;

@Repository
public interface PuntoDeInteresRepository extends JpaRepository<PuntoDeInteres, Integer>  {

	@Query("""
		    SELECT d FROM PuntoDeInteres d
		    WHERE (:techado IS NULL OR d.techado = :techado)
		    AND (:climaActual IS NULL OR d.climaIdeal = :climaActual OR d.climaIdeal = 'CUALQUIERA')
		    AND d.id NOT IN (
		        SELECT rpi.puntoDeInteres.id FROM RutaPuntoDeInteres rpi
		        WHERE rpi.visitado = true
		        AND rpi.ruta.usuario.id = :idUsuario
		    )
		    ORDER BY d.id
		""")
		List<PuntoDeInteres> filtrarDestinos(
		    @Param("techado") Boolean techado,
		    @Param("climaActual") PuntoDeInteres.ClimaIdeal climaActual,
		    @Param("idUsuario") Integer idUsuario
		);
	
}
