package com.finanzas.personales.service;


import com.finanzas.personales.dto.AuthResponseDTO;
import com.finanzas.personales.dto.LoginDTO;
import com.finanzas.personales.dto.RegisterDTO;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.UsuarioRepository;
import com.finanzas.personales.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.finanzas.personales.Exception.EmailYaRegistradoException;
import com.finanzas.personales.Exception.UsuarioNoEncontradoException;
import com.finanzas.personales.Exception.CredencialesInvalidasException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDTO registrar(RegisterDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailYaRegistradoException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        usuarioRepository.save(usuario);

        String token = jwtService.generarToken(usuario.getEmail());
        return new AuthResponseDTO(token, usuario.getEmail());
    }

    public AuthResponseDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getEmail());
        return new AuthResponseDTO(token, usuario.getEmail());
    }
}

