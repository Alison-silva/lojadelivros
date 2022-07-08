package com.alison.lojadelivros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alison.lojadelivros.model.Compra;


@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>{
	
	@Query("select c from Compra c where c.cliente.id = ?1")
	public List<Compra> getFindId(Long idpedido);

}
