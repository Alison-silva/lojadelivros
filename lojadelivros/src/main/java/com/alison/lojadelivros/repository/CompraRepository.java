package com.alison.lojadelivros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alison.lojadelivros.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>{

}
