
package com.finanzas.personales.controller;


import com.finanzas.personales.dto.request.PresupuestoRequestDTO;
import com.finanzas.personales.model.Presupuesto;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.PresupuestoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/presupuesto")
@RequiredArgsConstructor
public class PresupuestoController {


    private final PresupuestoService presupuestoService;

    @PostMapping
    public Presupuesto guardar(@Valid @RequestBody PresupuestoRequestDTO dto,
                               @AuthenticationPrincipal Usuario usuario) {
        return presupuestoService.guardar(dto, usuario);
    }


    @GetMapping
    public List<Presupuesto> listar(@AuthenticationPrincipal Usuario usuario) {
        return presupuestoService.listar(usuario.getId());
    }


    @GetMapping("/{id}")
    public Presupuesto listarXid(@PathVariable Long id,
                                 @AuthenticationPrincipal Usuario usuario) {
        return presupuestoService.listarXid(id, usuario.getId());
    }


    @DeleteMapping("/{id}")
    public Presupuesto eliminar(@PathVariable Long id,
                                @AuthenticationPrincipal Usuario usuario) {
        return presupuestoService.eliminar(id, usuario.getId());
    }


    @PutMapping("/{id}")
    public Presupuesto actualizar(@PathVariable Long id, @Valid @RequestBody PresupuestoRequestDTO dto,
                                  @AuthenticationPrincipal Usuario usuario) {
        return presupuestoService.actualizar(id, dto, usuario.getId());
    }


}
