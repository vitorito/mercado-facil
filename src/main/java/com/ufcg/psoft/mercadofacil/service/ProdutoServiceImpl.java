package com.ufcg.psoft.mercadofacil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.DTO.ProdutoDTO;
import com.ufcg.psoft.mercadofacil.model.ItemDoCarrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Optional<Produto> getProdutoById(long id) {
		return produtoRepository.findById(id);
	}
	
	public List<Produto> getProdutoByCodigoBarra(String codigo) {
		return produtoRepository.findByCodigoBarra(codigo);
	}
	
	public void removerProdutoCadastrado(Produto produto) {
		produtoRepository.delete(produto);
	}

	public void salvarProdutoCadastrado(Produto produto) {
		produtoRepository.save(produto);		
	}

	public List<Produto> listarProdutos() {
		return produtoRepository.findAll();
	}

	public Produto criaProduto(ProdutoDTO produtoDTO) {
		Produto produto = new Produto(produtoDTO.getNome(), produtoDTO.getFabricante(), produtoDTO.getCodigoBarra(),
				produtoDTO.getPreco(), produtoDTO.getCategoria());
		
		produto.tornaDisponivel();
		return produto;
	}

	@Override
	public List<Produto> checaDisponibilidade(List<ItemDoCarrinho> produtos) {
		List<Produto> indisponiveis = new ArrayList<>();
		
		for (ItemDoCarrinho item : produtos) {
			Produto produto = item.getProduto();
			if (!produto.isDisponivel()) {
				indisponiveis.add(produto);
			}
		}

		return indisponiveis;
	}

	public Produto atualizaProduto(ProdutoDTO produtoDTO, Produto produto) {
		produto.setNome(produtoDTO.getNome());
		produto.setPreco(produtoDTO.getPreco());
		produto.setCodigoBarra(produtoDTO.getCodigoBarra());
		produto.mudaFabricante(produtoDTO.getFabricante());
		produto.mudaCategoria(produtoDTO.getCategoria());
		
		return produto;
	}

	@Override
	public boolean isDisponivel(Produto produto) {
		return produto.isDisponivel();
	}
}
