package com.ufcg.psoft.mercadofacil.model;

import java.util.Arrays;

import com.ufcg.psoft.mercadofacil.exception.ErroCompra;

public enum TipoEntrega {

    PADRAO, EXPRESS, RETIRADA;

    public static TipoEntrega get(String tipo) {
		try {
			return valueOf(tipo.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw ErroCompra.erroTipoEntregaInvalida();
		}
	}

    public static String valuesToString() {
        return Arrays.toString(values());
    }
    
}
