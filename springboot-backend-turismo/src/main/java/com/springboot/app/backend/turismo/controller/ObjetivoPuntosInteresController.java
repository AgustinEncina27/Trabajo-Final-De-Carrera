package com.springboot.app.backend.turismo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.backend.turismo.dto.ObjetivosConTraducciones;
import com.springboot.app.backend.turismo.service.ObjetivoPuntosInteresService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/objetivos")
@RequiredArgsConstructor
public class ObjetivoPuntosInteresController {

    private final ObjetivoPuntosInteresService objetivoPuntosInteresService;
    
    @GetMapping("/usuario")
    public ResponseEntity<?> obtenerObjetivosPorUsuario(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String idioma) {
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token no válido"));
        }

        ObjetivosConTraducciones resultado = objetivoPuntosInteresService.obtenerObjetivosConTraducciones(authorizationHeader, idioma);
        return ResponseEntity.ok(resultado);
    }
    
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarObjetivosFijos(@RequestHeader("Authorization") String authorizationHeader) {
    	
    	if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token no válido"));
        }
    	
    	
        objetivoPuntosInteresService.asignarObjetivosFijos(authorizationHeader);
        return ResponseEntity.ok(Map.of("message", "Objetivos guardados con éxito"));
    }
    
    @PutMapping("/visitar/{puntoDeInteresId}")
    public ResponseEntity<?> marcarComoVisitado(
    		@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer puntoDeInteresId) {
    	
    	if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token no válido"));
        }
    	
    	boolean marcado = objetivoPuntosInteresService.marcarComoVisitado(authorizationHeader, puntoDeInteresId);

        if (!marcado) {
            return ResponseEntity.badRequest().build(); // 400 si falla algo
        }

        return ResponseEntity.ok().build(); // 200 OK si todo salió bien
    }
    
    @DeleteMapping("/eliminar-antiguos")
    public ResponseEntity<Void> eliminarDesafios() {
        objetivoPuntosInteresService.eliminarDesafios();
        return ResponseEntity.ok().build(); // Devuelve un 200 OK sin contenido
    }
}
