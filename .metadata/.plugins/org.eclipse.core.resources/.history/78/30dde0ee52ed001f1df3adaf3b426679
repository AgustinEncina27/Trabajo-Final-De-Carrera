package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.dto.UserResponse;
import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.model.Usuario;
import com.springboot.app.backend.turismo.repository.PreferenciaRepository;
import com.springboot.app.backend.turismo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioImpl implements IUsuarioService {
	
	private final UserRepository usuarioRepository;
    private final PreferenciaRepository preferenciaRepository;

	
	@Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
	
	@Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }
	
	@Override
    @Transactional
    public List<UserResponse> cambioContrasena() {
        return usuarioRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getNombreUsuario(), user.getCorreoUsuario()))
                .toList();
    }
	
	@Override
	@Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreoUsuario(correo);
    }
	
	@Override
	@Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
	@Override
	@Transactional
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
	
	@Override
	@Transactional
	public Usuario guardarPreferencias(Integer idUsuario, Preferencia preferencia) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            preferencia.setUsuario(usuario); // Asigna el usuario a la preferencia
            usuario.setPreferencia(preferencia); // Asigna la preferencia al usuario
            
            preferenciaRepository.save(preferencia); // Guarda la preferencia
            return usuarioRepository.save(usuario); // Guarda el usuario con la nueva preferencia
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
	}
}
