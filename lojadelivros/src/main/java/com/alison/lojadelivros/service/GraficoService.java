package com.alison.lojadelivros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GraficoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> GerarGrafico() {

        String sql = "select trunc(avg(ic.quantidade),2) as media, l.titulo" +
                " from livro l " +
                " inner join itens_compra ic on ic.livro_id = l.id " +
                " group by l.id";

        return jdbcTemplate.queryForList(sql);

    }
}
