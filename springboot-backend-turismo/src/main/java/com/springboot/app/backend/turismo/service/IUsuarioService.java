package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import com.springboot.app.backend.turismo.dto.UsuarioDTO;
import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.model.Usuario;

public interface IUsuarioService {
	public List<Usuario> obtenerTodos();
	public Optional<Usuario> obtenerPorId(Integer id);
	public Optional<UsuarioDTO> obtenerPorToken(String authorizationHeader);
	public Optional<Usuario> obtenerPorCorreo(String correo);
	public Usuario guardar(Usuario usuario);
	public void eliminar(String authorizationHeader);
	public boolean cambiarContrasena(String nombreUsuario, String nuevaContrasena);
	public Usuario guardarPreferencias(String authorizationHeader, Preferencia preferencia);
	public Preferencia actualizarPreferencias(String authorizationHeader, Preferencia nuevaPreferencia);
	public boolean enviarCodigoRecuperacion(String correoUsuario);
	public String actualizarDistancia(String authorizationHeader, Long distanciaRecorrida);
	public String actualizarPuntos(String authorizationHeader, Integer puntosObtenidos);
}
