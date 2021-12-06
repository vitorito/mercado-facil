package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.repository.CompraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraServiceImpl implements CompraService {

	@Autowired
	CompraRepository compraRepository;

	@Override
	public Compra finalizaCompra(Cliente cliente, Carrinho carrinho) {
		return new Compra(cliente, carrinho.getProdutos());
	}

	@Override
	public void salvaCompra(Compra compra) {
		compraRepository.save(compra);
	}
	
}
