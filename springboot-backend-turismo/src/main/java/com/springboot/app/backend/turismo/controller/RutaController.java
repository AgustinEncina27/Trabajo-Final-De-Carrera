package com.springboot.app.backend.turismo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.backend.turismo.dto.ComentarioRequest;
import com.springboot.app.backend.turismo.dto.RutaConTraducciones;
import com.springboot.app.backend.turismo.model.Coordenada;
import com.springboot.app.backend.turismo.model.Ruta;
import com.springboot.app.backend.turismo.service.IRutaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {
	
	private final IRutaService rutaService;
	
	@GetMapping
    public ResponseEntity<List<RutaConTraducciones>> obtenerTodas(@RequestParam String idioma) {
        return ResponseEntity.ok(rutaService.obtenerTodas(idioma));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutaConTraducciones> obtenerPorId(@PathVariable Integer id, @RequestParam String idioma) {
        return rutaService.obtenerPorId(id, idioma)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario")
    public ResponseEntity<?> obtenerRutasPorUsuario(
    		@RequestHeader("Authorization") String authorizationHeader,
    		@RequestParam String idioma) {
    	
    	if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token no válido"));
        }
    	
        List<RutaConTraducciones> rutas = rutaService.obtenerRutasPorUsuario(authorizationHeader, idioma);
        if (rutas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rutas);
    }

    
    @PostMapping
    public Ruta guardar(@RequestBody Ruta ruta) {
        return rutaService.guardar(ruta);
    }
    
    @PostMapping("/comentario")
    public ResponseEntity<?> agregarComentario(@RequestBody ComentarioRequest request) {
        rutaService.agregarComentarioYActualizarCalificacion(request);
        return ResponseEntity.ok("Comentario agregado y calificación actualizada");
    }

	@PostMapping("/generar")
	public ResponseEntity<?> generarRuta(
			@RequestHeader("Authorization") String authorizationHeader,
	        @RequestParam double latitud,
	        @RequestParam double longitud,
	        @RequestParam Long distanciaPreferida,
	        @RequestParam Integer costeMaximo,
	        @RequestParam Long tiempoDisponible,
	        @RequestParam String idioma) {

	    Coordenada ubicacionActual = new Coordenada(null, latitud, longitud);
	    
	    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token no válido"));
        }
	    
	    RutaConTraducciones ruta = rutaService.generarRutaParaUsuario(authorizationHeader, ubicacionActual,distanciaPreferida,costeMaximo,tiempoDisponible, idioma);
	    return ResponseEntity.ok(ruta);
	}
	
	@PutMapping("/{id}/estado")
	public ResponseEntity<?> actualizarEstadoRuta(
	        @PathVariable Integer id,
	        @RequestParam Integer idEstadoRuta) {

	    boolean actualizado = rutaService.actualizarEstado(id, idEstadoRuta);
	    
	    if (actualizado) {
	        return ResponseEntity.ok(Map.of("message", "Estado actualizado correctamente"));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Ruta o estado no encontrados"));
	    }
	}
	
	@PutMapping("/destino/{id}/llegar")
    public ResponseEntity<String> marcarDestinoComoLlegado(@PathVariable Integer id) {
        boolean actualizado = rutaService.marcarDestinoComoLlegado(id);
        if (actualizado) {
            return ResponseEntity.ok("Destino marcado como visitado.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!rutaService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        rutaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
