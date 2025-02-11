package com.springboot.app.backend.turismo.repository;

import com.springboot.app.backend.turismo.model.DestinoTraduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DestinoTraduccionRepository extends JpaRepository<DestinoTraduccion, Integer> {
	DestinoTraduccion findByDestino_IdAndIdioma(Integer destinoId, String idioma);
}