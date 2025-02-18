package com.springboot.app.backend.turismo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.app.backend.turismo.model.Usuario;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByCorreoUsuario(String email);

  Optional<Usuario> findByNombreUsuarioAndCorreoUsuario(String nombreUsuario, String correoUsuario);
}
