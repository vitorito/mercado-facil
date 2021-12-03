package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface CarrinhoService {
	
	public Optional<Carrinho> getCarrinhoById(Long id);
	
	public Carrinho criaCarrinho(Long id);
	
	public void removeCarrinho(Carrinho carrinho);

	public void salvaCarrinho(Carrinho carrinho);

	public void adicionaProdutos(Carrinho carrinho, Produto produto, int numDeItens);
	
	public void removeProdutos(Carrinho carrinho, Produto produto, int numDeItens);
	
	public void removeTodosProdutos(Carrinho carrinho);
	
	public List<Produto> listaProdutos(Carrinho carrinho);

}
