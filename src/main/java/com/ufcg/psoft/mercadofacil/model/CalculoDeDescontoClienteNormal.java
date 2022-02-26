package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public class CalculoDeDescontoClienteNormal implements CalculoDeDescontoPorTipoCliente {

    @Override
    public BigDecimal calculaDesconto(int totalItens) {
        return BigDecimal.ZERO;
    }
    
}
