package com.ufcg.psoft.mercadofacil.repository;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long> {

	public List<Compra> findByCliente(Cliente cliente);

}
