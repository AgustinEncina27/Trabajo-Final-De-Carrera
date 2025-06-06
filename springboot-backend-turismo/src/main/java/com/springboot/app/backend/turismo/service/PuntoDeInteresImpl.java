package com.springboot.app.backend.turismo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.backend.turismo.dto.ActividadDTO;
import com.springboot.app.backend.turismo.dto.PuntoDeInteresDTO;
import com.springboot.app.backend.turismo.model.DistanciaPuntoDeInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;
import com.springboot.app.backend.turismo.repository.DistanciaPuntoDeInteresRepository;
import com.springboot.app.backend.turismo.repository.PuntoDeInteresRepository;
import com.springboot.app.backend.turismo.repository.PuntoDeInteresTraduccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuntoDeInteresImpl implements IPuntoDeInteresService {
	
	private final PuntoDeInteresRepository puntoDeInteresRepository;
	private final PuntoDeInteresTraduccionRepository puntoDeInteresTraduccionRepository;
    private final DistanciaPuntoDeInteresRepository distanciaRepository;


	@Override
    @Transactional(readOnly = true)
	public List<PuntoDeInteres> obtenerPuntosDeInteresSegunClima( PuntoDeInteres.ClimaIdeal climaActual,Integer idUsuario) {
		
	    return puntoDeInteresRepository.filtrarDestinos(
	        climaActual == PuntoDeInteres.ClimaIdeal.LLUVIOSO,  // Si llueve, buscar destinos techados
	        climaActual,
	        idUsuario
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
	@Transactional(readOnly = true)
	public List<PuntoDeInteresDTO> obtenerPuntosDeInteresFiltrados(List<Integer> idsExcluir, String idioma) {
	    List<PuntoDeInteres> todos = puntoDeInteresRepository.findAll();

	    return todos.stream()
	        .filter(p -> !idsExcluir.contains(p.getId()))
	        .map(p -> mapToDTO(p, idioma))
	        .toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int calcularPosicionInsercion(Integer nuevoId, List<Integer> listaIds) {
	    PuntoDeInteres nuevo = puntoDeInteresRepository.findById(nuevoId)
	        .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado"));

	    List<PuntoDeInteres> lista = puntoDeInteresRepository.findAllById(listaIds);

	    double menorCosto = Double.MAX_VALUE;
	    int mejorPosicion = 0;

	    for (int i = 0; i <= lista.size(); i++) {
	        double costo = 0;

	        if (i == 0 && !lista.isEmpty()) {
	            // Inserción al inicio
	            costo = obtenerDistancia(nuevo, lista.get(0));
	        } else if (i == lista.size() && !lista.isEmpty()) {
	            // Inserción al final
	            costo = obtenerDistancia(lista.get(lista.size() - 1), nuevo);
	        } else if (i > 0 && i < lista.size()) {
	            PuntoDeInteres anterior = lista.get(i - 1);
	            PuntoDeInteres siguiente = lista.get(i);

	            double distanciaOriginal = obtenerDistancia(anterior, siguiente);
	            double nuevaDistancia = obtenerDistancia(anterior, nuevo) + obtenerDistancia(nuevo, siguiente);
	            costo = nuevaDistancia - distanciaOriginal; // diferencia real
	        }

	        if (costo < menorCosto) {
	            menorCosto = costo;
	            mejorPosicion = i;
	        }
	    }

	    return mejorPosicion;
	}

	
	@Override
	@Transactional(readOnly = true)
	public PuntoDeInteresDTO mapToDTO(PuntoDeInteres pdi, String idioma) {
	    PuntoDeInteresTraduccion traduccion = pdi.getTraducciones().stream()
	        .filter(t -> t.getIdioma().equalsIgnoreCase(idioma))
	        .findFirst()
	        .orElse(null);

	    return new PuntoDeInteresDTO(
	        pdi.getId(),
	        pdi.getCoordenada().getLatitud(),
	        pdi.getCoordenada().getLongitud(),
	        pdi.getDuracionVisita(),
	        pdi.isAccesibilidad(),
	        pdi.getPopularidad(),
	        pdi.getHorario(),
	        pdi.getClimaIdeal().name(),
	        pdi.getCoste(),
	        pdi.isTechado(),
	        traduccion != null ? traduccion.getNombrePuntoDeInteres() : "",
	        traduccion != null ? traduccion.getDescripcion() : "",
	        pdi.getActividades().stream()
	            .map(a -> new ActividadDTO(
	                a.getTipoDeActividad().getId(),
	                a.getValor()))
	            .toList()
	    );
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
	

	private double obtenerDistancia(PuntoDeInteres a, PuntoDeInteres b) {
	    PuntoDeInteres origen = a.getId() < b.getId() ? a : b;
	    PuntoDeInteres destino = a.getId() < b.getId() ? b : a;

	    return distanciaRepository.findByPuntoDeInteresOrigenAndPuntoDeInteresDestino(origen, destino)
	            .map(DistanciaPuntoDeInteres::getDistancia)
	            .orElse(Double.MAX_VALUE); // Penalización si no existe
	}
	


}
