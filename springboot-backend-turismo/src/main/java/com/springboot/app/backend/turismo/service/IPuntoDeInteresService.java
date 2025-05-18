package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import com.springboot.app.backend.turismo.dto.PuntoDeInteresDTO;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;

public interface IPuntoDeInteresService {
	public List<PuntoDeInteres> obtenerPuntosDeInteresSegunClima( PuntoDeInteres.ClimaIdeal climaActual,Integer idUsuario);
	public PuntoDeInteresTraduccion obtenerTraduccionDestino(Integer idDestino, String idioma);
	public List<PuntoDeInteres> obtenerTodas();
	public Optional<PuntoDeInteres> obtenerPorId(Integer id);
	public PuntoDeInteres guardar(PuntoDeInteres puntoDeInteres);
	public void eliminar(Integer id);
    List<PuntoDeInteresDTO> obtenerPuntosDeInteresFiltrados(List<Integer> idsExcluir, String idioma);
    public int calcularPosicionInsercion(Integer nuevoId, List<Integer> listaIds);
    public PuntoDeInteresDTO mapToDTO(PuntoDeInteres pdi, String idioma);
}
