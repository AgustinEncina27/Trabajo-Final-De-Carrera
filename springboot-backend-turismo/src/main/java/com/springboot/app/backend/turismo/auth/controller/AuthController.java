package com.springboot.app.backend.turismo.auth.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.app.backend.turismo.auth.service.AuthService;
import com.springboot.app.backend.turismo.dto.RecuperarContrasenaRequest;
import com.springboot.app.backend.turismo.dto.VerificarCodigoRequest;
import com.springboot.app.backend.turismo.service.UsuarioImpl;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final UsuarioImpl usuarioService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse response = service.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        final TokenResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
    ) {
        return service.refreshToken(authentication);
    }
    
    
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
    
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestBody VerificarCodigoRequest request) {
        boolean valido = usuarioService.verificarCodigo(request.email(), request.codigoVerificacion());
        if (valido) {
            return ResponseEntity.ok("Código válido, puede cambiar la contraseña");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código incorrecto o expirado");
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

}
