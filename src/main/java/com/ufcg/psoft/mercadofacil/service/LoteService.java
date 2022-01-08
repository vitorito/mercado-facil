package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface LoteService {

	public List<Lote> listaLotes();

	public Lote cadastraLote(Long idProduto, int numItens);

	public List<Lote> getLotesByProduto(Produto produto);

	public void retiraItensDoEstoque(List<ItemCarrinho> produtos);

	public List<ItemSemEstoque> temEmEstoque(List<ItemCarrinho> produtos);

}
