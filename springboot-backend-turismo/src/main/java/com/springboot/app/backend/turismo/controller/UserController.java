package com.springboot.app.backend.turismo.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.model.Usuario;
import com.springboot.app.backend.turismo.service.UsuarioImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	
    private final UsuarioImpl usuarioService;
    
    @GetMapping("/obtenerTodos")
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> obtenerPorCorreo(@PathVariable String correo) {
        return usuarioService.obtenerPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{idUsuario}/preferencias")
    public ResponseEntity<Usuario> guardarPreferencias(@PathVariable Integer idUsuario, @RequestBody Preferencia preferencia) {
        Usuario usuarioActualizado = usuarioService.guardarPreferencias(idUsuario, preferencia);
        return ResponseEntity.ok(usuarioActualizado);
    }
        
    @PutMapping("/{idUsuario}/preferencias")
    public ResponseEntity<Preferencia> actualizarPreferencias(
            @PathVariable Integer idUsuario,
            @RequestBody Preferencia nuevaPreferencia) {

        Preferencia preferenciaActualizada = usuarioService.actualizarPreferencias(idUsuario, nuevaPreferencia);
        return ResponseEntity.ok(preferenciaActualizada);
    }
    
    // Endpoint para actualizar la distancia recorrida
    @PutMapping("/{id}/distancia")
    public ResponseEntity<String> actualizarDistancia(@PathVariable Integer id, @RequestBody Long distanciaRecorrida) {
        if (distanciaRecorrida == null || distanciaRecorrida < 0) {
            return ResponseEntity.badRequest().body("La distancia recorrida no puede ser nula o negativa.");
        }
        String resultado = usuarioService.actualizarDistancia(id, distanciaRecorrida);
        return ResponseEntity.ok(resultado);
    }

    // Endpoint para actualizar los puntos obtenidos
    @PutMapping("/{id}/puntos")
    public ResponseEntity<String> actualizarPuntos(@PathVariable Integer id, @RequestBody Integer puntosObtenidos) {
        if (puntosObtenidos == null || puntosObtenidos < 0) {
            return ResponseEntity.badRequest().body("Los puntos obtenidos no pueden ser nulos o negativos.");
        }
        String resultado = usuarioService.actualizarPuntos(id, puntosObtenidos);
        return ResponseEntity.ok(resultado);
    }
    
    @PostMapping
    public Usuario guardar(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!usuarioService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
