package com.springboot.app.backend.turismo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.backend.turismo.dto.PuntoDeInteresDTO;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.service.IPuntoDeInteresService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/punto-de-interes")
@RequiredArgsConstructor
public class PuntoDeInteresControlelr {
    private final IPuntoDeInteresService puntoDeInteresService;

    @GetMapping
    public List<PuntoDeInteres> obtenerTodas() {
        return puntoDeInteresService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoDeInteres> obtenerPorId(@PathVariable Integer id) {
        return puntoDeInteresService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/filtrados")
    public ResponseEntity<List<PuntoDeInteresDTO>> obtenerFiltrados(
            @RequestBody List<Integer> excluir,
            @RequestParam String idioma) {

        List<PuntoDeInteresDTO> resultado = puntoDeInteresService.obtenerPuntosDeInteresFiltrados(excluir, idioma);
        return ResponseEntity.ok(resultado);
    }
    
    @PostMapping("/calcular-posicion-insercion")
    public ResponseEntity<Integer> calcularPosicionInsercion(
            @RequestParam Integer nuevoId,
            @RequestBody List<Integer> listaIds) {
        int posicion = puntoDeInteresService.calcularPosicionInsercion(nuevoId, listaIds);
        return ResponseEntity.ok(posicion);
    }

    @PostMapping
    public PuntoDeInteres guardar(@RequestBody PuntoDeInteres puntoDeInteres) {
        return puntoDeInteresService.guardar(puntoDeInteres);
    }
        
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!puntoDeInteresService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        puntoDeInteresService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
