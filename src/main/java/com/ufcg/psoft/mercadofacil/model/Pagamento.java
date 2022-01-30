package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private FormaDePagamento formaDePagamento;

	private BigDecimal totalCompra;

	private BigDecimal taxaJuros;

	private BigDecimal valorJuros;

	private BigDecimal taxaDesconto;

	private BigDecimal valorDesconto;

	private BigDecimal totalPago;

	@Builder
	private Pagamento(BigDecimal totalCompra, FormaDePagamento formaDePagamento,
			BigDecimal taxaDesconto) {
		this.formaDePagamento = formaDePagamento;
		this.totalCompra = totalCompra;
		this.taxaDesconto = taxaDesconto;
		this.taxaJuros = formaDePagamento.getJuros();

		calculaTotalPago();
	}

	private void calculaTotalPago() {
		this.valorJuros = totalCompra.multiply(taxaJuros);
		this.valorDesconto = totalCompra.multiply(taxaDesconto);
		this.totalPago = totalCompra.add(valorJuros).subtract(valorDesconto);
	}

}
