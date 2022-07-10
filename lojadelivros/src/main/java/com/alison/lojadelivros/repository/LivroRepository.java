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

import com.alison.lojadelivros.model.Livro;

@Repository
@Transactional
public interface LivroRepository extends JpaRepository<Livro, Long> {
	
	@Query("select c from Livro c where c.titulo like %?1% ")
	List<Livro> findLivroByName(String titulo);
	
	
	default Page<Livro> findLivroByNamePage(String titulo, Pageable pageable){
		
		Livro livro = new Livro();
		livro.setTitulo(titulo);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
			.withMatcher("titulo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Livro> example = Example.of(livro, exampleMatcher);
		
		Page<Livro> livros = findAll(example, pageable);
		
		return livros;
	}
	
	default Page<Livro> findLivroByGeneroPage(String genero, Pageable pageable){
		
		Livro livro = new Livro();
		livro.setGenero(genero);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
			.withMatcher("genero", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Livro> example = Example.of(livro, exampleMatcher);
		
		Page<Livro> livros = findAll(example, pageable);
		
		return livros;
	}

}
