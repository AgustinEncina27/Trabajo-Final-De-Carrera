package com.springboot.app.backend.turismo.dto;

import java.time.LocalDate;
import com.springboot.app.backend.turismo.model.Preferencia;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {

	private Integer id;
    private String nombreUsuario;
    private String correoUsuario;
    private String celular;
    private LocalDate fechaDeCreacion;
    private Long distanciarecorrida;
    private Integer puntosObtenidos;
    private Preferencia preferencia;

    public UsuarioDTO() {
		// TODO Auto-generated constructor stub
	}
    
}
