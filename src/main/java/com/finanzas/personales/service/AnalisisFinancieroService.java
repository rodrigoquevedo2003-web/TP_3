package com.finanzas.personales.service;

import com.finanzas.personales.Exception.ReglaNegocioException;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.model.Presupuesto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalisisFinancieroService {

    private final CuentaService cuentaService;
    private final MovimientoService movimientoService;
    private final PresupuestoService presupuestoService;

    private final RestClient restClient = RestClient.create();

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}")
    private String baseUrl;

    public String analizarFinanzas(Long usuarioId) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ReglaNegocioException("El servicio de análisis no está configurado (falta GEMINI_API_KEY)");
        }

        String prompt = construirPrompt(usuarioId);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            GeminiResponse respuesta = restClient.post()
                    .uri(baseUrl + "/models/{model}:generateContent", model)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(GeminiResponse.class);

            return extraerTexto(respuesta);

        } catch (RestClientResponseException e) {
            log.error("[Gemini] Error HTTP {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ReglaNegocioException(
                    "Error al consultar Gemini (modelo '" + model + "'): " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("[Gemini] Error de conexión", e);
            throw new ReglaNegocioException(
                    "No se pudo conectar con Gemini: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        } catch (Exception e) {
            log.error("[Gemini] Error inesperado", e);
            throw new ReglaNegocioException(
                    "Error inesperado al generar el análisis: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private String construirPrompt(Long usuarioId) {
        List<Cuenta> cuentas = cuentaService.listarPorUsuario(usuarioId);
        List<Movimiento> movimientos = movimientoService.listarMovimientosPorUsuario(usuarioId);
        List<Presupuesto> presupuestos = presupuestoService.listar(usuarioId);

        StringBuilder sb = new StringBuilder();
        sb.append("Sos un asesor financiero personal. Analizá las finanzas de este usuario ");
        sb.append("y dale un resumen claro y breve (máximo 200 palabras) con su situación, ");
        sb.append("hábitos de gasto y 3 recomendaciones concretas. Respondé en español.\n\n");

        sb.append("CUENTAS:\n");
        for (Cuenta c : cuentas) {
            sb.append("- ").append(c.getNombre())
                    .append(" (").append(c.getTipoCuenta()).append("): $")
                    .append(c.getSaldo()).append("\n");
        }

        sb.append("\nPRESUPUESTOS:\n");
        if (presupuestos.isEmpty()) {
            sb.append("- (sin presupuestos definidos)\n");
        } else {
            for (Presupuesto p : presupuestos) {
                String cat = p.getCategoria() != null ? p.getCategoria().getNombre() : "sin categoría";
                sb.append("- ").append(cat)
                        .append(" (").append(p.getMes()).append("/").append(p.getAnio()).append("): consumido $")
                        .append(p.getMontoConsumido()).append(" de $").append(p.getMontoLimite()).append("\n");
            }
        }

        sb.append("\nÚLTIMOS MOVIMIENTOS:\n");
        movimientos.stream().limit(30).forEach(m -> {
            String cat = m.getCategoria() != null ? m.getCategoria().getNombre() : "sin categoría";
            sb.append("- ").append(m.getFecha())
                    .append(" ").append(m.getTipo())
                    .append(" $").append(m.getMonto())
                    .append(" [").append(cat).append("] ")
                    .append(m.getDescripcion() != null ? m.getDescripcion() : "")
                    .append("\n");
        });

        return sb.toString();
    }

    private String extraerTexto(GeminiResponse r) {
        if (r == null || r.candidates() == null || r.candidates().isEmpty()) {
            return "No se pudo generar el análisis.";
        }
        var content = r.candidates().get(0).content();
        if (content == null || content.parts() == null || content.parts().isEmpty()) {
            return "No se pudo generar el análisis.";
        }
        String texto = content.parts().get(0).text();
        return texto != null ? texto : "No se pudo generar el análisis.";
    }


    private record GeminiResponse(List<Candidate> candidates) {}
    private record Candidate(Content content) {}
    private record Content(List<Part> parts) {}
    private record Part(String text) {}
}