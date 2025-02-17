package com.springboot.app.backend.turismo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.PuntoDeInteresTipoDeActividad;

@Repository
public interface PuntoDeInteresTipoDeActividadRepository extends JpaRepository<PuntoDeInteresTipoDeActividad, Integer>  {
	List<PuntoDeInteresTipoDeActividad> findByPuntoDeInteres_Id(Integer idPuntoDeInteres);
}
