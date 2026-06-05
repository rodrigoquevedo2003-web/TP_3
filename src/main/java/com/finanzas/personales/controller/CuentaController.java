package com.finanzas.personales.controller;

import com.finanzas.personales.dto.CuentaDTO;
import com.finanzas.personales.dto.request.TransferenciaDTO;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.CuentaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Cuenta crearCuenta(@RequestBody CuentaDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return cuentaService.crearCuenta(dto, usuario);
    }

    @GetMapping
    public List<Cuenta> listarCuentas(@AuthenticationPrincipal Usuario usuario) {
        return cuentaService.listarPorUsuario(usuario.getId());
    }

    @GetMapping("/{id}")
    public Cuenta buscarPorId(@PathVariable Long id,  @AuthenticationPrincipal Usuario usuario) {
        return cuentaService.buscarPropia(id, usuario.getId());
    }


    @PutMapping("/{id}")
    public Cuenta actualizarCuenta(@PathVariable Long id, @RequestBody Cuenta cuenta,  @AuthenticationPrincipal Usuario usuario) {
        return cuentaService.actualizarCuenta(id, cuenta, usuario.getId());
    }

    @DeleteMapping("/{id}")
    public void eliminarCuenta(@PathVariable Long id,  @AuthenticationPrincipal Usuario usuario) {
        cuentaService.eliminarCuenta(id, usuario.getId());
    }

    @PostMapping("/transferir")
    public String transferir(@RequestBody TransferenciaDTO dto) {

        cuentaService.transferir(dto);

        return "Transferencia realizada correctamente";
    }
}