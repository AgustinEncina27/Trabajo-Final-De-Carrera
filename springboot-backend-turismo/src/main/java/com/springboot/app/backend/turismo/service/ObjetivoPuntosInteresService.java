package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.auth.service.JwtService;
import com.springboot.app.backend.turismo.dto.ObjetivosConTraducciones;
import com.springboot.app.backend.turismo.model.ObjetivoPuntosInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;
import com.springboot.app.backend.turismo.model.Usuario;
import com.springboot.app.backend.turismo.repository.ObjetivoPuntosInteresRepository;
import com.springboot.app.backend.turismo.repository.PuntoDeInteresRepository;
import com.springboot.app.backend.turismo.repository.PuntoDeInteresTraduccionRepository;
import com.springboot.app.backend.turismo.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ObjetivoPuntosInteresService {

    private final ObjetivoPuntosInteresRepository repository;
    private final PuntoDeInteresRepository puntoDeInteresRepository;
    private final UserRepository usuarioRepository;
    private final JwtService jwtService;
	private final PuntoDeInteresTraduccionRepository puntoDeInteresTraduccionRepository;
    
    @Transactional(readOnly = true)
    public List<ObjetivoPuntosInteres> obtenerObjetivosPorUsuario(String authorizationHeader) {
    	
    	// Extraer token eliminando "Bearer "
	    String token = authorizationHeader.substring(7);

	    // Obtener ID del usuario desde el token
	    Integer idUsuario = jwtService.extractUserId(token);
    	
        return repository.findByUsuarioId(idUsuario);
    }
    
    @Transactional(readOnly = true)
	public ObjetivosConTraducciones obtenerObjetivosConTraducciones(
			String authorizationHeader, String idioma) {
    	// Extraer token eliminando "Bearer "
	    String token = authorizationHeader.substring(7);

	    // Obtener ID del usuario desde el token
	    Integer idUsuario = jwtService.extractUserId(token);

        List<ObjetivoPuntosInteres> objetivos = repository.findByUsuarioId(idUsuario);
        
        List<PuntoDeInteresTraduccion> puntosTraducidos = objetivos.stream()
                .map(ObjetivoPuntosInteres::getPuntoDeInteres)
                .map(p -> puntoDeInteresTraduccionRepository.findByPuntoDeInteresAndIdioma(p.getId(), idioma))
                .collect(Collectors.toList());

            return new ObjetivosConTraducciones(objetivos, puntosTraducidos);
	}
    
    @Transactional
    public void eliminarDesafios() {
        // Eliminar objetivos antiguos
        repository.eliminarObjetivosAntiguos();
    }

    @Transactional
    public void asignarObjetivosFijos(String authorizationHeader) {
    	
    	// Extraer token eliminando "Bearer "
	    String token = authorizationHeader.substring(7);

	    // Obtener ID del usuario desde el token
	    Integer idUsuario = jwtService.extractUserId(token);
    	
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener la lista de puntos de interés fijos (IDs fijos)
        List<Integer> idsPuntosFijos = List.of(1, 2, 3, 4, 5); // IDs fijos en la base de datos

        // Filtrar los puntos que aún no están asignados al usuario
        List<PuntoDeInteres> puntosNuevos = puntoDeInteresRepository.findAllById(idsPuntosFijos).stream()
                .filter(punto -> !repository.existsByUsuarioAndPuntoDeInteres(usuario, punto))
                .toList();

        // Crear y guardar los nuevos objetivos
        puntosNuevos.forEach(punto -> {
            ObjetivoPuntosInteres nuevoObjetivo = new ObjetivoPuntosInteres();
            nuevoObjetivo.setUsuario(usuario);
            nuevoObjetivo.setPuntoDeInteres(punto);
            nuevoObjetivo.setVisitado(false);

            repository.save(nuevoObjetivo);
        });
    }
    
    @Transactional
    public boolean marcarComoVisitado(String authorizationHeader, Integer puntoDeInteresId) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authorizationHeader.substring(7);
        Integer idUsuario = jwtService.extractUserId(token);

        if (idUsuario == null || puntoDeInteresId == null) {
            return false;
        }

        repository.marcarComoVisitado(idUsuario, puntoDeInteresId);
        return true;
    }
    
}
