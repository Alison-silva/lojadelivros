package com.alison.lojadelivros.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alison.lojadelivros.model.dto.LivroDTO;

@Service
public class NotaCompraService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public List<LivroDTO> GerarRelatorioNota(Long idCompra){
		
		List<LivroDTO> retorno = new ArrayList<LivroDTO>();
		
		String sql = " select liv.id as codigoLivro, liv.titulo as titulo, liv.preco as valorUnitario, "
				+ " ic.quantidade as qtde, com.valor_total as valorTotal, com.data_compra as dataDaCompra, "
				+ " cli.id as codigoCliente, cli.nome as nomeCliente "
				+ " from compra as com "
				+ " inner join itens_compra as ic on com.id = compra_id "
				+ " inner join livro as liv on liv.id = ic.livro_id "
				+ " inner join cliente as cli on cli.id = com.cliente_id "
				+ " where com.id = "+ idCompra +";";
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LivroDTO.class));
		
		return retorno;
		
	}
	
}
