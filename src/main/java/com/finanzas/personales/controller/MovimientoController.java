package com.finanzas.personales.controller;

import com.finanzas.personales.dto.request.MovimientoRequestDTO;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;


    @PostMapping
    public ResponseEntity<Movimiento> crearMovimiento(@Valid @RequestBody MovimientoRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.crearMovimiento(dto, usuario.getId()));
    }

    @GetMapping
    public ResponseEntity<List<Movimiento>> listarMovimientos(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(movimientoService.listarMovimientosPorUsuario(usuario.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(movimientoService.buscarPorIdYUsuario(id, usuario.getId()));
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<Movimiento>> listarPorCuenta(@PathVariable Long cuentaId, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(movimientoService.listarPorCuentaYUsuario(cuentaId, usuario.getId()));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Movimiento>> listarPorCategoria(@PathVariable Long categoriaId, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(movimientoService.listarPorCategoriaYUsuario(categoriaId, usuario.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
       movimientoService.eliminarMovimiento(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movimiento> editarMovimiento(@PathVariable Long id, @Valid @RequestBody MovimientoRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(movimientoService.editarMovimiento(id, dto, usuario.getId()));
    }
}
