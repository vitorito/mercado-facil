package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ItemCarrinho {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Produto produto;

	@Column(name = "quantidade")
	private int numDeItens;

	public ItemCarrinho() {
	}
	
	public ItemCarrinho(Produto produto, int numDeItens) {
		this.produto = produto;
		this.numDeItens = numDeItens;
	}

	public BigDecimal getSubtotal() {
		BigDecimal numItens = new BigDecimal(getNumDeItens());
		return getProduto().getPreco().multiply(numItens);
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
