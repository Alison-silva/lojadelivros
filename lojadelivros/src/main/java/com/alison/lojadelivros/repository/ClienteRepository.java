package com.alison.lojadelivros.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alison.lojadelivros.model.Cliente;

@Repository
@Transactional
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	@Query("select c from Cliente c where c.nome like %?1% ")
	List<Cliente> findClienteByName(String nome);

	
	default Page<Cliente> findClienteByNamePage(String nome, Pageable pageable){
		
		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
			.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Cliente> example = Example.of(cliente, exampleMatcher);
		
		Page<Cliente> clientes = findAll(example, pageable);
		
		return clientes;
	}
	
}
