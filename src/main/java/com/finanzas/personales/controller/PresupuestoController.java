
package com.finanzas.personales.controller;


import com.finanzas.personales.model.Presupuesto;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.PresupuestoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/presupuesto")
public class PresupuestoController {


    private final PresupuestoService presupuestoService;


    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }
    @PostMapping
    public Presupuesto guardar(@RequestBody Presupuesto presupuesto,
                               @AuthenticationPrincipal Usuario usuario) {
        return presupuestoService.guardar(presupuesto, usuario);
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
    public Presupuesto actualizar(@PathVariable Long id,
                                  @RequestBody Presupuesto presupuesto,
                                  @AuthenticationPrincipal Usuario usuario) {
        return presupuestoService.actualizar(id, presupuesto, usuario.getId());
    }


}
