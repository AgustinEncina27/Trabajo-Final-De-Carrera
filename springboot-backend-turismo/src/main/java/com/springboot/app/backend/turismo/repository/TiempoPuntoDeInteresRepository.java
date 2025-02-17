package com.springboot.app.backend.turismo.repository;


import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.TiempoPuntoDeInteres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TiempoPuntoDeInteresRepository extends JpaRepository<TiempoPuntoDeInteres, Integer> {
    Optional<TiempoPuntoDeInteres> findByOrigenAndDestino(PuntoDeInteres origen, PuntoDeInteres destino);
}
