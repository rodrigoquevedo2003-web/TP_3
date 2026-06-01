package com.finanzas.personales.controller;

import com.finanzas.personales.dto.MovimientoDTO;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.service.MovimientoService;
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
    public Movimiento crearMovimiento(@RequestBody MovimientoDTO dto) {
        return movimientoService.crearMovimiento(dto);
    }

    @GetMapping
    public List<Movimiento> listarMovimientos() {
        return movimientoService.listarMovimientos();
    }

    @GetMapping("/{id}")
    public Movimiento buscarPorId(@PathVariable Long id) {
        return movimientoService.buscarPorId(id);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<Movimiento> listarPorCuenta(@PathVariable Long cuentaId) {
        return movimientoService.listarPorCuenta(cuentaId);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Movimiento> listarPorCategoria(@PathVariable Long categoriaId) {
        return movimientoService.listarPorCategoria(categoriaId);
    }

    @DeleteMapping("/{id}")
    public void eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
    }
}