package com.finanzas.personales.controller;

import com.finanzas.personales.model.Presupuesto;
import com.finanzas.personales.service.PresupuestoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presupuesto")
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @PostMapping()
    public Presupuesto guardar(@RequestBody Presupuesto presupuesto){
        return presupuestoService.guardar(presupuesto);
    }

    @GetMapping()
    public List<Presupuesto> listar(){
        return presupuestoService.listar();
    }

    @GetMapping("/{id}")
    public Presupuesto listarXid(@PathVariable Long id){
        return presupuestoService.listarXid(id);
    }

    @DeleteMapping("/{id}")
    public Presupuesto eliminar(@PathVariable Long id){
        return presupuestoService.eliminar(id);
    }

    @PutMapping("/{id}")
    public Presupuesto actualizar(@PathVariable Long id, @RequestBody Presupuesto presupuesto){
        return presupuestoService.actualizar(id, presupuesto);
    }

}
