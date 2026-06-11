package com.finanzas.personales.service;

import com.finanzas.personales.Exception.UsuarioNoEncontradoException;
import com.finanzas.personales.dto.request.DeudaRequestDTO;
import com.finanzas.personales.dto.response.DeudaResponseDTO;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Deuda;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.CategoriaRepository;
import com.finanzas.personales.dto.MovimientoDTO;
import com.finanzas.personales.model.Categoria;
import com.finanzas.personales.Exception.CategoriaNoEncontradaException;
import com.finanzas.personales.enums.TipoMovimiento;

import com.finanzas.personales.repository.DeudaRepository;
import com.finanzas.personales.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.finanzas.personales.enums.TipoDeuda;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeudaService {

    private final DeudaRepository deudaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimientoService movimientoService;
    private final UvaService uvaService;

    @Transactional
    public DeudaResponseDTO crear(DeudaRequestDTO dto, Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Deuda deuda = new Deuda();

        deuda.setNombre(dto.getNombre());
        deuda.setDescripcion(dto.getDescripcion());
        if (dto.getTasaUva() != null && dto.getTasaUva()) {
            if (dto.getTipoDeuda() == null || dto.getTipoDeuda() != TipoDeuda.HIPOTECARIO) {
                throw new RuntimeException("Solo las deudas hipotecarias pueden ser indexadas por UVA");
            }
            if (dto.getMontoEnUva() == null) {
                throw new RuntimeException("Monto en UVA es requerido para deuda a tasa UVA");
            }

            BigDecimal uvaValor = uvaService.obtenerValorActual();
            deuda.setTasaUva(true);
            deuda.setMontoEnUva(dto.getMontoEnUva());
            deuda.setUvaValorInicial(uvaValor);

            BigDecimal montoTotalPesos = dto.getMontoEnUva().multiply(uvaValor).setScale(2, RoundingMode.HALF_UP);
            deuda.setMontoTotal(montoTotalPesos);

            deuda.setMontoCuota(
                    montoTotalPesos.divide(BigDecimal.valueOf(dto.getCuotasTotales()), 2, RoundingMode.HALF_UP)
            );
        } else {
            deuda.setTasaUva(false);
            deuda.setMontoEnUva(null);
            deuda.setUvaValorInicial(null);
            deuda.setMontoTotal(dto.getMontoTotal());
            deuda.setMontoCuota(dto.getMontoTotal().divide(BigDecimal.valueOf(dto.getCuotasTotales()), 2, RoundingMode.HALF_UP));
        }

        deuda.setCuotasTotales(dto.getCuotasTotales());
        deuda.setCuotasPagadas(0);
        deuda.setFechaInicio(dto.getFechaInicio());
        deuda.setSaldada(false);
        deuda.setUsuario(usuario);

        deuda.setTipoDeuda(dto.getTipoDeuda());

        if (dto.getCuentaId() != null) {
            Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            if (!cuenta.getUsuario().getId().equals(usuarioId)) {
                throw new RuntimeException("La cuenta no pertenece al usuario");
            }

            deuda.setCuenta(cuenta);
        }

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findByIdAndUsuarioId(dto.getCategoriaId(), usuarioId)
                    .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria no encontrada"));

            deuda.setCategoria(categoria);
        }


        return DeudaResponseDTO.from(deudaRepository.save(deuda));
    }

    public List<DeudaResponseDTO> listar(Long usuarioId) {
        return deudaRepository.findByUsuarioIdOrderByFechaInicioAsc(usuarioId)
                .stream()
                .map(DeudaResponseDTO::from)
                .toList();
    }

    public List<DeudaResponseDTO> listarPorEstado(Long usuarioId, Boolean saldada) {
        return deudaRepository.findByUsuarioIdAndSaldadaOrderByFechaInicioAsc(usuarioId, saldada)
                .stream()
                .map(DeudaResponseDTO::from)
                .toList();
    }

    @Transactional
    public DeudaResponseDTO pagarCuota(Long id, Long usuarioId) {

        Deuda deuda = obtenerPropia(id, usuarioId);

        if (deuda.getSaldada()) {
            throw new RuntimeException("La deuda ya está saldada");
        }

        if (deuda.getCuotasPagadas() >= deuda.getCuotasTotales()) {
            deuda.setSaldada(true);
            return DeudaResponseDTO.from(deudaRepository.save(deuda));
        }

        if (deuda.getCuenta() != null) {
            Cuenta cuenta = deuda.getCuenta();
            Long categoriaId;
            if (deuda.getCategoria() != null) {
                categoriaId = deuda.getCategoria().getId();
            } else {
                var categorias = categoriaRepository.findByUsuarioIdAndTipo(usuarioId, TipoMovimiento.EGRESO);
                var defaultCat = categorias.stream().filter(Categoria::getEsDefault).findFirst();
                if (defaultCat.isPresent()) {
                    categoriaId = defaultCat.get().getId();
                } else if (!categorias.isEmpty()) {
                    categoriaId = categorias.get(0).getId();
                } else {
                    Categoria nueva = new Categoria();
                    nueva.setNombre("Pago deuda");
                    nueva.setTipo(TipoMovimiento.EGRESO);
                    nueva.setEsDefault(false);
                    Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    nueva.setUsuario(usuario);
                    categoriaId = categoriaRepository.save(nueva).getId();
                }
            }

            BigDecimal montoAPagar = deuda.getMontoCuota();
            String descripcion = "Pago cuota: " + deuda.getNombre();
            if (deuda.getTasaUva() != null && deuda.getTasaUva() && deuda.getMontoEnUva() != null) {
                BigDecimal cuotaEnUva = deuda.getMontoEnUva().divide(BigDecimal.valueOf(deuda.getCuotasTotales()), 8, RoundingMode.HALF_UP);
                BigDecimal uvaActual = uvaService.obtenerValorActual();
                montoAPagar = cuotaEnUva.multiply(uvaActual).setScale(2, RoundingMode.HALF_UP);
                descripcion = descripcion + " (UVA - valor: " + uvaActual + ")";
            }

            MovimientoDTO mvDto = new MovimientoDTO();
            mvDto.setCuentaId(cuenta.getId());
            mvDto.setCategoriaId(categoriaId);
            mvDto.setTipo(TipoMovimiento.EGRESO);
            mvDto.setDescripcion(descripcion);
            mvDto.setMonto(montoAPagar);
            movimientoService.crearMovimiento(mvDto, usuarioId);
        }

        deuda.setCuotasPagadas(deuda.getCuotasPagadas() + 1);

        if (deuda.getCuotasPagadas() >= deuda.getCuotasTotales()) {
            deuda.setSaldada(true);
        }

        return DeudaResponseDTO.from(deudaRepository.save(deuda));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioId) {
        Deuda deuda = obtenerPropia(id, usuarioId);
        deudaRepository.delete(deuda);
    }

    public BigDecimal calcularTotalPendiente(Long usuarioId) {
        var deudas = deudaRepository.findByUsuarioIdAndSaldadaOrderByFechaInicioAsc(usuarioId, false);
        boolean anyUva = deudas.stream().anyMatch(d -> d.getTasaUva() != null && d.getTasaUva());
        BigDecimal uvaActual = null;
        if (anyUva) {
            uvaActual = uvaService.obtenerValorActual();
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Deuda d : deudas) {
            int restantes = d.getCuotasTotales() - d.getCuotasPagadas();
            if (restantes <= 0) continue;
            if (d.getTasaUva() != null && d.getTasaUva() && d.getMontoEnUva() != null && uvaActual != null) {
                BigDecimal cuotaEnUva = d.getMontoEnUva().divide(BigDecimal.valueOf(d.getCuotasTotales()), 8, RoundingMode.HALF_UP);
                BigDecimal restanteUva = cuotaEnUva.multiply(BigDecimal.valueOf(restantes));
                total = total.add(restanteUva.multiply(uvaActual).setScale(2, RoundingMode.HALF_UP));
            } else {
                total = total.add(d.getMontoCuota().multiply(BigDecimal.valueOf(restantes)));
            }
        }
        return total;
    }

    private Deuda obtenerPropia(Long id, Long usuarioId) {
        return deudaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
    }

    public DeudaResponseDTO obtener(Long id, Long usuarioId) {
        return DeudaResponseDTO.from(
                obtenerPropia(id, usuarioId)
        );
    }
}
