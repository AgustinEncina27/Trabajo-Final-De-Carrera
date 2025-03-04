package com.springboot.app.backend.turismo.dto;

import java.util.List;

import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;
import com.springboot.app.backend.turismo.model.Ruta;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RutaConTraducciones {
    private Ruta ruta;
    private List<PuntoDeInteresTraduccion> puntosDeInteresTraducidos;
}