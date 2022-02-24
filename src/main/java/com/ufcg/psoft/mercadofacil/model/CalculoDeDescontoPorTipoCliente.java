package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

public interface CalculoDeDescontoPorTipoCliente {
    
	BigDecimal calculaDesconto(int totalItens);
    
}
