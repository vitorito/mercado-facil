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

	private BigDecimal valorEntrega;

	@Builder
	private Pagamento(BigDecimal totalCompra, FormaDePagamento formaDePagamento,
			BigDecimal taxaDesconto, BigDecimal valorEntrega) {
		this.formaDePagamento = formaDePagamento;
		this.totalCompra = totalCompra;
		this.taxaDesconto = taxaDesconto;
		this.taxaJuros = formaDePagamento.getJuros();
		this.valorEntrega = valorEntrega;

		calculaValorJuros();
		calculaValorDesconto();
		calculaTotalPago();
	}

	private void calculaValorDesconto() {
		this.valorDesconto = totalCompra.multiply(taxaDesconto);
	}

	private void calculaValorJuros() {
		this.valorJuros = totalCompra.multiply(taxaJuros);
	}

	private void calculaTotalPago() {
		BigDecimal totalParcial = totalCompra.add(valorJuros).subtract(valorDesconto);
		this.totalPago = totalParcial.add(valorEntrega);
	}

}
