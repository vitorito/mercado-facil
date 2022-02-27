package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class EntregaRetirada extends Entrega {

    public EntregaRetirada(String destino, int distanciaEmMetros) {
        super(destino, distanciaEmMetros, TipoEntrega.RETIRADA);
    }

    @Override
    public BigDecimal calculaCustoEntrega(CalculoDeEntregaPorTipoTransporte calculo) {
        return BigDecimal.ZERO;
    }
    
}
