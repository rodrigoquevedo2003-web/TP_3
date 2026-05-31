package com.finanzas.personales.service;

import com.finanzas.personales.Exception.PresupuestoInexistenteException;
import com.finanzas.personales.model.Presupuesto;
import com.finanzas.personales.repository.PresupuestoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;

    public PresupuestoService(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    public Presupuesto guardar(Presupuesto presupuesto){
        return presupuestoRepository.save(presupuesto);
    }

    public List<Presupuesto> listar(){
        return presupuestoRepository.findAll();
    }

    public Presupuesto listarXid(Long id){
        return presupuestoRepository.findById(id).orElseThrow(() -> new PresupuestoInexistenteException("Presupuesto inexistente."));
    }

    public Presupuesto eliminar(Long id){
        Presupuesto p = presupuestoRepository.findById(id).orElseThrow(() -> new PresupuestoInexistenteException("Presupuesto inexistente."));

        presupuestoRepository.delete(p);

        return p;
    }

    @Transactional
    public Presupuesto actualizar(Long id, Presupuesto presupuesto){
        Presupuesto p = presupuestoRepository.findById(id).orElseThrow(() -> new PresupuestoInexistenteException("Presupuesto inexistente."));

        p.setAnio(presupuesto.getAnio());
        p.setCategoria(presupuesto.getCategoria());
        p.setAnio(presupuesto.getAnio());
        p.setMes(presupuesto.getMes());
        p.setUsuario(presupuesto.getUsuario());
        p.setMontoConsumido(presupuesto.getMontoConsumido());
        p.setMontoLimite(presupuesto.getMontoLimite());

        return presupuestoRepository.save(p);
    }

    @Transactional
    public boolean verificarLimite(Presupuesto presupuesto, BigDecimal nuevoGasto) {
        BigDecimal total = presupuesto.getMontoConsumido().add(nuevoGasto);

        if (total.compareTo(presupuesto.getMontoLimite()) > 0) {
            return false;
        }

        return true;
    }

    public BigDecimal calcularDisponible(Long idPresupuesto){
        Presupuesto p = presupuestoRepository.findById(idPresupuesto).orElseThrow(() -> new PresupuestoInexistenteException("presupuesto inexistente"));

        BigDecimal resto = p.getMontoLimite().subtract(p.getMontoConsumido());

        return resto;
    }
    
}
