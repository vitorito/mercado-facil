package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface CarrinhoService {
	
	public Optional<Carrinho> getcarrinhoById(Long id);
	
	public Carrinho criaCarrinho(long id);
	
	public void salvaCarrinho(Carrinho carrinho);

	public void adicionaProduto(Carrinho carrinho, Produto produto, int numDeItens);
	
	public void removeProduto(Carrinho carrinho, Produto produto, int numDeItens);
	
	public void removeTodosProdutos(Carrinho carrinho);
	
	public List<Produto> listaProdutos(Carrinho carrinho);
}
