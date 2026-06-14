package com.finanzas.personales.controller;

import com.finanzas.personales.dto.request.CuentaRequestDTO;
import com.finanzas.personales.dto.request.CuentaUpdateRequestDTO;
import com.finanzas.personales.dto.request.TransferenciaRequestDTO;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;


    @PostMapping
    public Cuenta crearCuenta(@Valid @RequestBody CuentaRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
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
    public Cuenta actualizarCuenta(@PathVariable Long id, @Valid @RequestBody CuentaUpdateRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return cuentaService.actualizarCuenta(id, dto, usuario.getId());
    }

    @DeleteMapping("/{id}")
    public void eliminarCuenta(@PathVariable Long id,  @AuthenticationPrincipal Usuario usuario) {
        cuentaService.eliminarCuenta(id, usuario.getId());
    }

    @PostMapping("/transferir")
    public String transferir(@Valid @RequestBody TransferenciaRequestDTO dto,
                             @AuthenticationPrincipal Usuario usuario) {

        cuentaService.transferir(dto, usuario.getId());

        return "Transferencia realizada correctamente";
    }
}