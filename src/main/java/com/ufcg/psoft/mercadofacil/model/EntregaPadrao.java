package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class EntregaPadrao extends Entrega {

    public EntregaPadrao(String destino, double distanciaEmKm) {
        super(destino, distanciaEmKm, TipoEntrega.PADRAO);
    }

    @Override
    public BigDecimal calculaCustoEntrega(CalculoDeEntregaPorTipoTransporte calculoCustoTransporte) {
        BigDecimal valorBase = BigDecimal.valueOf(5);
        BigDecimal custoTransporte = calculoCustoTransporte.calculaCustoEntrega(getDistanciaEmKm());
        return valorBase.max(custoTransporte);
    }
    
}
