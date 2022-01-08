package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Compra {

	public static final String FORMATO_DATA = "dd/MM/yyyy HH:mm:ss";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	Cliente cliente;

	@OneToMany(cascade = CascadeType.ALL)
	private List<ItemCarrinho> produtos;

	private LocalDateTime data;

	private BigDecimal total;

	public Compra() {
	}

	public Compra(Cliente cliente, List<ItemCarrinho> produtos, BigDecimal total) {
		this.cliente = cliente;
		this.produtos = produtos;
		this.total = total;
		this.data = LocalDateTime.now(ZoneId.of("Z"));
	}

	public Cliente getCliente() {
		return cliente;
	}

	public List<ItemCarrinho> getProdutos() {
		return new ArrayList<>(produtos);
	}

	public LocalDateTime getData() {
		return this.data;
	}

	public BigDecimal getTotal() {
		return total;
	}

}
