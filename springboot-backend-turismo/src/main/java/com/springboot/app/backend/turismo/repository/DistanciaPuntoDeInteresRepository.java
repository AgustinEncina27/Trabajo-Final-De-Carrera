package com.springboot.app.backend.turismo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.DistanciaPuntoDeInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;

@Repository
public interface DistanciaPuntoDeInteresRepository extends JpaRepository<DistanciaPuntoDeInteres, Integer>  {
	Optional<DistanciaPuntoDeInteres> findByPuntoDeInteresOrigenAndPuntoDeInteresDestino(PuntoDeInteres origen, PuntoDeInteres destino);
}
