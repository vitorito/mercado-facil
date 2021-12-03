package com.ufcg.psoft.mercadofacil.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Carrinho {
	
	@Id
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL)
    private Map<Produto, Integer> produtos;
    
    public Carrinho(Long id) {
    	this.id = id;
		this.produtos = new HashMap<>();
	}

	public Map<Produto, Integer> getProduto() {
		return new HashMap<>(produtos);
	}
	
	public void adicionaProdutos(Produto produto, int numDeItens) {
		if (produtos.containsKey(produto)) {
			int novaQuantidadeDeProdutos = produtos.get(produto) + numDeItens;
			produtos.replace(produto, novaQuantidadeDeProdutos);
			return;
		}
		
		produtos.put(produto, numDeItens);
	}

	public void removeProdutos(Produto produto, int numDeItens) {
		int novaQuantidadeDeProdutos = produtos.get(produto) - numDeItens;

		if (novaQuantidadeDeProdutos > 0) {
			produtos.replace(produto, novaQuantidadeDeProdutos);
			return;
		}

		produtos.remove(produto);
	}

	public void removeTodosProdutos() {
		produtos.clear();
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Carrinho [id=" + id + ", produto=" + produtos.toString() + "]";
	}


}
