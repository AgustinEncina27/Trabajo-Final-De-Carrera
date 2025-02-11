package com.springboot.app.backend.turismo.service;

import com.springboot.app.backend.turismo.model.TipoDeActividadTraduccion;

public interface ITipoDeActividadService {
	public TipoDeActividadTraduccion obtenerTraduccionTipoDeActividad(Integer idTipoDeActividad, String idioma);
}
