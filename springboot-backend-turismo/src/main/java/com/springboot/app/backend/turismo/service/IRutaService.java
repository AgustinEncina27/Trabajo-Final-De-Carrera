package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import com.springboot.app.backend.turismo.dto.RutaConTraducciones;
import com.springboot.app.backend.turismo.model.Coordenada;
import com.springboot.app.backend.turismo.model.Ruta;

public interface IRutaService {
	public RutaConTraducciones generarRutaParaUsuario(Integer usuarioId, Coordenada ubicacionActual,Long distanciaPreferida,Long tiempoDisponible, String idioma);
	public boolean marcarDestinoComoLlegado(Integer idRutaDestino);
	public Optional<Ruta> obtenerPorId(Integer id);
	public Ruta guardar(Ruta ruta);
	public void eliminar(Integer id);
	public List<RutaConTraducciones> obtenerTodas(String idioma);
	public Optional<RutaConTraducciones> obtenerPorId(Integer id, String idioma);
	public List<RutaConTraducciones> obtenerRutasPorUsuario(Integer usuarioId, String idioma);
}

