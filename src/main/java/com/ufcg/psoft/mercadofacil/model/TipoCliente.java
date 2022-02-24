package com.ufcg.psoft.mercadofacil.model;

import java.util.Arrays;

import com.ufcg.psoft.mercadofacil.exception.ErroCliente;

public enum TipoCliente {

	NORMAL,
	ESPECIAL,
	PREMIUM;

	public static TipoCliente get(String tipo) {
		try {
			return valueOf(tipo.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw ErroCliente.erroTipoInvalidoDeCliente();
		}
	}

	public static String valuesToString() {
		return Arrays.toString(values());
	}

}
