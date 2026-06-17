package com.finanzas.personales.congif;

import com.finanzas.personales.service.ReglaRecurrenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile("!test") // No se ejecuta si el profile "test" está activo
public class RunOnStartup implements CommandLineRunner {

    private final ReglaRecurrenteService reglaService;

    @Override
    public void run(String... args) {
        log.info("=================================================");
        log.info("🤖 EJECUTANDO CONTROL DE REGLAS RECURRENTES AL INICIAR...");

        try {
            reglaService.ejecutarReglasVencidas();
            log.info("✅ Proceso de reglas finalizado.");
        } catch (Exception e) {
            log.error("❌ Error al ejecutar las reglas: {}", e.getMessage(), e);
        }

        log.info("=================================================");
    }
}