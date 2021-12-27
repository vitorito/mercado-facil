package com.ufcg.psoft.mercadofacil.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Carrinho {

	@Id
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemDoCarrinho> produtos;

	public Carrinho() {
	}

	public Carrinho(Long id) {
		this.id = id;
		this.produtos = new ArrayList<>();
	}

	public List<ItemDoCarrinho> getProdutos() {
		return new ArrayList<>(produtos);
	}

	public void adicionaProdutos(Produto produto, int numDeItens) {
		ItemDoCarrinho itemDoCarrinho = getItemDoCarrinho(produto);

		if (itemDoCarrinho != null) {
			int novaQuantidadeDeProdutos = itemDoCarrinho.getNumDeItens() + numDeItens;
			itemDoCarrinho.setNumDeItens(novaQuantidadeDeProdutos);
			return;
		}

		produtos.add(new ItemDoCarrinho(produto, numDeItens));
	}

	public void removeProdutos(Produto produto, int numDeItens) {
		ItemDoCarrinho itemDoCarrinho = getItemDoCarrinho(produto);
		int novaQuantidadeDeProdutos = itemDoCarrinho.getNumDeItens() - numDeItens;

		if (novaQuantidadeDeProdutos > 0) {
			itemDoCarrinho.setNumDeItens(novaQuantidadeDeProdutos);
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

	public boolean containsProduto(Produto produto) {
		return this.getItemDoCarrinho(produto) != null;
	}

	private ItemDoCarrinho getItemDoCarrinho(Produto produto) {
		ItemDoCarrinho result = null;

		for (ItemDoCarrinho item : produtos) {
			if (item.getProduto().equals(produto)) {
				result = item;
				break;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		String produtosToStr = "";

		for (ItemDoCarrinho item : produtos) {
			produtosToStr += item.getProduto().toString() + "\n";
		}

		return "Carrinho id=" + id + "\nprodutos=" + produtosToStr;
	}

}
