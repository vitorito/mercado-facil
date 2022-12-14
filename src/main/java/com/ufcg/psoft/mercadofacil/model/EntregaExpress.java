package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class EntregaExpress extends Entrega {

    public EntregaExpress(String destino, double distanciaEmKm) {
        super(destino, distanciaEmKm, TipoEntrega.EXPRESS);
    }

    @Override
    public BigDecimal calculaCustoEntrega(CalculoDeEntregaPorTipoTransporte calculoCustoTransporte) {
        BigDecimal valorBase = BigDecimal.TEN;
        BigDecimal custoTransporte = calculoCustoTransporte.calculaCustoEntrega(getDistanciaEmKm());
        BigDecimal taxaExpress = BigDecimal.valueOf(1.2);
        BigDecimal valorTotal = custoTransporte.multiply(taxaExpress);
        return valorTotal.max(valorBase);    
    }

}
