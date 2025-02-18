package com.springboot.app.backend.turismo.dto;

public record VerificarCodigoRequest(
		String email,
		String codigoVerificacion
	) {

}
