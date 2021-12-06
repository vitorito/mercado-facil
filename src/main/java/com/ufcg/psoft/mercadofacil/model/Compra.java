package com.ufcg.psoft.mercadofacil.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Compra {

	static final String FORMATO_DATA = "dd/MM/yyyy HH:mm:ss";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	Cliente cliente;

	@OneToMany
	private List<ItemDoCarrinho> produtos;

	private String data;

	public Compra(Cliente cliente, List<ItemDoCarrinho> produtos) {
		this.cliente = cliente;
		this.produtos = produtos;

		defineData();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public List<ItemDoCarrinho> getProdutos() {
		return produtos;
	}

	public String getData() {
		return this.data;
	}

	private void defineData() {
		SimpleDateFormat sdf = new SimpleDateFormat(Compra.FORMATO_DATA);
		this.data = sdf.format(new Date());
	}

}
