package com.springboot.app.backend.turismo.service;

import java.util.List;

import com.springboot.app.backend.turismo.model.TipoDeActividad;
import com.springboot.app.backend.turismo.model.TipoDeActividadTraduccion;

public interface ITipoDeActividadService {
	public TipoDeActividadTraduccion obtenerTraduccionTipoDeActividad(Integer idTipoDeActividad, String idioma);
	public List<TipoDeActividad> findAll();
}
