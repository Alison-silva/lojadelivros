package com.alison.lojadelivros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alison.lojadelivros.model.ItensCompra;

@Repository
public interface ItensCompraRepository extends JpaRepository<ItensCompra, Long> {
	
	@Query("select p from ItensCompra p where p.compra.id = ?1")
	public List<ItensCompra> getItens(Long idpedido);

}
