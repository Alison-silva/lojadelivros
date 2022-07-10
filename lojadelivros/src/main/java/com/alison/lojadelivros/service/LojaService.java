package com.alison.lojadelivros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LojaService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void alterarQuantidade(Long idLoja, Integer quantidade) {
		String sql = "begin; UPDATE Livro SET estoque = estoque -"+quantidade+" where id = " + idLoja +"; commit;";
		jdbcTemplate.execute(sql);
	}
	
	
}
