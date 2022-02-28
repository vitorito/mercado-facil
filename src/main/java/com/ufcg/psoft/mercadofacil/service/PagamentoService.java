package com.ufcg.psoft.mercadofacil.service;

import java.math.BigDecimal;

import com.ufcg.psoft.mercadofacil.model.FormaDePagamento;
import com.ufcg.psoft.mercadofacil.model.Pagamento;

public interface PagamentoService {

	FormaDePagamento[] listaFormasDePagamento();

	Pagamento geraPagamento(BigDecimal totalCompra, String formaDePagamento, BigDecimal desconto, BigDecimal custoEntrega); 
	
}
