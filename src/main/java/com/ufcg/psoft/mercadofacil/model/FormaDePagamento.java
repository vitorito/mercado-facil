package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;
import java.util.Arrays;

import lombok.Getter;

public enum FormaDePagamento {
	
	BOLETO(0), 
	PAYPAL(0.02), 
	CREDITO(0.05);

	@Getter
	private BigDecimal juros;

	FormaDePagamento(double juros) {
		this.juros = BigDecimal.valueOf(juros);
	}

	public static String valuesToString() {
		return Arrays.toString(values());
	}

}
