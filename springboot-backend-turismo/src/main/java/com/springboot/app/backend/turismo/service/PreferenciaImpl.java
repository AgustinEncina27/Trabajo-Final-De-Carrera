package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.model.Preferencia;
import com.springboot.app.backend.turismo.repository.PreferenciaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenciaImpl implements IPreferenciaService {
		
	private final PreferenciaRepository preferenciaRepository;
	
	
	@Override
    @Transactional(readOnly = true)
    public List<Preferencia> obtenerTodas() {
        return preferenciaRepository.findAll();
    }
	
	@Override
    @Transactional(readOnly = true)
    public Optional<Preferencia> obtenerPorId(Integer id) {
        return preferenciaRepository.findById(id);
    }
	
	@Override
    @Transactional(readOnly = true)
	public Preferencia obtenerPreferenciasPorUsuario(Integer usuarioId) {
	    return preferenciaRepository.findByUsuarioId(usuarioId)
	            .orElseThrow(() -> new RuntimeException("Preferencias no encontradas para el usuario " + usuarioId));
	}
	
	@Override
	@Transactional
    public Preferencia guardar(Preferencia preferencia) {
        return preferenciaRepository.save(preferencia);
    }
		
	@Override
	@Transactional
    public void eliminar(Integer id) {
        preferenciaRepository.deleteById(id);
    }
}
