package com.finanzas.personales.service;

import com.finanzas.personales.dto.request.TarjetaRequestDTO;
import com.finanzas.personales.model.TarjetaCredito;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.TarjetaCreditoRepository;
import com.finanzas.personales.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarjetaService {

    private final TarjetaCreditoRepository tarjetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;

    public TarjetaCredito crear(TarjetaRequestDTO dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TarjetaCredito t = new TarjetaCredito();
        t.setNombre(dto.getNombre());
        t.setUsuario(usuario);
        if (dto.getCuentaPagoId() != null) {
            t.setCuentaPago(cuentaRepository.findById(dto.getCuentaPagoId()).orElseThrow(() -> new RuntimeException("Cuenta no encontrada")));
        }
        t.setLimiteCredito(dto.getLimiteCredito());
        t.setCierreDia(dto.getCierreDia());
        t.setDiasVencimiento(dto.getDiasVencimiento());
        t.setTasaAnual(dto.getTasaAnual());

        return tarjetaRepository.save(t);
    }

    public List<TarjetaCredito> listar(Long usuarioId) {
        return tarjetaRepository.findByUsuarioId(usuarioId);
    }

    public TarjetaCredito obtener(Long id, Long usuarioId) {
        return tarjetaRepository.findById(id)
                .filter(t -> t.getUsuario().getId().equals(usuarioId))
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
    }
}

