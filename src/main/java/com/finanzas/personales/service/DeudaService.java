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
            if (dto.getInterestRate() == null || dto.getInterestPeriod() == null) {
                throw new RuntimeException("Interest rate y period deben proporcionarse para deuda UVA hipotecaria");
            }
            if (dto.getInterestPeriod() != com.finanzas.personales.enums.PeriodicidadInteres.ANUAL) {
                throw new RuntimeException("Las deudas hipotecarias con UVA deben tener interés anual");
            }
            if (dto.getMontoEnUva().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El monto en UVA debe ser mayor que cero");
            }
            if (dto.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("El interestRate no puede ser negativo");
            }

            BigDecimal uvaValor = uvaService.obtenerValorActual();
            deuda.setTasaUva(true);
            deuda.setMontoEnUva(dto.getMontoEnUva());
            deuda.setUvaValorInicial(uvaValor);
            deuda.setInterestRate(dto.getInterestRate());
            deuda.setInterestPeriod(dto.getInterestPeriod());

            BigDecimal montoTotalPesos = dto.getMontoEnUva().multiply(uvaValor).setScale(2, RoundingMode.HALF_UP);
            deuda.setMontoTotal(montoTotalPesos);

            BigDecimal cuotaEnUva = calcularCuota(dto.getMontoEnUva(), dto.getInterestRate(), true, dto.getCuotasTotales());
            deuda.setMontoCuota(cuotaEnUva.multiply(uvaValor).setScale(2, RoundingMode.HALF_UP));
        } else {
            deuda.setTasaUva(false);
            deuda.setMontoEnUva(null);
            deuda.setUvaValorInicial(null);
            deuda.setInterestRate(dto.getInterestRate());
            deuda.setInterestPeriod(dto.getInterestPeriod());

            if (dto.getInterestRate() != null && dto.getInterestRate().compareTo(BigDecimal.ZERO) > 0) {
                if (dto.getInterestPeriod() == null) {
                    throw new RuntimeException("interestPeriod es requerido cuando se provee interestRate");
                }
                if (dto.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
                    throw new RuntimeException("El interestRate no puede ser negativo");
                }
                BigDecimal cuota = calcularCuota(dto.getMontoTotal(), dto.getInterestRate(), dto.getInterestPeriod() == com.finanzas.personales.enums.PeriodicidadInteres.ANUAL, dto.getCuotasTotales());
                deuda.setMontoCuota(cuota);
                deuda.setMontoTotal(dto.getMontoTotal());
            } else {
                deuda.setMontoTotal(dto.getMontoTotal());
                deuda.setMontoCuota(dto.getMontoTotal().divide(BigDecimal.valueOf(dto.getCuotasTotales()), 2, RoundingMode.HALF_UP));
            }
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

    private BigDecimal calcularCuota(BigDecimal principal, BigDecimal annualRateOrPeriodRate, boolean annual, int cuotas) {
        if (cuotas <= 0) return BigDecimal.ZERO;
        BigDecimal periodicRate;
        if (annual) {
            periodicRate = annualRateOrPeriodRate.divide(BigDecimal.valueOf(12), 18, RoundingMode.HALF_UP);
        } else {
            periodicRate = annualRateOrPeriodRate;
        }

        if (periodicRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(cuotas), 2, RoundingMode.HALF_UP);
        }

        java.math.MathContext mc = new java.math.MathContext(18, RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(periodicRate, mc);
        BigDecimal denomPow = onePlusR.pow(cuotas, mc);
        BigDecimal denom = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(denomPow, mc), mc);
        if (denom.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(cuotas), 2, RoundingMode.HALF_UP);
        }

        BigDecimal numer = principal.multiply(periodicRate, mc);
        BigDecimal cuota = numer.divide(denom, 2, RoundingMode.HALF_UP);
        return cuota;
    }

    public List<DeudaResponseDTO> listar(Long usuarioId) {
        return deudaRepository.findByUsuarioIdOrderByFechaInicioAsc(usuarioId)
                .stream()
                .map(DeudaResponseDTO::from)
                .toList();
    }

    public List<DeudaResponseDTO> listarPorTipo(Long usuarioId, TipoDeuda tipo) {
        return deudaRepository.findByUsuarioIdOrderByFechaInicioAsc(usuarioId)
                .stream()
                .filter(d -> d.getTipoDeuda() == tipo)
                .map(DeudaResponseDTO::from)
                .toList();
    }

    public List<DeudaResponseDTO> listarPorEstadoYTipo(Long usuarioId, Boolean saldada, TipoDeuda tipo) {
        return deudaRepository.findByUsuarioIdAndSaldadaOrderByFechaInicioAsc(usuarioId, saldada)
                .stream()
                .filter(d -> d.getTipoDeuda() == tipo)
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
                BigDecimal cuotaEnUva;
                if (deuda.getInterestRate() != null && deuda.getInterestRate().compareTo(BigDecimal.ZERO) > 0) {
                    cuotaEnUva = calcularCuota(deuda.getMontoEnUva(), deuda.getInterestRate(), true, deuda.getCuotasTotales());
                } else {
                    cuotaEnUva = deuda.getMontoEnUva().divide(BigDecimal.valueOf(deuda.getCuotasTotales()), 8, RoundingMode.HALF_UP);
                }
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
            mvDto.setDeudaId(deuda.getId());
            movimientoService.crearMovimiento(mvDto, usuarioId);
        }

        deuda.setCuotasPagadas(deuda.getCuotasPagadas() + 1);

        if (deuda.getCuotasPagadas() >= deuda.getCuotasTotales()) {
            deuda.setSaldada(true);
        }

        return DeudaResponseDTO.from(deudaRepository.save(deuda));
    }

    @Transactional
    public DeudaResponseDTO pagarCuotasAdelantadas(Long id, int cantidad, Long usuarioId) {

        if (cantidad <= 0) throw new RuntimeException("La cantidad de cuotas a pagar debe ser mayor a cero");

        Deuda deuda = obtenerPropia(id, usuarioId);

        if (deuda.getSaldada()) throw new RuntimeException("La deuda ya está saldada");

        if (deuda.getTipoDeuda() != null && deuda.getTipoDeuda() != TipoDeuda.HIPOTECARIO && deuda.getTasaUva() != null && deuda.getTasaUva()) {
            throw new RuntimeException("Solo las deudas hipotecarias pueden ser adelantadas por UVA");
        }

        int restantes = deuda.getCuotasTotales() - deuda.getCuotasPagadas();
        int aPagar = Math.min(cantidad, restantes);
        if (aPagar <= 0) {
            deuda.setSaldada(true);
            return DeudaResponseDTO.from(deudaRepository.save(deuda));
        }

        if (deuda.getCuenta() == null) {
            throw new RuntimeException("La deuda no tiene cuenta asociada para realizar el pago");
        }

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

        java.math.BigDecimal montoTotalAPagar = java.math.BigDecimal.ZERO;
        if (deuda.getTasaUva() != null && deuda.getTasaUva() && deuda.getMontoEnUva() != null) {
            java.math.BigDecimal cuotaEnUva;
            if (deuda.getInterestRate() != null && deuda.getInterestRate().compareTo(java.math.BigDecimal.ZERO) > 0) {
                cuotaEnUva = calcularCuota(deuda.getMontoEnUva(), deuda.getInterestRate(), true, deuda.getCuotasTotales());
            } else {
                cuotaEnUva = deuda.getMontoEnUva().divide(java.math.BigDecimal.valueOf(deuda.getCuotasTotales()), 8, RoundingMode.HALF_UP);
            }
            java.math.BigDecimal uvaActual = uvaService.obtenerValorActual();
            montoTotalAPagar = cuotaEnUva.multiply(uvaActual).multiply(java.math.BigDecimal.valueOf(aPagar)).setScale(2, RoundingMode.HALF_UP);
        } else {
            if (deuda.getInterestRate() != null && deuda.getInterestRate().compareTo(java.math.BigDecimal.ZERO) > 0) {
                java.math.BigDecimal cuota = calcularCuota(deuda.getMontoTotal(), deuda.getInterestRate(), deuda.getInterestPeriod() == com.finanzas.personales.enums.PeriodicidadInteres.ANUAL, deuda.getCuotasTotales());
                montoTotalAPagar = cuota.multiply(java.math.BigDecimal.valueOf(aPagar));
            } else {
                montoTotalAPagar = deuda.getMontoCuota().multiply(java.math.BigDecimal.valueOf(aPagar));
            }
        }

        MovimientoDTO mvDto = new MovimientoDTO();
        mvDto.setCuentaId(deuda.getCuenta().getId());
        mvDto.setCategoriaId(categoriaId);
        mvDto.setTipo(TipoMovimiento.EGRESO);
        mvDto.setDescripcion("Pago adelantado de " + aPagar + " cuotas: " + deuda.getNombre());
        mvDto.setMonto(montoTotalAPagar);
        mvDto.setDeudaId(deuda.getId());

        movimientoService.crearMovimiento(mvDto, usuarioId);

        deuda.setCuotasPagadas(deuda.getCuotasPagadas() + aPagar);
        if (deuda.getCuotasPagadas() >= deuda.getCuotasTotales()) deuda.setSaldada(true);

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
                BigDecimal cuotaEnUva;
                if (d.getInterestRate() != null && d.getInterestRate().compareTo(BigDecimal.ZERO) > 0) {
                    cuotaEnUva = calcularCuota(d.getMontoEnUva(), d.getInterestRate(), true, d.getCuotasTotales());
                } else {
                    cuotaEnUva = d.getMontoEnUva().divide(BigDecimal.valueOf(d.getCuotasTotales()), 8, RoundingMode.HALF_UP);
                }
                BigDecimal restanteUva = cuotaEnUva.multiply(BigDecimal.valueOf(restantes));
                total = total.add(restanteUva.multiply(uvaActual).setScale(2, RoundingMode.HALF_UP));
            } else {
                total = total.add(d.getMontoCuota().multiply(BigDecimal.valueOf(restantes)));
            }
        }
        return total;
    }

    public com.finanzas.personales.dto.response.ResumenDeudasDTO resumen(Long usuarioId) {
        var deudas = deudaRepository.findByUsuarioIdOrderByFechaInicioAsc(usuarioId);
        BigDecimal totalPendiente = BigDecimal.ZERO;
        java.util.Map<String, Integer> countPorTipo = new java.util.HashMap<>();
        java.util.Map<String, BigDecimal> pendientePorTipo = new java.util.HashMap<>();

        boolean anyUva = deudas.stream().anyMatch(d -> d.getTasaUva() != null && d.getTasaUva());
        BigDecimal uvaActual = null;
        if (anyUva) uvaActual = uvaService.obtenerValorActual();

        for (Deuda d : deudas) {
            String tipo = d.getTipoDeuda() != null ? d.getTipoDeuda().name() : "OTRO";
            countPorTipo.put(tipo, countPorTipo.getOrDefault(tipo, 0) + 1);

            int restantes = d.getCuotasTotales() - d.getCuotasPagadas();
            BigDecimal pendiente = BigDecimal.ZERO;
            if (restantes > 0) {
                if (d.getTasaUva() != null && d.getTasaUva() && d.getMontoEnUva() != null && uvaActual != null) {
                    BigDecimal cuotaEnUva = d.getMontoEnUva().divide(BigDecimal.valueOf(d.getCuotasTotales()), 8, RoundingMode.HALF_UP);
                    pendiente = cuotaEnUva.multiply(BigDecimal.valueOf(restantes)).multiply(uvaActual).setScale(2, RoundingMode.HALF_UP);
                } else {
                    pendiente = d.getMontoCuota().multiply(BigDecimal.valueOf(restantes));
                }
            }

            pendientePorTipo.put(tipo, pendientePorTipo.getOrDefault(tipo, BigDecimal.ZERO).add(pendiente));
            totalPendiente = totalPendiente.add(pendiente);
        }

        com.finanzas.personales.dto.response.ResumenDeudasDTO resumen = new com.finanzas.personales.dto.response.ResumenDeudasDTO();
        resumen.setTotalDeudas(deudas.size());
        resumen.setTotalPendiente(totalPendiente);
        resumen.setCountPorTipo(countPorTipo);
        resumen.setPendientePorTipo(pendientePorTipo);

        return resumen;
    }

    private Deuda obtenerPropia(Long id, Long usuarioId) {
        return deudaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new com.finanzas.personales.Exception.RecursoNoEncontradoException("Deuda no encontrada"));
    }

    public DeudaResponseDTO obtener(Long id, Long usuarioId) {
        Deuda deuda = obtenerPropia(id, usuarioId);
        DeudaResponseDTO dto = DeudaResponseDTO.from(deuda);

        if (deuda.getTasaUva() != null && deuda.getTasaUva() && deuda.getMontoEnUva() != null) {
            BigDecimal uvaActual = uvaService.obtenerValorActual();
            BigDecimal cuotaEnUva;
            if (deuda.getInterestRate() != null && deuda.getInterestRate().compareTo(BigDecimal.ZERO) > 0) {
                cuotaEnUva = calcularCuota(deuda.getMontoEnUva(), deuda.getInterestRate(), true, deuda.getCuotasTotales());
            } else {
                cuotaEnUva = deuda.getMontoEnUva().divide(BigDecimal.valueOf(deuda.getCuotasTotales()), 8, RoundingMode.HALF_UP);
            }
            BigDecimal cuotaPesos = cuotaEnUva.multiply(uvaActual).setScale(2, RoundingMode.HALF_UP);
            int restantes = deuda.getCuotasTotales() - deuda.getCuotasPagadas();
            BigDecimal pendientePesos = cuotaEnUva.multiply(BigDecimal.valueOf(restantes)).multiply(uvaActual).setScale(2, RoundingMode.HALF_UP);

            dto.setCuotaActualPesos(cuotaPesos);
            dto.setMontoActualPesos(deuda.getMontoEnUva().multiply(uvaActual).setScale(2, RoundingMode.HALF_UP));
            dto.setPendienteActualPesos(pendientePesos);
        } else {
            if (deuda.getInterestRate() != null && deuda.getInterestRate().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal cuota = calcularCuota(deuda.getMontoTotal(), deuda.getInterestRate(), deuda.getInterestPeriod() == com.finanzas.personales.enums.PeriodicidadInteres.ANUAL, deuda.getCuotasTotales());
                dto.setCuotaActualPesos(cuota);
                dto.setMontoActualPesos(deuda.getMontoTotal());
                int restantes = deuda.getCuotasTotales() - deuda.getCuotasPagadas();
                dto.setPendienteActualPesos(cuota.multiply(BigDecimal.valueOf(restantes)));
            } else {
                dto.setCuotaActualPesos(deuda.getMontoCuota());
                dto.setMontoActualPesos(deuda.getMontoTotal());
                int restantes = deuda.getCuotasTotales() - deuda.getCuotasPagadas();
                dto.setPendienteActualPesos(deuda.getMontoCuota().multiply(BigDecimal.valueOf(restantes)));
            }
        }

        // agregar historial de pagos y total pagado
        var movimientos = movimientoService.listarPorDeudaYUsuario(deuda.getId(), usuarioId);
        java.math.BigDecimal totalPagado = java.math.BigDecimal.ZERO;
        java.util.List<com.finanzas.personales.dto.response.PagoDTO> pagos = new java.util.ArrayList<>();
        for (var m : movimientos) {
            com.finanzas.personales.dto.response.PagoDTO p = new com.finanzas.personales.dto.response.PagoDTO();
            p.setId(m.getId());
            p.setMonto(m.getMonto());
            p.setFecha(m.getFecha());
            p.setDescripcion(m.getDescripcion());
            p.setCuentaNombre(m.getCuenta() != null ? m.getCuenta().getNombre() : null);
            p.setCategoriaNombre(m.getCategoria() != null ? m.getCategoria().getNombre() : null);
            pagos.add(p);
            totalPagado = totalPagado.add(m.getMonto());
        }

        dto.setPagos(pagos);
        dto.setTotalPagado(totalPagado);

        return dto;
    }
}
