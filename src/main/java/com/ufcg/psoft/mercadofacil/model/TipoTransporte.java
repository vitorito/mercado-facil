package com.ufcg.psoft.mercadofacil.model;

import java.util.Arrays;

import com.ufcg.psoft.mercadofacil.exception.ErroProduto;

import lombok.Getter;

public enum TipoTransporte {

    REFRIGERADO(new CalculoDeEntregaRefrigerada()), 
	FRAGIL(new CalculoDeEntregaFragil()), 
	COMUM(new CalculoDeEntregaComum());

	@Getter
	private CalculoDeEntregaPorTipoTransporte estrategiaCalculo;

	TipoTransporte(CalculoDeEntregaPorTipoTransporte estrategiaCalculo) {
		this.estrategiaCalculo = estrategiaCalculo;
	}

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
