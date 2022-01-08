package com.ufcg.psoft.mercadofacil.service;

import java.math.BigDecimal;
import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ItemCarrinhoDTO;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface CarrinhoService {
	
	public Carrinho getCarrinhoById(Long id);
	
	public Carrinho cadastraCarrinho(Long id);
	 
	public void removeCarrinho(Long id);

	public Carrinho adicionaProdutos(Long idCliente, ItemCarrinhoDTO itemCarrinhoDTO);
	
	public Carrinho removeProduto(Long idCliente, ItemCarrinhoDTO itemCarrinhoDTO);
	
	public Carrinho removeTodosProdutos(Long idCliente);
	
	public List<ItemCarrinho> listaItensDoCarrinho(Long id);

	public List<Produto> getProdutosDoCarrinho(Long id);

	public BigDecimal calculaTotal(Long idCarrinho);

	public Carrinho getCarrinhoByCliente(Long idCliente);

}
