package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufcg.psoft.mercadofacil.exception.ErroCarrinho;

@Entity
public class Carrinho {

	@Id
	@JsonIgnore
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<ItemCarrinho> produtos;

	public Carrinho() {
	}

	public Carrinho(Long id) {
		this.id = id;
		this.produtos = new ArrayList<>();
	}

	public List<ItemCarrinho> getItens() {
		return new ArrayList<>(produtos);
	}

	@JsonIgnore
	public List<Produto> getProdutos() {
		List<Produto> produtosList = produtos.stream()
				.map(ItemCarrinho::getProduto)
				.collect(Collectors.toList());

		return produtosList;
	}

	public void adicionaProdutos(Produto produto, int numDeItens) {
		Optional<ItemCarrinho> itemOptional = getItemDoCarrinho(produto);

		if (itemOptional.isPresent()) {
			ItemCarrinho item = itemOptional.get();
			int novaQuantidade = item.getNumDeItens() + numDeItens;
			item.setNumDeItens(novaQuantidade);
			return;
		}

		produtos.add(new ItemCarrinho(produto, numDeItens));
	}

	public void removeProduto(Produto produto, int numDeItens) {
		ItemCarrinho itemDoCarrinho = assertTemProduto(produto);
		int novaQuantidade = itemDoCarrinho.getNumDeItens() - numDeItens;

		if (novaQuantidade > 0) {
			itemDoCarrinho.setNumDeItens(novaQuantidade);
			return;
		}

		produtos.remove(itemDoCarrinho);
	}

	public void removeTodosProdutos() {
		produtos.clear();
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getTotal() {
		BigDecimal total = produtos.stream()
			.map(ItemCarrinho::getSubtotal)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return total;
	}

	public boolean containsProduto(Produto produto) {
		return this.getItemDoCarrinho(produto).isPresent();
	}

	private ItemCarrinho assertTemProduto(Produto produto) {
		return getItemDoCarrinho(produto).orElseThrow(
				() -> ErroCarrinho.erroCarrinhoNaoTemProduto());
	}

	private Optional<ItemCarrinho> getItemDoCarrinho(Produto produto) {
		Optional<ItemCarrinho> result = produtos.stream()
				.filter(item -> item.getProduto().equals(produto))
				.findAny();

		return result;
	}

	@Override
	public String toString() {
		String produtosToStr = "";

		for (ItemCarrinho item : produtos) {
			produtosToStr += item.getProduto() + "\n";
		}

		return "Carrinho id=" + id + "\nprodutos=" + produtosToStr;
	}

}
