package com.springboot.app.backend.turismo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.Destino;
import com.springboot.app.backend.turismo.model.TipoDeActividad;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Integer>  {
	@Query("SELECT d FROM Destino d " +
		       "JOIN d.tiposDeActividades t " +
		       "WHERE t IN :tipos " +
		       "AND d.coste <= :costeMaximo " +
		       "AND (:accesibilidad IS NULL OR :accesibilidad = false OR d.accesibilidad = :accesibilidad) " +
		       "AND (:techado IS NULL OR d.techado = :techado) " +
		       "AND (:climaActual IS NULL OR d.climaIdeal = :climaActual OR d.climaIdeal = 'CUALQUIERA') " +
		       "ORDER BY d.popularidad DESC")
		List<Destino> filtrarDestinos(
		        @Param("tipos") List<TipoDeActividad> tipos,
		        @Param("costeMaximo") Integer costeMaximo,
		        @Param("accesibilidad") Boolean accesibilidad,
		        @Param("techado") Boolean techado,
		        @Param("climaActual") Destino.ClimaIdeal climaActual
		);

}
