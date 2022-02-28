package com.ufcg.psoft.mercadofacil.model;

import java.util.Arrays;

import com.ufcg.psoft.mercadofacil.exception.ErroProduto;

public enum TipoTransporte {

    REFRIGERADO, FRAGIL, COMUM;

	public static TipoTransporte get(String tipo) {
		try {
			return valueOf(tipo.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw ErroProduto.erroTipoTransporteInvalido();
		}
	} 

    public static String valuesToString() {
        return Arrays.toString(values());
    }

}
