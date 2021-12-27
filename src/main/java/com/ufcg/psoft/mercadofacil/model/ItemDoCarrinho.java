package com.ufcg.psoft.mercadofacil.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ItemDoCarrinho {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	private Produto produto;

	@Column(name = "quantidade")
	private int numDeItens;

	public ItemDoCarrinho() {}
	
	public ItemDoCarrinho(Produto produto, int numDeItens) {
		this.produto = produto;
		this.numDeItens = numDeItens;
	}

	public Produto getProduto() {
		return this.produto;
	}

	public int getNumDeItens() {
		return numDeItens;
	}

	public void setNumDeItens(int numDeItens) {
		this.numDeItens = numDeItens;
	}

}
