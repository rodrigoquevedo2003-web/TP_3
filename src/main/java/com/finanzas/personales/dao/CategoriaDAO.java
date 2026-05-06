package com.finanzas.personales.dao;

import com.finanzas.personales.model.Categoria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaDAO {

    private final JdbcTemplate jdbcTemplate;

    public CategoriaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Categoria> listarTodas() {
        String sql = "SELECT * FROM categorias";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Categoria c = new Categoria();
            c.setIdCategoria(rs.getInt("id_categoria"));
            c.setNombre(rs.getString("nombre"));
            c.setTipo(rs.getString("tipo"));
            c.setFija(rs.getBoolean("fija"));
            return c;
        });
    }

    public List<Categoria> listarPorTipo(String tipo) {
        String sql = "SELECT * FROM categorias WHERE tipo = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Categoria c = new Categoria();
            c.setIdCategoria(rs.getInt("id_categoria"));
            c.setNombre(rs.getString("nombre"));
            c.setTipo(rs.getString("tipo"));
            c.setFija(rs.getBoolean("fija"));
            return c;
        }, tipo);
    }
}