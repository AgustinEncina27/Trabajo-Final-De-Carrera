package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;
import com.springboot.app.backend.turismo.repository.PuntoDeInteresRepository;
import com.springboot.app.backend.turismo.repository.PuntoDeInteresTraduccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuntoDeInteresImpl implements IPuntoDeInteresService {
	
	private final PuntoDeInteresRepository puntoDeInteresRepository;
	private final PuntoDeInteresTraduccionRepository puntoDeInteresTraduccionRepository;

	@Override
    @Transactional(readOnly = true)
	public List<PuntoDeInteres> obtenerPuntosDeInteresSegunClima( PuntoDeInteres.ClimaIdeal climaActual) {
		
	    return puntoDeInteresRepository.filtrarDestinos(
	        climaActual == PuntoDeInteres.ClimaIdeal.LLUVIOSO,  // Si llueve, buscar destinos techados
	        climaActual
	    );
	}
	
	@Override
    @Transactional(readOnly = true)
	public PuntoDeInteresTraduccion obtenerTraduccionDestino(Integer idDestino, String idioma) {
	    return puntoDeInteresTraduccionRepository.findByPuntoDeInteresAndIdioma(idDestino, idioma);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PuntoDeInteres> obtenerTodas() {
		return puntoDeInteresRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PuntoDeInteres> obtenerPorId(Integer id) {
		 return puntoDeInteresRepository.findById(id);
	}

	@Override
	@Transactional
	public PuntoDeInteres guardar(PuntoDeInteres puntoDeInteres) {
        return puntoDeInteresRepository.save(puntoDeInteres);
	}

	@Override
	@Transactional
	public void eliminar(Integer id) {
		puntoDeInteresRepository.deleteById(id);	
	}

}
