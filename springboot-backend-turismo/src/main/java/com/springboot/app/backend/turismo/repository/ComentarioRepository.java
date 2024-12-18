package com.springboot.app.backend.turismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.app.backend.turismo.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer>  {

}
