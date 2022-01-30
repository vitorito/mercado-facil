package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufcg.psoft.mercadofacil.exception.ErroCarrinho;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Carrinho {

	@Id
	@Getter
	@NonNull
	@JsonIgnore
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<ItemCarrinho> produtos = new ArrayList<>();

	public List<ItemCarrinho> getItens() {
		return new ArrayList<>(produtos);
	}

	public void adicionaProdutos(Produto produto, int quantidade) {
		Optional<ItemCarrinho> itemOptional = getItemDoCarrinho(produto);

		if (itemOptional.isPresent()) {
			ItemCarrinho item = itemOptional.get();
			int novaQuantidade = item.getQuantidade() + quantidade;
			item.setQuantidade(novaQuantidade);
			return;
		}

		produtos.add(new ItemCarrinho(produto, quantidade));
	}

	public void removeProduto(Produto produto, int quantidade) {
		ItemCarrinho itemDoCarrinho = assertTemProduto(produto);
		int novaQuantidade = itemDoCarrinho.getQuantidade() - quantidade;

		if (novaQuantidade > 0) {
			itemDoCarrinho.setQuantidade(novaQuantidade);
			return;
		}

		produtos.remove(itemDoCarrinho);
	}

	public void removeTodosProdutos() {
		produtos.clear();
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

}
