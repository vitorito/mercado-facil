package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class EntregaRetirada extends Entrega {

    public EntregaRetirada(String destino, double distanciaEmMetros) {
        super(destino, distanciaEmMetros, TipoEntrega.RETIRADA);
    }

    @Override
    public BigDecimal calculaCustoEntrega(CalculoDeEntregaPorTipoTransporte calculo) {
        return BigDecimal.ZERO;
    }
    
}
