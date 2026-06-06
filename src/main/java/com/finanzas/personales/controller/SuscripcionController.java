package com.finanzas.personales.controller;

import com.finanzas.personales.dto.request.SuscripcionRequestDTO;
import com.finanzas.personales.dto.response.SuscripcionResponseDTO;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.SuscripcionService;
import com.finanzas.personales.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController {

    private final SuscripcionService suscripcionService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<SuscripcionResponseDTO> crear(
            @Valid @RequestBody SuscripcionRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(suscripcionService.crear(dto, usuario.getId()));
    }

    @GetMapping
    public ResponseEntity<List<SuscripcionResponseDTO>> listar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Boolean activa) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        if (activa != null) {
            return ResponseEntity.ok(suscripcionService.listarPorEstado(usuario.getId(), activa));
        }
        return ResponseEntity.ok(suscripcionService.listar(usuario.getId()));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<SuscripcionResponseDTO> toggleActiva(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        return ResponseEntity.ok(suscripcionService.toggleActiva(id, usuario.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuscripcionResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SuscripcionRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        return ResponseEntity.ok(suscripcionService.actualizar(id, dto, usuario.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        suscripcionService.eliminar(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-mensual")
    public ResponseEntity<BigDecimal> totalMensual(
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        return ResponseEntity.ok(suscripcionService.calcularTotalMensual(usuario.getId()));
    }
}
