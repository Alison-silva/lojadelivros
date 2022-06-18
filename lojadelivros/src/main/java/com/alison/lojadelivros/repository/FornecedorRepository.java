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

import com.alison.lojadelivros.model.Fornecedor;

@Repository
@Transactional
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>{
	
	
	@Query("select f from Fornecedor f where f.razaoSocial like %?1% ")
	List<Fornecedor> findFornecedorByName(String razaoSocial);
	
	default Page<Fornecedor> findFornecedorByNamePage(String razaoSocial, Pageable pageable){
		
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setRazaoSocial(razaoSocial);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("razaoSocial", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
	
		Example<Fornecedor> example = Example.of(fornecedor, exampleMatcher);
		
		Page<Fornecedor> fornecedores = findAll(example, pageable);
		
		return fornecedores;
	}

}
