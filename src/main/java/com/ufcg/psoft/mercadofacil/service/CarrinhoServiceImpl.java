package com.ufcg.psoft.mercadofacil.service;

import java.math.BigDecimal;
import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ItemCarrinhoDTO;
import com.ufcg.psoft.mercadofacil.exception.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.exception.ErroProduto;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

	@Autowired
	private CarrinhoRepository carrinhoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Override
	public Carrinho getCarrinhoById(Long idCarrinho) {
		return carrinhoRepository.findById(idCarrinho).orElseThrow(
				() -> ErroCarrinho.erroCarrinhoNaoEncontrado());
	}

	@Override
	public Carrinho cadastraCarrinho(Long idCarrinho) {
		assertCarrinhoNaoCadastrado(idCarrinho);

		Carrinho carrinho = new Carrinho(idCarrinho);
		salvaCarrinho(carrinho);

		return carrinho;
	}

	@Override
	public void removeCarrinho(Long idCarrinho) {
		carrinhoRepository.delete(getCarrinhoById(idCarrinho));
	}

	@Override
	public Carrinho adicionaProdutos(Long idCarrinho, ItemCarrinhoDTO itemCarrinhoDTO) {
		Carrinho carrinho = getCarrinhoById(idCarrinho);
		Produto produto = produtoService.getProdutoById(itemCarrinhoDTO.getIdProduto());

		assertIsdisponivel(produto);

		carrinho.adicionaProdutos(produto, itemCarrinhoDTO.getNumDeItens());
		salvaCarrinho(carrinho);

		return carrinho;
	}

	@Override
	public Carrinho removeProduto(Long idCarrinho, ItemCarrinhoDTO itemCarrinhoDTO) {
		Carrinho carrinho = getCarrinhoById(idCarrinho);
		Produto produto = produtoService.getProdutoById(itemCarrinhoDTO.getIdProduto());
		carrinho.removeProduto(produto, itemCarrinhoDTO.getNumDeItens());
		salvaCarrinho(carrinho);

		return carrinho;
	}

	@Override
	public void removeTodosProdutos(Long idCarrinho) {
		Carrinho carrinho = getCarrinhoById(idCarrinho);
		carrinho.removeTodosProdutos();
		salvaCarrinho(carrinho);
	}

	@Override
	public List<ItemCarrinho> listaItensDoCarrinho(Long idCarrinho) {
		return getCarrinhoById(idCarrinho).getItens();
	}

	@Override
	public List<Produto> listaProdutosDoCarrinho(Long idCarrinho) {
		return getCarrinhoById(idCarrinho).getProdutos();
	}

	@Override
	public BigDecimal calculaTotal(Long idCarrinho) {
		return getCarrinhoById(idCarrinho).getTotal();
	}

	private void salvaCarrinho(Carrinho carrinho) {
		carrinhoRepository.save(carrinho);
	}

	private void assertCarrinhoNaoCadastrado(Long idCarrinho) {
		if (carrinhoRepository.existsById(idCarrinho)) {
			throw ErroCarrinho.erroCarrinhoJaCadastrado();
		}
	}

	private void assertIsdisponivel(Produto produto) {
		if (!produtoService.isDisponivel(produto)) {
			throw ErroProduto.erroProdutoIndisponivel();
		}
	}

}
