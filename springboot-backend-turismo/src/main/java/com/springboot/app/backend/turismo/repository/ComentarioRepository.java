package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer>  {

}
