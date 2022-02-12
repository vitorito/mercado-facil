package com.ufcg.psoft.mercadofacil.service.impl;

import java.math.BigDecimal;

import com.ufcg.psoft.mercadofacil.exception.ErroCompra;
import com.ufcg.psoft.mercadofacil.model.FormaDePagamento;
import com.ufcg.psoft.mercadofacil.model.Pagamento;
import com.ufcg.psoft.mercadofacil.repository.PagamentoRepository;
import com.ufcg.psoft.mercadofacil.service.PagamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoServiceImpl implements PagamentoService {

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Override
	public FormaDePagamento[] listaFormasDePagamento() {
		return FormaDePagamento.values();
	}

	@Override
	public Pagamento geraPagamento(BigDecimal totalCompra, String formaPagamento, BigDecimal desconto) {
		FormaDePagamento formaDePagamento = getFormaDePagamento(formaPagamento);
	
		Pagamento pagamento = Pagamento.builder()
				.totalCompra(totalCompra)
				.formaDePagamento(formaDePagamento)
				.taxaDesconto(desconto)
				.build();

		salvaPagamento(pagamento);

		return pagamento;
	}

	private void salvaPagamento(Pagamento pagamento) {
		this.pagamentoRepository.save(pagamento);
	}

	private FormaDePagamento getFormaDePagamento(String formaDePagamento) {
		try {
			return FormaDePagamento.valueOf(formaDePagamento.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw ErroCompra.erroFormaDePagamentoInvalida();
		}
	}

}
