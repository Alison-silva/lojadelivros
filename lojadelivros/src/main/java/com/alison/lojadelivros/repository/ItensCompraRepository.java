package com.alison.lojadelivros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alison.lojadelivros.model.ItensCompra;

@Repository
public interface ItensCompraRepository extends JpaRepository<ItensCompra, Long> {
	
	

}
