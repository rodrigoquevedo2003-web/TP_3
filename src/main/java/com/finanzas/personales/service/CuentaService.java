package com.finanzas.personales.service;

import com.finanzas.personales.dto.CuentaDTO;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.finanzas.personales.Exception.UsuarioNoEncontradoException;
import com.finanzas.personales.Exception.CuentaNoEncontradaException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;


    public Cuenta crearCuenta(CuentaDTO dto, Usuario usuario) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(dto.getNombre());
        cuenta.setSaldo(dto.getSaldo());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setActiva(true);
        cuenta.setUsuario(usuario);
        return cuentaRepository.save(cuenta);
    }

    public List<Cuenta> listarCuentas() {
        return cuentaRepository.findAll();
    }

    public Cuenta buscarPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    public List<Cuenta> listarPorUsuario(Long usuarioId) {
        return cuentaRepository.findByUsuarioId(usuarioId);
    }

    public Cuenta buscarPropia(Long id, Long usuarioId) {
        return cuentaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    public Cuenta actualizarCuenta(Long id, Cuenta cuentaActualizada, Long usuarioId) {
        Cuenta cuenta = buscarPropia(id, usuarioId);

        cuenta.setNombre(cuentaActualizada.getNombre());
        cuenta.setSaldo(cuentaActualizada.getSaldo());

        return cuentaRepository.save(cuenta);
    }

    public void eliminarCuenta(Long id, Long usuarioId) {
        Cuenta cuenta = buscarPropia(id, usuarioId);
        cuentaRepository.delete(cuenta);
    }
}