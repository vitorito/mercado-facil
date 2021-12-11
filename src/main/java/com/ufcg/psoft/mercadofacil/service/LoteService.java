package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.ItemDoCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemInsuficienteNoEstoque;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface LoteService {

	public List<Lote> listarLotes();

	public void salvarLote(Lote lote);

	public Lote criaLote(int numItens, Produto produto);

	public void removeLote(Lote lote);

	public List<Lote> getLotesByProduto(Produto produto);

	public void retiraItensDoEstoque(List<ItemDoCarrinho> produtos);

	public List<ItemInsuficienteNoEstoque> temNoEstoque(List<ItemDoCarrinho> produtos);
}
