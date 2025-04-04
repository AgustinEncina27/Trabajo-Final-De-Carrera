package com.springboot.app.backend.turismo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.app.backend.turismo.model.PuntoDeInteres.ClimaIdeal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ClimaService {
    
    private final WebClient webClient;

    public ClimaService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ClimaIdeal obtenerClima(double latitud, double longitud, long tiempoDisponible) {

        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("Las coordenadas están fuera del rango permitido. Latitud: " + latitud + ", Longitud: " + longitud);
        }

     // Construcción de la URL de la API
        String url = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", latitud)
                .queryParam("longitude", longitud)
                .queryParam("hourly", "precipitation")
                .queryParam("timezone", "America/Argentina/Buenos_Aires")
                .build()
                .toUriString();

        try {
            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("hourly")) {
                System.err.println("Error: Respuesta inválida de la API");
                return ClimaIdeal.SOLEADO;
            }

            Map<String, Object> hourly = (Map<String, Object>) response.get("hourly");
            List<String> times = (List<String>) hourly.get("time");
            List<Double> precipitation = (List<Double>) hourly.get("precipitation");

            if (times == null || precipitation == null) {
                System.err.println("Error: No se encontraron datos de precipitación.");
                return ClimaIdeal.SOLEADO;
            }

            // Obtener la fecha y hora actual
            LocalDateTime ahora = LocalDateTime.now();
            int horasARevisar = (int) (tiempoDisponible / 60);

            // Buscar el índice de la hora actual en el array de tiempos
            int indiceInicio = -1;
            for (int i = 0; i < times.size(); i++) {
                LocalDateTime forecastTime = LocalDateTime.parse(times.get(i), DateTimeFormatter.ISO_DATE_TIME);
                if (!forecastTime.isBefore(ahora)) { // Encuentra la primera hora que es igual o después de la actual
                    indiceInicio = i;
                    break;
                }
            }

            if (indiceInicio == -1) {
                System.err.println("No se encontraron datos de pronóstico a partir de la hora actual.");
                return ClimaIdeal.SOLEADO;
            }

            // Verificar la precipitación solo en las horas dentro del período disponible
            for (int i = indiceInicio; i < times.size() && i < (indiceInicio + horasARevisar); i++) {
                double lluvia = precipitation.get(i);
                if (lluvia > 0) {
                    return ClimaIdeal.LLUVIOSO; // Si en alguna hora llueve, devuelve LLUVIOSO
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el clima: " + e.getMessage());
        }


        return ClimaIdeal.SOLEADO;
    }
}

