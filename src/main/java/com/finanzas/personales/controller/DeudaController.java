package com.finanzas.personales.controller;

import com.finanzas.personales.dto.request.DeudaRequestDTO;
import com.finanzas.personales.dto.response.DeudaResponseDTO;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.DeudaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/deudas")
@RequiredArgsConstructor
public class DeudaController {

    private final DeudaService deudaService;

    @PostMapping
    public ResponseEntity<DeudaResponseDTO> crear(
            @Valid @RequestBody DeudaRequestDTO dto,
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deudaService.crear(dto, usuario.getId()));
    }

    @GetMapping
    public ResponseEntity<List<DeudaResponseDTO>> listar(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) Boolean saldada) {

        if (saldada != null) {
            return ResponseEntity.ok(
                    deudaService.listarPorEstado(usuario.getId(), saldada)
            );
        }

        return ResponseEntity.ok(
                deudaService.listar(usuario.getId())
        );
    }

    @PatchMapping("/{id}/pagar-cuota")
    public ResponseEntity<DeudaResponseDTO> pagarCuota(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.ok(
                deudaService.pagarCuota(id, usuario.getId())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        deudaService.eliminar(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-pendiente")
    public ResponseEntity<BigDecimal> totalPendiente(
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.ok(
                deudaService.calcularTotalPendiente(usuario.getId())
        );
    }
}
