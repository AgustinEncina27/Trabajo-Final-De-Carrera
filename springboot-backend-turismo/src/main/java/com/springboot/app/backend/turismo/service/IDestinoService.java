package com.springboot.app.backend.turismo.service;

import java.util.List;

import com.springboot.app.backend.turismo.model.Destino;
import com.springboot.app.backend.turismo.model.DestinoTraduccion;
import com.springboot.app.backend.turismo.model.Preferencia;

public interface IDestinoService {
	public List<Destino> obtenerDestinosSegunPreferencias(Preferencia preferencia, Destino.ClimaIdeal climaActual);
	public DestinoTraduccion obtenerTraduccionDestino(Integer idDestino, String idioma);
}
