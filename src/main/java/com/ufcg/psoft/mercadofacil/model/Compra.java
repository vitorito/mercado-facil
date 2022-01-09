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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Compra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@ManyToOne
	Cliente cliente;

	@NonNull
	@Getter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL)
	private List<ItemCarrinho> produtos;

	@NonNull
	private BigDecimal total;

	private LocalDateTime data = LocalDateTime.now(ZoneId.of("Z"));

	public List<ItemCarrinho> getProdutos() {
		return new ArrayList<>(produtos);
	}

}
