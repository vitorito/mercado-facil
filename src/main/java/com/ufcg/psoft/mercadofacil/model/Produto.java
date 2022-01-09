package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;

	@NonNull
	private String nome;

	@NonNull
	private String codigoBarra;

	@NonNull
	private String fabricante;

	@NonNull
	private String categoria;

	@NonNull
	private BigDecimal preco;

	private boolean isDisponivel = false;

	public void tornaDisponivel() {
		this.isDisponivel = true;
	}

	public void tornaIndisponivel() {
		this.isDisponivel = false;
	}

}