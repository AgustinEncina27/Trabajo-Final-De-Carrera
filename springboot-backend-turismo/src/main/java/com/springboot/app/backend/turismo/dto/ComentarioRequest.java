package com.springboot.app.backend.turismo.dto;

import lombok.Data;

@Data
public class ComentarioRequest {
	private Integer rutaId;
    private String contenido;
    private Integer calificacion;
}
