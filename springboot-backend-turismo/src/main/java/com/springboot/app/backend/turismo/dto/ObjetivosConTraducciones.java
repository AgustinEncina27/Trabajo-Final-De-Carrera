package com.springboot.app.backend.turismo.dto;

import java.util.List;

import com.springboot.app.backend.turismo.model.ObjetivoPuntosInteres;
import com.springboot.app.backend.turismo.model.PuntoDeInteresTraduccion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ObjetivosConTraducciones {
    private List<ObjetivoPuntosInteres> objetivo;
    private List<PuntoDeInteresTraduccion> puntosDeInteresTraducidos;
}
