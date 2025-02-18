package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.model.Usuario;

public interface IUsuarioService {
	public List<Usuario> obtenerTodos();
	public Optional<Usuario> obtenerPorId(Integer id);
	public Optional<Usuario> obtenerPorCorreo(String correo);
	public Usuario guardar(Usuario usuario);
	public void eliminar(Integer id);
	public boolean cambiarContrasena(String nombreUsuario, String nuevaContrasena);
	public Usuario guardarPreferencias(Integer idUsuario, Preferencia preferencia);
	public Preferencia actualizarPreferencias(Integer idUsuario, Preferencia nuevaPreferencia);
	public boolean enviarCodigoRecuperacion(String correoUsuario);
}
