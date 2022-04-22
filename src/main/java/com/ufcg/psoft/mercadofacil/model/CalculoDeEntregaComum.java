package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public class CalculoDeEntregaComum implements CalculoDeEntregaPorTipoTransporte {

    @Override
    public BigDecimal calculaCustoEntrega(double distanciaEmKm) {
        BigDecimal precoKm = BigDecimal.ONE;
        return BigDecimal.valueOf(distanciaEmKm).multiply(precoKm);
    }
    
}
