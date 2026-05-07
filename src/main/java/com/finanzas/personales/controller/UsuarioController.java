package com.finanzas.personales.controller;

import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import com.finanzas.personales.dto.LoginDTO;
import com.finanzas.personales.dto.LoginDTO;
import jakarta.servlet.http.HttpSession;

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

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginDTO loginDTO) {

        return usuarioService.login(loginDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO, HttpSession session) {

        Usuario usuario = usuarioService.login(loginDTO);

        session.setAttribute("usuarioLogueado", usuario);

        return "Sesión iniciada correctamente";
    }

    @GetMapping("/actual")
    public Usuario usuarioActual(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            throw new RuntimeException("No hay usuario logueado");
        }

        return usuario;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "Sesión cerrada correctamente";
    }
}