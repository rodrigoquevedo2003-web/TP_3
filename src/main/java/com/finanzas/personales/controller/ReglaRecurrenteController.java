package com.finanzas.personales.controller;



import com.finanzas.personales.dto.request.ReglaRecurrenteRequestDTO;
import com.finanzas.personales.service.ReglaRecurrenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/reglas-recurrentes")
@RequiredArgsConstructor
public class ReglaRecurrenteController {

    private final ReglaRecurrenteService service;

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody @jakarta.validation.Valid ReglaRecurrenteRequestDTO dto,
            @RequestParam Long usuarioId) {
        try {
            return ResponseEntity.ok(service.crearRegla(dto, usuarioId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam Long usuarioLogueadoId) {
        try {
            return ResponseEntity.ok(service.obtenerReglasPorUsuario(usuarioId, usuarioLogueadoId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivar(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        try {
            service.desactivarRegla(id, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/test-scheduler")
    public ResponseEntity<String> forzarScheduler() {
        service.ejecutarReglasVencidas();
        return ResponseEntity.ok("Scheduler ejecutado manualmente con éxito. Revisá la consola de IntelliJ.");
    }
}