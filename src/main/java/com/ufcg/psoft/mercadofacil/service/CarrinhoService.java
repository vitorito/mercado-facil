package com.ufcg.psoft.mercadofacil.service;

import java.math.BigDecimal;
import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ItemCarrinhoDTO;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface CarrinhoService {
	
	Carrinho getCarrinhoById(Long id);
	
	Carrinho cadastraCarrinho(Long id);
	 
	void removeCarrinho(Long id);

	Carrinho adicionaProdutos(Long idCarrinho, ItemCarrinhoDTO itemCarrinhoDTO);
	
	Carrinho removeProduto(Long idCarrinho, ItemCarrinhoDTO itemCarrinhoDTO);
	
	void removeTodosProdutos(Long idCarrinho);
	
	List<ItemCarrinho> listaItensDoCarrinho(Long id);

	List<Produto> listaProdutosDoCarrinho(Long id);

	BigDecimal calculaTotal(Long idCarrinho);

}
