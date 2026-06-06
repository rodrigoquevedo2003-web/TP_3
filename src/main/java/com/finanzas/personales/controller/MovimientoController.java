package com.finanzas.personales.controller;

import com.finanzas.personales.dto.MovimientoDTO;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public Movimiento crearMovimiento(@Valid @RequestBody MovimientoDTO dto, @AuthenticationPrincipal Usuario usuario) {
        // Obtenemos el usuario del contexto de seguridad, no de la URL
        return movimientoService.crearMovimiento(dto, usuario.getId());
    }

    @GetMapping
    public List<Movimiento> listarMovimientos(@AuthenticationPrincipal Usuario usuario) {
        // Filtramos para que solo vea SUS movimientos
        return movimientoService.listarMovimientosPorUsuario(usuario.getId());
    }

    @GetMapping("/{id}")
    public Movimiento buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        // Validamos que el ID solicitado pertenezca al usuario logueado
        return movimientoService.buscarPorIdYUsuario(id, usuario.getId());
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<Movimiento> listarPorCuenta(@PathVariable Long cuentaId, @AuthenticationPrincipal Usuario usuario) {
        // Validamos que la cuenta sea del usuario antes de listar
        return movimientoService.listarPorCuentaYUsuario(cuentaId, usuario.getId());
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Movimiento> listarPorCategoria(@PathVariable Long categoriaId, @AuthenticationPrincipal Usuario usuario) {
        // Filtramos por categoría y usuario
        return movimientoService.listarPorCategoriaYUsuario(categoriaId, usuario.getId());
    }

    @DeleteMapping("/{id}")
    public void eliminarMovimiento(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        // Validamos propiedad antes de eliminar
        movimientoService.eliminarMovimiento(id, usuario.getId());
    }
}
