package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import com.springboot.app.backend.turismo.dto.ComentarioRequest;
import com.springboot.app.backend.turismo.dto.RutaConTraducciones;
import com.springboot.app.backend.turismo.model.Coordenada;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.Ruta;

public interface IRutaService {
	public RutaConTraducciones generarRutaParaUsuario(String authorizationHeader, Coordenada ubicacionActual,Long distanciaPreferida,Integer costeMaximo,Long tiempoDisponible,Boolean sorpresa, String idioma);
	public boolean marcarDestinoComoLlegado(Integer idRutaDestino);
	public Optional<Ruta> obtenerPorId(Integer id);
	public Ruta guardar(Ruta ruta);
	public void eliminar(Integer id);
	public List<RutaConTraducciones> obtenerTodas(String idioma);
	public Optional<RutaConTraducciones> obtenerPorId(Integer id, String idioma);
	public List<RutaConTraducciones> obtenerRutasPorUsuario(String authorizationHeader, String idioma);
	public boolean actualizarEstado(Integer id, Integer idEstadoRuta);
	public void agregarComentarioYActualizarCalificacion(ComentarioRequest request);
	public Ruta actualizarRuta(Integer idRuta, Ruta rutaNueva);
	public List<PuntoDeInteres> obtenerSugerenciasDesdePunto(Integer idPuntoActual, String authorizationHeader, Integer idRuta);
	public Optional<RutaConTraducciones> eliminarPuntoDeInteresDeRuta(Integer idRuta,Coordenada ubicacionActual,  Integer idPunto, String idioma);
	public Optional<RutaConTraducciones> agregarPuntoDeInteresARuta(Integer idRuta, Coordenada ubicacionActual, Integer idPunto, String idioma);
	public Optional<RutaConTraducciones> insertarPuntoDespuesDeOtro(Integer idRuta, Coordenada ubicacionActual, Integer idPuntoNuevo,
			Integer idPuntoReferencia, String idioma);
}

