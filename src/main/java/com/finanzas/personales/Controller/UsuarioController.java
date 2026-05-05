package com.finanzas.personales.controller;

import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public String crearUsuario(@RequestBody Usuario usuario) {
        usuarioService.crearUsuario(usuario);
        return "Usuario creado correctamente";
    }
}