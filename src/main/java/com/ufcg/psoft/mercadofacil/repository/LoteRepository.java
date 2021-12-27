package com.ufcg.psoft.mercadofacil.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface LoteRepository extends JpaRepository<Lote, Long>{

	List<Lote> findByProduto(Produto produto);
}