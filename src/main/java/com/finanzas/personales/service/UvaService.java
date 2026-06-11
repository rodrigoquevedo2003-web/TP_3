package com.finanzas.personales.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class UvaService {

    @Value("${finanzas.uva.api-url:https://api.estadisticasbcra.com/uva}")
    private String apiUrl;

    @Value("${finanzas.uva.api-key:}")
    private String apiKey;

    private final RestTemplate rest = new RestTemplate();

    public BigDecimal obtenerValorActual() {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (apiKey != null && !apiKey.isBlank()) {
                headers.set("Authorization", "Bearer " + apiKey);
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = rest.exchange(apiUrl, HttpMethod.GET, entity, Object.class);
            Object resp = response.getBody();

            if (resp instanceof List) {
                List<?> lista = (List<?>) resp;
                if (lista.isEmpty()) throw new RuntimeException("Respuesta UVA vacía");
                Object first = lista.get(0);
                if (first instanceof Map) return extractValor((Map<?, ?>) first);
            } else if (resp instanceof Map) {
                return extractValor((Map<?, ?>) resp);
            }

            throw new RuntimeException("Formato de respuesta UVA no reconocido");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener valor UVA: " + e.getMessage(), e);
        }
    }

    private BigDecimal extractValor(Map<?, ?> map) {
        String[] keys = new String[]{"valor", "value", "indice", "indice_uva", "valor_uva", "valor_actual"};
        for (String k : keys) {
            if (map.containsKey(k) && map.get(k) != null) {
                Object v = map.get(k);
                return toBigDecimal(v);
            }
        }

        for (Map.Entry<?, ?> e : map.entrySet()) {
            if (e.getValue() instanceof Number || e.getValue() instanceof String) {
                try {
                    return toBigDecimal(e.getValue());
                } catch (Exception ignored) {
                }
            }
        }

        throw new RuntimeException("No se encontró valor numérico en respuesta UVA");
    }

    private BigDecimal toBigDecimal(Object v) {
        if (v instanceof Number) {
            return new BigDecimal(v.toString());
        }
        String s = v.toString().trim().replace(',', '.');
        return new BigDecimal(s);
    }
}



