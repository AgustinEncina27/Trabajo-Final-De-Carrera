package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import com.springboot.app.backend.turismo.model.Preferencia;

public interface IPreferenciaService {
	public List<Preferencia> obtenerTodas();
	public Optional<Preferencia> obtenerPorId(Integer id);
	public Preferencia guardar(Preferencia preferencia);
	public void eliminar(Integer id);
	public Preferencia obtenerPreferenciasPorUsuario(Integer usuarioId);
}
