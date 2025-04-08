package com.springboot.app.backend.turismo.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
