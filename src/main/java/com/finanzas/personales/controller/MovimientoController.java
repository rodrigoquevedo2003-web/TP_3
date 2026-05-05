package com.finanzas.personales.controller;

import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.service.MovimientoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public String crearMovimiento(@RequestBody Movimiento movimiento) {
        movimientoService.crearMovimiento(movimiento);
        return "Movimiento creado correctamente";
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Movimiento> listarPorUsuario(@PathVariable Integer idUsuario) {
        return movimientoService.listarPorUsuario(idUsuario);
    }

    @GetMapping("/familia/{idFamilia}")
    public List<Movimiento> listarPorFamilia(@PathVariable Integer idFamilia) {
        return movimientoService.listarPorFamilia(idFamilia);
    }

    @GetMapping("/saldo/usuario/{idUsuario}")
    public BigDecimal calcularSaldoUsuario(@PathVariable Integer idUsuario) {
        return movimientoService.calcularSaldoUsuario(idUsuario);
    }

    @GetMapping("/saldo/familia/{idFamilia}")
    public BigDecimal calcularSaldoFamilia(@PathVariable Integer idFamilia) {
        return movimientoService.calcularSaldoFamilia(idFamilia);
    }
}