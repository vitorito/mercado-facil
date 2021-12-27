package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

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
	public Compra criaCompra(Cliente cliente, Carrinho carrinho) {
		return new Compra(cliente, carrinho.getProdutos());
	}

	@Override
	public void salvaCompra(Compra compra) {
		compraRepository.save(compra);
	}

	@Override
	public List<Compra> listaCompras(Cliente cliente) {
		return compraRepository.findByCliente(cliente);
	}

	@Override
	public Optional<Compra> getCompraById(long idCompra) {
		return compraRepository.findById(idCompra);
	}
	
}
