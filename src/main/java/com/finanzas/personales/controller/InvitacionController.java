package com.finanzas.personales.controller;

import com.finanzas.personales.model.Invitacion;
import com.finanzas.personales.service.InvitacionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitaciones")
public class InvitacionController {

    private final InvitacionService invitacionService;

    public InvitacionController(InvitacionService invitacionService) {
        this.invitacionService = invitacionService;
    }

    @PostMapping
    public String crearInvitacion(@RequestBody Invitacion invitacion) {
        invitacionService.crearInvitacion(invitacion);
        return "Invitación creada correctamente";
    }

    @PutMapping("/{idInvitacion}/aceptar")
    public String aceptarInvitacion(@PathVariable Integer idInvitacion) {
        invitacionService.aceptarInvitacion(idInvitacion);
        return "Invitación aceptada correctamente";
    }

    @PutMapping("/{idInvitacion}/rechazar")
    public String rechazarInvitacion(@PathVariable Integer idInvitacion) {
        invitacionService.rechazarInvitacion(idInvitacion);
        return "Invitación rechazada correctamente";
    }
}
