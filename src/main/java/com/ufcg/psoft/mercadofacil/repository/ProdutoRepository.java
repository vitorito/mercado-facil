package com.ufcg.psoft.mercadofacil.repository;

import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Produto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

	Optional<Produto> findByCodigoBarra(String codigoBarra);

	boolean existsByCodigoBarra(String codigo);
}
