package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ProdutoDTO;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface ProdutoService {

	public Produto getProdutoById(Long id);

	public Produto getProdutoByCodigoBarra(String codigo);

	public void removeProduto(Long id);

	public List<Produto> listaProdutos();

	public Produto cadastraProduto(ProdutoDTO produto);

	public Produto atualizaProduto(ProdutoDTO produtoDTO);

	public List<Produto> checaDisponibilidade(List<Produto> produtos);

	public boolean isDisponivel(Produto Produto);

	public void tornaDisponivel(Long idProduto);

	public void tornaDisponivel(Produto produto);

	public void tornaIndisponivel(Long idProduto);

	public void tornaIndisponivel(Produto produto);


}
