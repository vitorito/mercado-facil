package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;


public interface CompraService {

	public Compra criaCompra(Cliente cliente, Carrinho carrinho);

	public void salvaCompra(Compra compra);

}
