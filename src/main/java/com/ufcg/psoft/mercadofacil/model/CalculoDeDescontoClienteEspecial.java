package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public class CalculoDeDescontoClienteEspecial implements CalculoDeDescontoPorTipoCliente {

    @Override
    public BigDecimal calculaDesconto(int totalItens) {
        int minItens = 10;
        BigDecimal desconto = BigDecimal.valueOf(0.1);
        return totalItens >= minItens ? desconto : BigDecimal.ZERO;
    }
    
}
