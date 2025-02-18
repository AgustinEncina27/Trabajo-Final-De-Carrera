package com.springboot.app.backend.turismo.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.backend.turismo.auth.controller.AuthRequest;
import com.springboot.app.backend.turismo.dto.RecuperarContrasenaRequest;
import com.springboot.app.backend.turismo.dto.VerificarCodigoRequest;
import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.model.Usuario;
import com.springboot.app.backend.turismo.service.UsuarioImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	
    private final UsuarioImpl usuarioService;

    // Endpoint POST para cambiar la contraseña
    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(@RequestBody AuthRequest request) {
        boolean actualizado = usuarioService.cambiarContrasena(request.email(), request.password());
        if (actualizado) {
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
    
    @PostMapping("/solicitar-recuperacion")
    public ResponseEntity<String> solicitarRecuperacion(@RequestBody RecuperarContrasenaRequest request) {
        boolean enviado = usuarioService.enviarCodigoRecuperacion(request.email());
        if (enviado) {
            return ResponseEntity.ok("Código de verificación enviado al correo");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Correo incorrecto");
        }
    }
    
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestBody VerificarCodigoRequest request) {
        boolean valido = usuarioService.verificarCodigo(request.email(), request.codigoVerificacion());
        if (valido) {
            return ResponseEntity.ok("Código válido, puede cambiar la contraseña");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código incorrecto o expirado");
        }
    }


    
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
