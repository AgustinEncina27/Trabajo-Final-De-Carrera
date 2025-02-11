package com.springboot.app.backend.turismo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.Preferencia;

@Repository
public interface PreferenciaRepository extends JpaRepository<Preferencia, Integer>  {
    Optional<Preferencia> findByUsuarioId(Integer usuarioId);
}
