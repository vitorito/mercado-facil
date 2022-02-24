package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public class CalculoDeDescontoClientePremium implements CalculoDeDescontoPorTipoCliente {

    @Override
    public BigDecimal calculaDesconto(int totalItens) {
        BigDecimal desconto = BigDecimal.valueOf(0.1);
        int minItens = 5;
        return totalItens >= minItens ? desconto : BigDecimal.ZERO;
    }
    
}
