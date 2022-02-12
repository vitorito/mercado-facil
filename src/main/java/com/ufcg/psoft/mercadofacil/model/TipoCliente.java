package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;
import java.util.Arrays;

import lombok.Getter;

@Getter
public enum TipoCliente {

	NORMAL(0, 0),
	ESPECIAL(0.01, 10),
	PREMIUM(0.01, 5);

	BigDecimal desconto;

	int minItens;

	TipoCliente(double desconto, int minItens) {
		this.desconto = BigDecimal.valueOf(desconto);
		this.minItens = minItens;
	}

	public static String valuesToString() {
		return Arrays.toString(values());
	}

}
