package com.springboot.app.backend.turismo.dto;

import java.util.List;

public record PuntoDeInteresDTO(
	    Integer id,
	    double latitud,
	    double longitud,
	    int duracionVisita,
	    boolean accesibilidad,
	    int popularidad,
	    String horario,
	    String climaIdeal,
	    Integer coste,
	    boolean techado,
	    String nombre,
	    String descripcion,
	    List<ActividadDTO> actividades
	) {}