package com.finanzas.personales.controller;

import com.finanzas.personales.dto.CuentaDTO;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.service.CuentaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDTO dto) {
        return cuentaService.crearCuenta(dto);
    }

    @GetMapping
    public List<Cuenta> listarCuentas() {
        return cuentaService.listarCuentas();
    }

    @GetMapping("/{id}")
    public Cuenta buscarPorId(@PathVariable Long id) {
        return cuentaService.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Cuenta> listarPorUsuario(@PathVariable Long usuarioId) {
        return cuentaService.listarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}")
    public Cuenta actualizarCuenta(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        return cuentaService.actualizarCuenta(id, cuenta);
    }

    @DeleteMapping("/{id}")
    public void eliminarCuenta(@PathVariable Long id) {
        cuentaService.eliminarCuenta(id);
    }
}