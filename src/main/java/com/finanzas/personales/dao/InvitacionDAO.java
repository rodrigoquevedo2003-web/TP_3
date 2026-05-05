package com.finanzas.personales.dao;

import com.finanzas.personales.model.Invitacion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InvitacionDAO {

    private final JdbcTemplate jdbcTemplate;

    public InvitacionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void crearInvitacion(Invitacion invitacion) {
        String sql = "INSERT INTO invitaciones " +
                "(id_familia, id_usuario_invita, id_usuario_invitado, email_invitado, rol_invitado) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                invitacion.getIdFamilia(),
                invitacion.getIdUsuarioInvita(),
                invitacion.getIdUsuarioInvitado(),
                invitacion.getEmailInvitado(),
                invitacion.getRolInvitado()
        );
    }

    public void aceptarInvitacion(Integer idInvitacion) {
        String sql = "UPDATE invitaciones SET estado = 'ACEPTADA' WHERE id_invitacion = ?";

        jdbcTemplate.update(sql, idInvitacion);
    }

    public void rechazarInvitacion(Integer idInvitacion) {
        String sql = "UPDATE invitaciones SET estado = 'RECHAZADA' WHERE id_invitacion = ?";

        jdbcTemplate.update(sql, idInvitacion);
    }
}
