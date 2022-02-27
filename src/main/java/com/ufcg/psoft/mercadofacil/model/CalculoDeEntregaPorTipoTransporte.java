package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public interface CalculoDeEntregaPorTipoTransporte {

    BigDecimal calculaCustoEntrega(double distanciaEmKm);
}
