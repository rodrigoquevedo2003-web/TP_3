package com.finanzas.personales.service;

import com.finanzas.personales.dao.UsuarioDAO;
import com.finanzas.personales.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void crearUsuario(Usuario usuario) {
        usuarioDAO.guardar(usuario);
    }
}