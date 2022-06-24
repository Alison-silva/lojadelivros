package com.alison.lojadelivros.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alison.lojadelivros.model.Livro;

@Repository
@Transactional
public interface LivroRepository extends JpaRepository<Livro, Long> {

}
