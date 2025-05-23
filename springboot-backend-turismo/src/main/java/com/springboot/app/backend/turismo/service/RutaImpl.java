package com.springboot.app.backend.turismo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.springboot.app.backend.turismo.auth.service.JwtService;
import com.springboot.app.backend.turismo.dto.ComentarioRequest;
import com.springboot.app.backend.turismo.dto.RutaConTraducciones;
import com.springboot.app.backend.turismo.model.ColoniaHormigas;
import com.springboot.app.backend.turismo.model.Comentario;
import com.springboot.app.backend.turismo.model.Coordenada;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;
import com.springboot.app.backend.turismo.model.DistanciaPuntoDeInteres;
import com.springboot.app.backend.turismo.model.TiempoPuntoDeInteres;
import com.springboot.app.backend.turismo.model.TipoDeActividad;
import com.springboot.app.backend.turismo.model.EstadoRuta;
import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.model.PreferenciaTipoDeActividad;
import com.springboot.app.backend.turismo.model.Ruta;
import com.springboot.app.backend.turismo.model.RutaPuntoDeInteres;
import com.springboot.app.backend.turismo.repository.PuntoDeInteresTraduccionRepository;
import com.springboot.app.backend.turismo.repository.ComentarioRepository;
import com.springboot.app.backend.turismo.repository.DistanciaPuntoDeInteresRepository;
import com.springboot.app.backend.turismo.repository.EstadoRutaRepository;
import com.springboot.app.backend.turismo.repository.RutaPuntoDeInteresRepository;
import com.springboot.app.backend.turismo.repository.RutaRepository;
import com.springboot.app.backend.turismo.repository.TiempoPuntoDeInteresRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RutaImpl implements IRutaService {
	
	private final IPreferenciaService preferenciaService;
	private final IPuntoDeInteresService puntoDeInteresService;
	private final RutaRepository rutaRepository;
	private final PuntoDeInteresTraduccionRepository puntoDeInteresTraduccionRepository;
	private final RutaPuntoDeInteresRepository rutaDestinoRepository;
	private final EstadoRutaRepository estadoRutaRepository;
	private final DistanciaPuntoDeInteresRepository distanciaRepository;
    private final TiempoPuntoDeInteresRepository tiempoRepository;
    private final ClimaService climaService;
    private final JwtService jwtService;
    private double distanciaMetros;
    private final ComentarioRepository comentarioRepository;
    private final TipoDeActividadServiceImpl TipoDeActividadServiceImpl;

	
    @Override
    @Transactional(readOnly = true)
    public List<RutaConTraducciones> obtenerTodas(String idioma) {
        return rutaRepository.findAll().stream()
                .map(ruta -> convertirARutaConTraducciones(ruta, idioma))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Ruta> obtenerPorId(Integer id) {
        return rutaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RutaConTraducciones> obtenerPorId(Integer id, String idioma) {
        return rutaRepository.findById(id)
                .map(ruta -> convertirARutaConTraducciones(ruta, idioma));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutaConTraducciones> obtenerRutasPorUsuario(String authorizationHeader, String idioma) {
    	
    	// Extraer token eliminando "Bearer "
        String token = authorizationHeader.substring(7);
		
		// Obtener ID del usuario desde el token
        Integer idUsuario = jwtService.extractUserId(token);
    	
    	return rutaRepository.findByUsuarioId(idUsuario).stream()
                .map(ruta -> convertirARutaConTraducciones(ruta, idioma))
                .collect(Collectors.toList());
    }
    

		
	@Override
	@Transactional
    public Ruta guardar(Ruta ruta) {
          return rutaRepository.save(ruta);
    }
	
	@Override
	@Transactional
	public Ruta actualizarRuta(Integer idRuta, Ruta rutaNueva) {
	    Ruta rutaExistente = rutaRepository.findById(idRuta)
	        .orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada"));

	    EstadoRuta estado = estadoRutaRepository.findById(rutaNueva.getEstado().getIdEstadoRuta())
	        .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado"));

	    // Actualizar campos simples
	    rutaExistente.setCosteMaximo(rutaNueva.getCosteMaximo());
	    rutaExistente.setDistanciaTotal(rutaNueva.getDistanciaTotal());
	    rutaExistente.setDuracionEstimada(rutaNueva.getDuracionEstimada());
	    rutaExistente.setFechaCreacion(rutaNueva.getFechaCreacion());
	    rutaExistente.setClima(rutaNueva.getClima());
	    rutaExistente.setCalificacion(rutaNueva.getCalificacion());
	    rutaExistente.setEstado(estado);

	    // Actualizar puntos de interés
	    rutaExistente.getPuntosDeInteres().clear();

	    for (RutaPuntoDeInteres rpi : rutaNueva.getPuntosDeInteres()) {
	        Integer idPoi = rpi.getPuntoDeInteres().getId();
	        PuntoDeInteres poi = puntoDeInteresService.obtenerPorId(idPoi)
	            .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con id " + idPoi));

	        rpi.setPuntoDeInteres(poi);
	        rpi.setRuta(rutaExistente);
	        rutaExistente.getPuntosDeInteres().add(rpi);
	    }

	    return rutaRepository.save(rutaExistente);
	}

	
	@Override
	@Transactional
    public void eliminar(Integer id) {
		rutaRepository.deleteById(id);
    }
	
	
	@Override
	@Transactional
	public RutaConTraducciones generarRutaParaUsuario(String authorizationHeader, Coordenada ubicacionActual, 
			Long distanciaPreferida,Integer costeMaximo,Long tiempoDisponible,Boolean sorpresa, String idioma) {
		
		// Extraer token eliminando "Bearer "
        String token = authorizationHeader.substring(7);
		
		// Obtener ID del usuario desde el token
        Integer idUsuario = jwtService.extractUserId(token);
		
	    Preferencia preferencia = preferenciaService.obtenerPreferenciasPorUsuario(idUsuario);
	    
	    if (sorpresa) {
	        Random random = new Random();

	        // Obtener todas las actividades posibles desde la base de datos
	        List<TipoDeActividad> todasLasActividades = TipoDeActividadServiceImpl.findAll();

	        // Limpiar las actividades actuales si hay
	        preferencia.getPreferenciasActividades().clear();
	        
	        List<PreferenciaTipoDeActividad> nuevasPreferencias = todasLasActividades.stream()
	                .map(tipo -> PreferenciaTipoDeActividad.builder()
	                        .preferencia(preferencia)
	                        .tipoDeActividad(tipo)
	                        .peso(Math.round(random.nextDouble() * 100.0) / 100.0)
	                        .build())
	                .collect(Collectors.toList());

	        preferencia.getPreferenciasActividades().addAll(nuevasPreferencias);
	        
	        tiempoDisponible = 60L + random.nextInt(301);
	        
	        costeMaximo=100000;
	        
	        distanciaPreferida=1000000L;
	        
	    }
	    	
	    Optional<EstadoRuta> estadoRuta = estadoRutaRepository.findById(3);
	    
	    PuntoDeInteres.ClimaIdeal climaActual = climaService.obtenerClima(ubicacionActual.getLatitud(), ubicacionActual.getLongitud(), tiempoDisponible);
	    
	    
	    List<PuntoDeInteres> destinos = puntoDeInteresService.obtenerPuntosDeInteresSegunClima(climaActual,idUsuario);

	    // Inicializar colonia de hormigas
	    ColoniaHormigas coloniaHormigas = new ColoniaHormigas(
	            destinos, preferencia, 100, 10,distanciaPreferida,costeMaximo,tiempoDisponible, distanciaRepository, tiempoRepository);

	    // Generar ruta optimizada considerando la ubicación actual
	    List<PuntoDeInteres> mejorRuta = coloniaHormigas.optimizarRuta(ubicacionActual);

	    // Crear la ruta en la BD
	    Ruta ruta = Ruta.builder()
	            .usuario(preferencia.getUsuario())
	            .distanciaTotal(calcularDistanciaTotal(mejorRuta,ubicacionActual))
	            .duracionEstimada(calcularDuracionTotal(mejorRuta))
	            .fechaCreacion(LocalDate.now())
	            .costeMaximo(costeMaximo)
	            .estado(estadoRuta.get())
	            .clima(climaActual)
	            .build();
	    	    
	 // Crear las relaciones de la ruta optimizada con los puntos de interés
	    List<RutaPuntoDeInteres> rutaPuntos = mejorRuta.stream()
	            .map(puntoDeInteres -> RutaPuntoDeInteres.builder()
	                    .ruta(ruta)
	                    .puntoDeInteres(puntoDeInteres)
	                    .visitado(false)
	                    .build())
	            .toList();
	    
	    ruta.setPuntosDeInteres(rutaPuntos);
	    
	    Ruta rutaGuardada=rutaRepository.save(ruta); 
    
	    // Obtener traducciones para la respuesta
	    List<PuntoDeInteresTraduccion> destinosTraducidos = mejorRuta.stream()
	            .map(destino -> puntoDeInteresTraduccionRepository.findByPuntoDeInteresAndIdioma(destino.getId(), idioma))
	            .toList();
	    
	    return new RutaConTraducciones(rutaGuardada, destinosTraducidos);
	}
	
    @Override
    @Transactional(readOnly = true)
    public List<PuntoDeInteres> obtenerSugerenciasDesdePunto(Integer idPuntoActual, String authorizationHeader) {
    	
    	// Extraer token eliminando "Bearer "
        String token = authorizationHeader.substring(7);
		
		// Obtener ID del usuario desde el token
        Integer idUsuario = jwtService.extractUserId(token);
    	
        // 1. Obtener preferencias del usuario
        Preferencia preferencia = preferenciaService.obtenerPreferenciasPorUsuario(idUsuario);
        
        PuntoDeInteres pDIActual= puntoDeInteresService.obtenerPorId(idPuntoActual)
        	    .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con ID: " + idPuntoActual));
        
        PuntoDeInteres.ClimaIdeal climaActual = climaService.obtenerClima(pDIActual.getCoordenada().getLatitud(), pDIActual.getCoordenada().getLongitud(), 300);

        // 2. Obtener todos los destinos excepto los ya visitados
        List<PuntoDeInteres> candidatos = puntoDeInteresService.obtenerPuntosDeInteresSegunClima(climaActual,idUsuario);

        // 3. Inicializar ColoniaHormigas con esos destinos
        ColoniaHormigas colonia = new ColoniaHormigas(candidatos, preferencia, 1, 1,
                0L, 0, 0L,
                distanciaRepository, tiempoRepository);

        return colonia.obtenerTopSugerenciasDesde(pDIActual, 3);
    }
	
	/**
     * Calcular la distancia total de la ruta optimizada
     */
    private double calcularDistanciaTotal(List<PuntoDeInteres> puntoDeInteres,Coordenada ubicacionActual) {
        double distanciaTotal = 0;
        
        distanciaTotal = calcularDistancia(ubicacionActual,puntoDeInteres.get(0).getCoordenada());
        this.distanciaMetros=distanciaTotal;
        for (int i = 0; i < puntoDeInteres.size() - 1; i++) {
        	Optional<DistanciaPuntoDeInteres> distanciaOpt;
        	if(puntoDeInteres.get(i).getId() < puntoDeInteres.get(i + 1).getId()) {
        		 distanciaOpt = distanciaRepository.findByPuntoDeInteresOrigenAndPuntoDeInteresDestino(puntoDeInteres.get(i), puntoDeInteres.get(i + 1));
        	}else {
        		 distanciaOpt = distanciaRepository.findByPuntoDeInteresOrigenAndPuntoDeInteresDestino(puntoDeInteres.get(i + 1), puntoDeInteres.get(i));
        	}	
            distanciaTotal += distanciaOpt.map(DistanciaPuntoDeInteres::getDistancia).orElse(0.0);
        }
        return distanciaTotal;
    }

    /**
     * Calcular la duración total de la ruta optimizada
     */
    private double calcularDuracionTotal(List<PuntoDeInteres> puntoDeInteres) {
        double duracionTotal = calcularTiempoEnMinutos() + puntoDeInteres.get(0).getDuracionVisita();
        for (int i = 0; i < puntoDeInteres.size() - 1; i++) {
        	Optional<TiempoPuntoDeInteres> tiempoOpt;
        	if(puntoDeInteres.get(i).getId() < puntoDeInteres.get(i + 1).getId()) {
        		tiempoOpt= tiempoRepository.findByOrigenAndDestino(puntoDeInteres.get(i), puntoDeInteres.get(i + 1));
        	}else {
        		tiempoOpt= tiempoRepository.findByOrigenAndDestino(puntoDeInteres.get(i+1), puntoDeInteres.get(i));
        	}	
            duracionTotal += tiempoOpt.map(TiempoPuntoDeInteres::getTiempo).orElse(0.0)+puntoDeInteres.get(i + 1).getDuracionVisita();

        }
        return duracionTotal;
    }

    @Override
    @Transactional
    public boolean marcarDestinoComoLlegado(Integer idRutaDestino) {
        Optional<RutaPuntoDeInteres> rutaDestinoOpt = rutaDestinoRepository.findById(idRutaDestino);

        if (rutaDestinoOpt.isPresent()) {
            RutaPuntoDeInteres rutaDestino = rutaDestinoOpt.get();
            rutaDestino.setVisitado(true);
            rutaDestinoRepository.save(rutaDestino);
            return true;
        }
        return false;
    }
    
	@Override
	@Transactional
	public boolean actualizarEstado(Integer idRuta, Integer idEstadoRuta) {
		  Optional<Ruta> optionalRuta = rutaRepository.findById(idRuta);
		    Optional<EstadoRuta> optionalEstado = estadoRutaRepository.findById(idEstadoRuta);

		    if (optionalRuta.isPresent() && optionalEstado.isPresent()) {
		        Ruta ruta = optionalRuta.get();
		        ruta.setEstado(optionalEstado.get());
		        rutaRepository.save(ruta);
		        return true;
		    }

		    return false;
	}
	
	@Override
	@Transactional
	public void agregarComentarioYActualizarCalificacion(ComentarioRequest request) {
        Ruta ruta = rutaRepository.findById(request.getRutaId())
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        Comentario comentario = Comentario.builder()
                .contenido(request.getContenido())
                .calificacion(request.getCalificacion())
                .fechaComentario(LocalDate.now())
                .ruta(ruta)
                .build();

        comentarioRepository.save(comentario);

        ruta.getComentarios().add(comentario);

        List<Comentario> comentarios = ruta.getComentarios();
        double promedio = comentarios.stream()
                .mapToInt(Comentario::getCalificacion)
                .average()
                .orElse(0.0);

        ruta.setCalificacion((int) Math.round(promedio));
        rutaRepository.save(ruta);
    }
    
    private RutaConTraducciones convertirARutaConTraducciones(Ruta ruta, String idioma) {
        List<PuntoDeInteresTraduccion> destinosTraducidos = ruta.getPuntosDeInteres().stream()
                .map(destino -> puntoDeInteresTraduccionRepository.findByPuntoDeInteresAndIdioma(destino.getPuntoDeInteres().getId(), idioma))
                .collect(Collectors.toList());

        return new RutaConTraducciones(ruta, destinosTraducidos);
    }

    private double calcularTiempoEnMinutos() {
        double velocidadMetrosPorMinuto = 83.33; // caminata aprox. 5km/h
        return this.distanciaMetros / velocidadMetrosPorMinuto;
    }
    
  //Calculo de distancias
    private double calcularDistancia(Coordenada origen, Coordenada destino) {
        final int RADIO_TIERRA_METROS = 6371000; // Radio de la Tierra en metros

        double lat1 = Math.toRadians(origen.getLatitud());
        double lon1 = Math.toRadians(origen.getLongitud());
        double lat2 = Math.toRadians(destino.getLatitud());
        double lon2 = Math.toRadians(destino.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_METROS * c; // Retorna la distancia en metros
    }

    @Override
    @Transactional
    public Optional<RutaConTraducciones> eliminarPuntoDeInteresDeRuta(Integer idRuta, Coordenada ubicacionActual, Integer idPunto, String idioma) {
        Optional<Ruta> rutaOpt = rutaRepository.findById(idRuta);
        if (rutaOpt.isEmpty()) return Optional.empty();

        Ruta ruta = rutaOpt.get();
        boolean eliminado = ruta.getPuntosDeInteres().removeIf(pdi -> pdi.getPuntoDeInteres().getId().equals(idPunto));
        if (!eliminado) return Optional.empty();

        // Obtener la lista actualizada de puntos de interés
        List<PuntoDeInteres> listaPDI = ruta.getPuntosDeInteres().stream()
            .map(RutaPuntoDeInteres::getPuntoDeInteres)
            .toList();

        // Recalcular distancia y duración solo si hay al menos un punto
        double nuevaDistancia = 0;
        double nuevaDuracion = 0;

        if (!listaPDI.isEmpty()) {
            nuevaDistancia = calcularDistanciaTotal(listaPDI, ubicacionActual);
            nuevaDuracion = calcularDuracionTotal(listaPDI);
        }

        ruta.setDistanciaTotal(nuevaDistancia);
        ruta.setDuracionEstimada(nuevaDuracion);

        rutaRepository.save(ruta);

        List<PuntoDeInteresTraduccion> traducciones = ruta.getPuntosDeInteres().stream()
            .map(p -> puntoDeInteresTraduccionRepository.findByPuntoDeInteresAndIdioma(p.getPuntoDeInteres().getId(), idioma))
            .toList();

        return Optional.of(new RutaConTraducciones(ruta, traducciones));
    }


    @Override
    @Transactional
    public Optional<RutaConTraducciones> agregarPuntoDeInteresARuta(Integer idRuta, Coordenada ubicacionActual, Integer idPunto, String idioma) {
        Optional<Ruta> rutaOpt = rutaRepository.findById(idRuta);
        Optional<PuntoDeInteres> puntoOpt = puntoDeInteresService.obtenerPorId(idPunto);

        if (rutaOpt.isEmpty() || puntoOpt.isEmpty()) return Optional.empty();

        Ruta ruta = rutaOpt.get();
        PuntoDeInteres nuevoPunto = puntoOpt.get();

        // Verificar si ya existe
        boolean yaExiste = ruta.getPuntosDeInteres().stream()
                .anyMatch(p -> p.getPuntoDeInteres().getId().equals(idPunto));
        if (yaExiste) return Optional.empty();

        // Obtener lista de IDs actuales
        List<Integer> idsActuales = ruta.getPuntosDeInteres().stream()
                .map(p -> p.getPuntoDeInteres().getId())
                .toList();

        // Calcular la posición óptima
        int posicion = puntoDeInteresService.calcularPosicionInsercion(idPunto, idsActuales);

        // Crear la relación
        RutaPuntoDeInteres nuevo = RutaPuntoDeInteres.builder()
                .ruta(ruta)
                .puntoDeInteres(nuevoPunto)
                .visitado(false)
                .build();

        // Insertar en la posición calculada
        ruta.getPuntosDeInteres().add(posicion, nuevo);

        // Recalcular distancia y duración
        List<PuntoDeInteres> listaPDI = ruta.getPuntosDeInteres().stream()
                .map(RutaPuntoDeInteres::getPuntoDeInteres)
                .toList();

        double nuevaDistancia = calcularDistanciaTotal(listaPDI, ubicacionActual);
        double nuevaDuracion = calcularDuracionTotal(listaPDI);

        ruta.setDistanciaTotal(nuevaDistancia);
        ruta.setDuracionEstimada(nuevaDuracion);

        rutaRepository.save(ruta);

        List<PuntoDeInteresTraduccion> traducciones = ruta.getPuntosDeInteres().stream()
                .map(p -> puntoDeInteresTraduccionRepository.findByPuntoDeInteresAndIdioma(p.getPuntoDeInteres().getId(), idioma))
                .toList();

        return Optional.of(new RutaConTraducciones(ruta, traducciones));
    }


    @Override
    @Transactional
    public Optional<RutaConTraducciones> insertarPuntoDespuesDeOtro(Integer idRuta, Coordenada ubicacionActual, Integer idPuntoNuevo, Integer idPuntoReferencia, String idioma) {
        Optional<Ruta> rutaOpt = rutaRepository.findById(idRuta);
        Optional<PuntoDeInteres> puntoNuevoOpt = puntoDeInteresService.obtenerPorId(idPuntoNuevo);

        if (rutaOpt.isEmpty() || puntoNuevoOpt.isEmpty()) return Optional.empty();

        Ruta ruta = rutaOpt.get();
        PuntoDeInteres nuevoPunto = puntoNuevoOpt.get();

        // Verificar si ya existe
        boolean yaExiste = ruta.getPuntosDeInteres().stream()
                .anyMatch(p -> p.getPuntoDeInteres().getId().equals(idPuntoNuevo));
        if (yaExiste) return Optional.empty();

        // Buscar la posición del punto de referencia
        List<RutaPuntoDeInteres> lista = ruta.getPuntosDeInteres();
        int indiceReferencia = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getPuntoDeInteres().getId().equals(idPuntoReferencia)) {
                indiceReferencia = i;
                break;
            }
        }

        if (indiceReferencia == -1) return Optional.empty();

        // Insertar después del punto de referencia
        int posicionInsercion = indiceReferencia + 1;

        RutaPuntoDeInteres nuevo = RutaPuntoDeInteres.builder()
                .ruta(ruta)
                .puntoDeInteres(nuevoPunto)
                .visitado(false)
                .build();

        lista.add(posicionInsercion, nuevo);

        // Recalcular distancia y duración
        List<PuntoDeInteres> listaPDI = ruta.getPuntosDeInteres().stream()
                .map(RutaPuntoDeInteres::getPuntoDeInteres)
                .toList();

        double nuevaDistancia = calcularDistanciaTotal(listaPDI, ubicacionActual);
        double nuevaDuracion = calcularDuracionTotal(listaPDI);

        ruta.setDistanciaTotal(nuevaDistancia);
        ruta.setDuracionEstimada(nuevaDuracion);

        rutaRepository.save(ruta);

        List<PuntoDeInteresTraduccion> traducciones = ruta.getPuntosDeInteres().stream()
                .map(p -> puntoDeInteresTraduccionRepository.findByPuntoDeInteresAndIdioma(p.getPuntoDeInteres().getId(), idioma))
                .toList();

        return Optional.of(new RutaConTraducciones(ruta, traducciones));
    }

    
    
}

