package com.springboot.app.backend.turismo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.service.IPreferenciaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/preferencias")
@RequiredArgsConstructor
public class PreferenciaController {
	
    private final IPreferenciaService preferenciaService;

    @GetMapping
    public List<Preferencia> obtenerTodas() {
        return preferenciaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Preferencia> obtenerPorId(@PathVariable Integer id) {
        return preferenciaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Preferencia guardar(@RequestBody Preferencia preferencia) {
        return preferenciaService.guardar(preferencia);
    }
        
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!preferenciaService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        preferenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
