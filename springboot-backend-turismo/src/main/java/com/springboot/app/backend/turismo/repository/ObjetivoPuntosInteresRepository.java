package com.springboot.app.backend.turismo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.model.ObjetivoPuntosInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.Usuario;

@Repository
public interface ObjetivoPuntosInteresRepository extends JpaRepository<ObjetivoPuntosInteres, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE ObjetivoPuntosInteres o SET o.visitado = true WHERE o.usuario.id = :usuarioId AND o.puntoDeInteres.id = :puntoDeInteresId")
    void marcarComoVisitado(Integer usuarioId, Integer puntoDeInteresId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM ObjetivoPuntosInteres o")
    void eliminarObjetivosAntiguos();
    
    List<ObjetivoPuntosInteres> findByUsuarioId(Integer usuarioId);
    
    boolean existsByUsuarioAndPuntoDeInteres(Usuario usuario, PuntoDeInteres puntoDeInteres);

}
