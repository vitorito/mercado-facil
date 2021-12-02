package com.ufcg.psoft.mercadofacil.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Carrinho {
	
	@Id
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL)
    private List<Produto> produtos;
    
    public Carrinho(Long id) {
    	this.id = id;
	}

	public List<Produto> getProduto() {
		return produtos;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Carrinho [id=" + id + ", produto=" + produtos.toString() + "]";
	}

}
