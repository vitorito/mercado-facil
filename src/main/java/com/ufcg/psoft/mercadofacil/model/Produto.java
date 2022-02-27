package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;

	private String nome;

	@EqualsAndHashCode.Include
	private String codigoBarra;

	private String fabricante;

	private String categoria;

	private BigDecimal preco;

	private boolean isDisponivel;

	@Enumerated(EnumType.STRING)
	private TipoTransporte tipoTransporte;

	@Builder
	private Produto(String nome, String codigoBarra, String fabricante, String categoria,
			BigDecimal preco, TipoTransporte tipoTransporte) {
		this.nome = nome;
		this.codigoBarra = codigoBarra;
		this.fabricante = fabricante;
		this.categoria = categoria;
		this.preco = preco;
		this.tipoTransporte = tipoTransporte;
		this.isDisponivel = false;
	}

	public void tornaDisponivel() {
		this.isDisponivel = true;
	}

	public void tornaIndisponivel() {
		this.isDisponivel = false;
	}

    public BigDecimal getSubtotal(Integer quantidade) {
        return this.preco.multiply(BigDecimal.valueOf(quantidade));
    }

}