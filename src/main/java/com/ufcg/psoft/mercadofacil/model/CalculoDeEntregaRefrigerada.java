package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public class CalculoDeEntregaRefrigerada implements CalculoDeEntregaPorTipoTransporte {

    @Override
    public BigDecimal calculaCustoEntrega(double distanciaEmKm) {
        BigDecimal precoBase = BigDecimal.TEN;
        BigDecimal precoKm = BigDecimal.ONE;
        BigDecimal taxaRefrigeracacao = BigDecimal.valueOf(1.2);
        BigDecimal precoTotalDistancia = BigDecimal.valueOf(distanciaEmKm).multiply(precoKm);
        return precoTotalDistancia.multiply(taxaRefrigeracacao).add(precoBase);
    }
    
}
