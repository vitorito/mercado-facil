package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public class CalculoDeEntregaFragil implements CalculoDeEntregaPorTipoTransporte {

    @Override
    public BigDecimal calculaCustoEntrega(double distanciaEmKm) {
        BigDecimal precoKm = BigDecimal.ONE;
        BigDecimal taxaRefrigeracacao = BigDecimal.valueOf(1.1);
        BigDecimal precoTotalDistancia = BigDecimal.valueOf(distanciaEmKm).multiply(precoKm);
        return precoTotalDistancia.multiply(taxaRefrigeracacao);
    }
    
}
