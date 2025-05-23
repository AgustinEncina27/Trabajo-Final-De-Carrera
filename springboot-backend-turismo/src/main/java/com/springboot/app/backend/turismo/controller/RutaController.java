package com.springboot.app.backend.turismo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
import com.springboot.app.backend.turismo.dto.PuntoDeInteresDTO;
import com.springboot.app.backend.turismo.dto.RutaConTraducciones;
import com.springboot.app.backend.turismo.model.Coordenada;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.Ruta;
import com.springboot.app.backend.turismo.service.IPuntoDeInteresService;
import com.springboot.app.backend.turismo.service.IRutaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {
	
	private final IRutaService rutaService;
	private final IPuntoDeInteresService puntoDeInteresService;
	
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
	        @RequestParam Boolean sorpresa,
	        @RequestParam String idioma) {

	    Coordenada ubicacionActual = new Coordenada(null, latitud, longitud);
	    
	    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token no válido"));
        }
	    
	    RutaConTraducciones ruta = rutaService.generarRutaParaUsuario(authorizationHeader, ubicacionActual,distanciaPreferida,costeMaximo,tiempoDisponible,sorpresa, idioma);
	    return ResponseEntity.ok(ruta);
	}
	
	@PostMapping("/sugerencias")
	public ResponseEntity<List<PuntoDeInteresDTO>> sugerenciasParaRuta(
			@RequestHeader("Authorization") String authorizationHeader,
	        @RequestParam Integer idPuntoActual,
	        @RequestParam String idioma) {

	    
	    List<PuntoDeInteres> sugerencias = rutaService.obtenerSugerenciasDesdePunto(idPuntoActual, authorizationHeader);

	    List<PuntoDeInteresDTO> respuesta = sugerencias.stream()
	        .map(p -> puntoDeInteresService.mapToDTO(p, idioma))
	        .toList();

	    return ResponseEntity.ok(respuesta);
	}
	
	@PostMapping("/{idRuta}/puntos")
    public ResponseEntity<?> agregarPuntoDeInteresARuta(
            @PathVariable Integer idRuta,
            @RequestParam Integer idPunto,
            @RequestParam double latitud,
	        @RequestParam double longitud,
            @RequestParam String idioma) {
		
		Coordenada ubicacionActual = new Coordenada(null, latitud, longitud);

        Optional<RutaConTraducciones> resultado = rutaService.agregarPuntoDeInteresARuta(idRuta,ubicacionActual, idPunto, idioma);

        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ruta o punto no encontrado"));
        }
    }
	
	@PostMapping("/{idRuta}/puntos/agregar-despues")
	public ResponseEntity<?> insertarPuntoDeInteresDespuesDeOtro(
	        @PathVariable Integer idRuta,
	        @RequestParam Integer idPuntoNuevo,
	        @RequestParam Integer idPuntoReferencia,
	        @RequestParam double latitud,
	        @RequestParam double longitud,
	        @RequestParam String idioma) {
		
		Coordenada ubicacionActual = new Coordenada(null, latitud, longitud);

		Optional<RutaConTraducciones> resultado = rutaService.insertarPuntoDespuesDeOtro(idRuta, ubicacionActual,idPuntoNuevo, idPuntoReferencia, idioma);
	      
		if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ruta o punto no encontrado"));
        }		
		
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
	
	@DeleteMapping("/{idRuta}/puntos/{idPunto}")
	public ResponseEntity<?> eliminarPuntoDeInteresDeRuta(
	        @PathVariable Integer idRuta,
	        @PathVariable Integer idPunto,
	        @RequestParam double latitud,
	        @RequestParam double longitud,
	        @RequestParam String idioma) {
		
		Coordenada ubicacionActual = new Coordenada(null, latitud, longitud);

	    Optional<RutaConTraducciones> result = rutaService.eliminarPuntoDeInteresDeRuta(idRuta,ubicacionActual, idPunto, idioma);

	    if (result.isPresent()) {
	        return ResponseEntity.ok(result.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Ruta o punto no encontrado"));
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
