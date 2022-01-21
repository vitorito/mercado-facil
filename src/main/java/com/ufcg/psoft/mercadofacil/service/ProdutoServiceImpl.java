package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ufcg.psoft.mercadofacil.DTO.ProdutoDTO;
import com.ufcg.psoft.mercadofacil.exception.ErroProduto;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Override
	public Produto getProdutoById(Long id) {
		return produtoRepository.findById(id).orElseThrow(
				() -> ErroProduto.erroProdutoNaoEncontradoId());
	}

	@Override
	public Produto getProdutoByCodigoBarra(String codigo) {
		return produtoRepository.findByCodigoBarra(codigo).orElseThrow(
				() -> ErroProduto.erroProdutoNaoEncontradoCodigo());
	}

	@Override
	public Produto cadastraProduto(ProdutoDTO produtoDTO) {
		assertProdutoNaoCadastrado(produtoDTO.getCodigoBarra());

		Produto produto = criaProduto(produtoDTO);
		salvaProduto(produto);

		return produto;
	}

	@Override
	public void removeProduto(Long id) {
		Produto produto = getProdutoById(id);
		produtoRepository.delete(produto);
	}

	@Override
	public Produto atualizaProduto(ProdutoDTO produtoDTO) {
		Produto produto = getProdutoByCodigoBarra(produtoDTO.getCodigoBarra());
		atualizaProduto(produtoDTO, produto);
		salvaProduto(produto);

		return produto;
	}

	@Override
	public List<Produto> listaProdutos() {
		List<Produto> produtos = produtoRepository.findAll();

		if (produtos.isEmpty()) {
			throw ErroProduto.erroSemProdutosCadastrados();
		}

		return produtos;
	}

	@Override
	public List<Produto> checaDisponibilidade(List<Produto> produtos) {
		List<Produto> indisponiveis = produtos.stream()
				.filter(p -> !p.isDisponivel())
				.collect(Collectors.toList());
		return indisponiveis;
	}

	@Override
	public boolean isDisponivel(Produto produto) {
		return produto.isDisponivel();
	}

	@Override
	public void tornaDisponivel(Produto produto) {
		produto.tornaDisponivel();
		salvaProduto(produto);
	}

	@Override
	public void tornaIndisponivel(Produto produto) {
		produto.tornaIndisponivel();
		salvaProduto(produto);
	}

	private Produto criaProduto(ProdutoDTO produtoDTO) {
		Produto produto = Produto.builder()
				.nome(produtoDTO.getNome())
				.codigoBarra(produtoDTO.getCodigoBarra())
				.fabricante(produtoDTO.getFabricante())
				.categoria(produtoDTO.getCategoria())
				.preco(produtoDTO.getPreco())
				.build();

		produto.tornaDisponivel();
		return produto;
	}

	private void atualizaProduto(ProdutoDTO produtoDTO, Produto produto) {
		produto.setNome(produtoDTO.getNome());
		produto.setPreco(produtoDTO.getPreco());
		produto.setCodigoBarra(produtoDTO.getCodigoBarra());
		produto.setFabricante(produtoDTO.getFabricante());
		produto.setCategoria(produtoDTO.getCategoria());
	}

	private void salvaProduto(Produto produto) {
		produtoRepository.save(produto);
	}

	private void assertProdutoNaoCadastrado(String codigoBarra) {
		if (!produtoRepository.existsByCodigoBarra(codigoBarra)) {
			throw ErroProduto.erroProdutoJaCadastrado();
		}
	}

}
