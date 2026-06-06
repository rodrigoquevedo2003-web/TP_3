package com.finanzas.personales.service;

import com.finanzas.personales.Exception.UsuarioNoEncontradoException;
import com.finanzas.personales.dto.request.SuscripcionRequestDTO;
import com.finanzas.personales.dto.response.SuscripcionResponseDTO;
import com.finanzas.personales.model.Suscripcion;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.SuscripcionRepository;
import com.finanzas.personales.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public SuscripcionResponseDTO crear(SuscripcionRequestDTO dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setNombre(dto.getNombre());
        suscripcion.setMonto(dto.getMonto());
        suscripcion.setDiaCobro(dto.getDiaCobro());
        suscripcion.setActiva(true);
        suscripcion.setUsuario(usuario);

        return SuscripcionResponseDTO.from(suscripcionRepository.save(suscripcion));
    }

    public List<SuscripcionResponseDTO> listar(Long usuarioId) {
        return suscripcionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(SuscripcionResponseDTO::from)
                .toList();
    }

    public List<SuscripcionResponseDTO> listarPorEstado(Long usuarioId, Boolean activa) {
        return suscripcionRepository.findByUsuarioIdAndActiva(usuarioId, activa)
                .stream()
                .map(SuscripcionResponseDTO::from)
                .toList();
    }

    @Transactional
    public SuscripcionResponseDTO toggleActiva(Long id, Long usuarioId) {
        Suscripcion suscripcion = obtenerPropia(id, usuarioId);
        suscripcion.setActiva(!suscripcion.getActiva());
        return SuscripcionResponseDTO.from(suscripcionRepository.save(suscripcion));
    }

    @Transactional
    public SuscripcionResponseDTO actualizar(Long id, SuscripcionRequestDTO dto, Long usuarioId) {
        Suscripcion suscripcion = obtenerPropia(id, usuarioId);
        suscripcion.setNombre(dto.getNombre());
        suscripcion.setMonto(dto.getMonto());
        suscripcion.setDiaCobro(dto.getDiaCobro());
        return SuscripcionResponseDTO.from(suscripcionRepository.save(suscripcion));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioId) {
        Suscripcion suscripcion = obtenerPropia(id, usuarioId);
        suscripcionRepository.delete(suscripcion);
    }

    public BigDecimal calcularTotalMensual(Long usuarioId) {
        return suscripcionRepository.findByUsuarioIdAndActiva(usuarioId, true)
                .stream()
                .map(Suscripcion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Suscripcion obtenerPropia(Long id, Long usuarioId) {
        return suscripcionRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
    }
}
