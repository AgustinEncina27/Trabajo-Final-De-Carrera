package com.springboot.app.backend.turismo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.model.TipoDeActividadTraduccion;
import com.springboot.app.backend.turismo.repository.TipoDeActividadTraduccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoDeActividadServiceImpl implements ITipoDeActividadService{
	
	private final TipoDeActividadTraduccionRepository tipoDeActividadTraduccionRepository;
	
	@Override
    @Transactional(readOnly = true)
	public TipoDeActividadTraduccion obtenerTraduccionTipoDeActividad(Integer idTipoDeActividad, String idioma) {
	    return tipoDeActividadTraduccionRepository.findByTipoDeActividadIdAndIdioma(idTipoDeActividad, idioma)
	            .orElseThrow(() -> new RuntimeException("Traducción no encontrada para la actividad " + idTipoDeActividad + " en idioma " + idioma));
	}
}
