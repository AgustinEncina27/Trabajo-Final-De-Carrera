package com.springboot.app.backend.turismo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.model.PreferenciaTipoDeActividad;
import com.springboot.app.backend.turismo.model.Usuario;
import com.springboot.app.backend.turismo.repository.PreferenciaRepository;
import com.springboot.app.backend.turismo.repository.PreferenciaTipoActividadRepository;
import com.springboot.app.backend.turismo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioImpl implements IUsuarioService {
	
	private final UserRepository usuarioRepository;
    private final PreferenciaRepository preferenciaRepository;
    private final PreferenciaTipoActividadRepository preferenciaTipoDeActividadRepository;
    private final PasswordEncoder passwordEncoder;
    private final CorreoService correoService;
    
    private final Map<String, String> codigosRecuperacion = new ConcurrentHashMap<>(); // Almacena temporalmente los códigos

    
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
    public boolean enviarCodigoRecuperacion(String correoUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoUsuario( correoUsuario);
        if (usuarioOpt.isPresent()) {
            String codigo = String.valueOf(new Random().nextInt(900000) + 100000); // Código de 6 dígitos
            codigosRecuperacion.put(correoUsuario, codigo);

            correoService.enviarCorreo(correoUsuario, "Código de recuperación", "Tu código de verificación es: " + codigo);
            return true;
        }
        return false;
    }
	
	// Verificar si el código es correcto
    public boolean verificarCodigo(String correoUsuario, String codigoVerificacion) {
        return codigosRecuperacion.containsKey(correoUsuario) && 
               codigosRecuperacion.get(correoUsuario).equals(codigoVerificacion);
    }
	
	@Override
	@Transactional
    public boolean cambiarContrasena(String email, String nuevaContrasena) {
		if (!codigosRecuperacion.containsKey(email)) {
            return false;
        }
		
		Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoUsuario(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setContrasena(passwordEncoder.encode(nuevaContrasena)); 
            usuarioRepository.save(usuario);
            codigosRecuperacion.remove(email);
            return true;
        }
        return false;
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
	    Usuario usuario = usuarioRepository.findById(idUsuario)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    // Desvincular temporalmente las actividades para que Hibernate no las guarde ahora
	    List<PreferenciaTipoDeActividad> actividades = preferencia.getPreferenciasActividades();
	    preferencia.setPreferenciasActividades(null);  // Evita que Hibernate intente guardarlas

	    // Guardar primero la preferencia sin actividades
	    Preferencia preferenciaGuardada = preferenciaRepository.save(preferencia);

	    // Ahora asignamos la preferencia a cada actividad y las volvemos a agregar
	    if (actividades != null) {
	        for (PreferenciaTipoDeActividad actividad : actividades) {
	            actividad.setPreferencia(preferenciaGuardada);
	        }
	        // Guardar todas las actividades con la referencia a la preferencia
	        preferenciaTipoDeActividadRepository.saveAll(actividades);

	        // Volver a asignar las actividades a la preferencia guardada
	        preferenciaGuardada.setPreferenciasActividades(actividades);
	    }

	    // Asignar la preferencia guardada al usuario y guardarlo
	    usuario.setPreferencia(preferenciaGuardada);
	    usuarioRepository.save(usuario);

	    return usuario;
	}
	
	@Override
	@Transactional
	public Preferencia actualizarPreferencias(Integer idUsuario, Preferencia nuevaPreferencia) {
	    Usuario usuario = usuarioRepository.findById(idUsuario)
	        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    Preferencia preferenciaExistente = usuario.getPreferencia();
	    if (preferenciaExistente == null) {
	        throw new RuntimeException("No se encontraron preferencias para el usuario");
	    }

	    // Actualizar los valores básicos
	    preferenciaExistente.setAccesibilidadRequerida(nuevaPreferencia.isAccesibilidadRequerida());
	    preferenciaExistente.setCosteMaximo(nuevaPreferencia.getCosteMaximo());
	    preferenciaExistente.setPopularidad(nuevaPreferencia.getPopularidad());

	    // 🔥 Solución para evitar que Hibernate intente eliminar las referencias previas
	    List<PreferenciaTipoDeActividad> actividadesActualizadas = new ArrayList<>();
	    for (PreferenciaTipoDeActividad preferenciaTipoDeActividad : nuevaPreferencia.getPreferenciasActividades()) {
	        if (preferenciaTipoDeActividad.getId() == null) {
	            preferenciaTipoDeActividad.setPreferencia(preferenciaExistente); // Asignar la preferencia si es una nueva actividad
	        } else {
	            // Mantener la referencia si ya existe en la base de datos
	            preferenciaTipoDeActividad = preferenciaTipoDeActividadRepository.findById(preferenciaTipoDeActividad.getId())
	                    .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
	        }
	        actividadesActualizadas.add(preferenciaTipoDeActividad);
	    }

	    // En lugar de sobrescribir, limpiamos y agregamos las nuevas actividades
	    preferenciaExistente.getPreferenciasActividades().clear();
	    preferenciaExistente.getPreferenciasActividades().addAll(actividadesActualizadas);

	    return preferenciaRepository.save(preferenciaExistente);
	}
	
	// Método para actualizar la distancia recorrida
	@Override
	@Transactional
    public String actualizarDistancia(Integer id, Long distanciaRecorrida) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setDistanciarecorrida((long) distanciaRecorrida); // Convertimos Integer a Long
            usuarioRepository.save(usuario);
            return "Distancia recorrida actualizada correctamente.";
        } else {
            return "Usuario no encontrado.";
        }
    }

    // Método para actualizar los puntos obtenidos
	@Override
	@Transactional
    public String actualizarPuntos(Integer id, Integer puntosObtenidos) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPuntosObtenidos(puntosObtenidos);
            usuarioRepository.save(usuario);
            return "Puntos obtenidos actualizados correctamente.";
        } else {
            return "Usuario no encontrado.";
        }
    }

}
