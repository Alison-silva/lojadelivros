package com.alison.lojadelivros.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alison.lojadelivros.model.Telefone;

@Repository
@Transactional
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
	
	@Query("select t from Telefone t where t.cliente.id = ?1")
	public List<Telefone> getTelefones(Long clienteid);
	
	@Query("select t from Telefone t where t.fornecedor.id = ?1")
	public List<Telefone> getTelefonesFornecedor(Long fornecedorid);
	

}
