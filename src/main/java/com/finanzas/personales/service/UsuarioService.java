package com.finanzas.personales.service;

import com.finanzas.personales.dao.UsuarioDAO;
import com.finanzas.personales.model.Usuario;
import org.springframework.stereotype.Service;
import com.finanzas.personales.dto.LoginDTO;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void crearUsuario(Usuario usuario) {
        usuarioDAO.guardar(usuario);
    }

    public Usuario login(LoginDTO loginDTO) {

        Usuario usuario = usuarioDAO.buscarPorEmail(
                loginDTO.getEmail()
        );

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!usuario.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }
}