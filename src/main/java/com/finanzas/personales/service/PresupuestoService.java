package com.finanzas.personales.service;


import com.finanzas.personales.Exception.PresupuestoInexistenteException;
import com.finanzas.personales.model.Presupuesto;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.PresupuestoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
public class PresupuestoService {


    private final PresupuestoRepository presupuestoRepository;


    public PresupuestoService(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }


    public Presupuesto guardar(Presupuesto presupuesto, Usuario usuario) {
        presupuesto.setUsuario(usuario);
        return presupuestoRepository.save(presupuesto);
    }


    public List<Presupuesto> listar(Long usuarioId) {
        return presupuestoRepository.findByUsuarioId(usuarioId);
    }


    public Presupuesto listarXid(Long id, Long usuarioId) {
        Presupuesto p = presupuestoRepository.findById(id)
                .orElseThrow(() -> new PresupuestoInexistenteException("Presupuesto inexistente."));
        if (!p.getUsuario().getId().equals(usuarioId)) {
            throw new PresupuestoInexistenteException("Presupuesto no encontrado o no autorizado");
        }
        return p;
    }


    public Presupuesto eliminar(Long id, Long usuarioId) {
        Presupuesto p = listarXid(id, usuarioId);
        presupuestoRepository.delete(p);
        return p;
    }


    @Transactional
    public Presupuesto actualizar(Long id, Presupuesto presupuesto, Long usuarioId) {
        Presupuesto p = listarXid(id, usuarioId);


        p.setAnio(presupuesto.getAnio());
        p.setCategoria(presupuesto.getCategoria());
        p.setMes(presupuesto.getMes());
        p.setMontoConsumido(presupuesto.getMontoConsumido());
        p.setMontoLimite(presupuesto.getMontoLimite());




        return presupuestoRepository.save(p);
    }

}
