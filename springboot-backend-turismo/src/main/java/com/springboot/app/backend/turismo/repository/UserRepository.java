package com.springboot.app.backend.turismo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.app.backend.turismo.model.Usuario;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByCorreoUsuario(String email);

}
