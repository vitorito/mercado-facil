package com.ufcg.psoft.mercadofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.exception.ErroCompra;
import com.ufcg.psoft.mercadofacil.model.FormaDePagamento;
import com.ufcg.psoft.mercadofacil.model.Pagamento;
import com.ufcg.psoft.mercadofacil.repository.PagamentoRepository;
import com.ufcg.psoft.mercadofacil.service.impl.PagamentoServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

	@InjectMocks
	private PagamentoService pagamentoService = new PagamentoServiceImpl();

	@Mock
	private PagamentoRepository pagamentoRepository;

	@Test
	public void geraPagamentoRetornaPagamentoRealizado() {
		BigDecimal totalCompra = BigDecimal.valueOf(10);
		BigDecimal desconto = BigDecimal.valueOf(0.1);
		String formaDePagamento = "credito";

		Pagamento pagamento = pagamentoService.geraPagamento(
				totalCompra, formaDePagamento, desconto);

		assertEquals(totalCompra, pagamento.getTotalCompra());
		assertEquals(FormaDePagamento.CREDITO, pagamento.getFormaDePagamento());
		assertEquals(desconto, pagamento.getTaxaDesconto());
		assertEquals(1.0, pagamento.getValorDesconto().doubleValue());
		assertEquals(0.05, pagamento.getTaxaJuros().doubleValue());
		assertEquals(0.5, pagamento.getValorJuros().doubleValue());
		assertEquals(9.5, pagamento.getTotalPago().doubleValue());
		verify(pagamentoRepository, times(1)).save(pagamento);
	}

	@Test
	public void geraPagamentoComFormaDePagamentoInvalidaLancaErro() {
		BigDecimal totalCompra = BigDecimal.valueOf(10);
		BigDecimal desconto = BigDecimal.valueOf(0.1);
		String formaDePagamento = "FormaInvalida";

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> pagamentoService.geraPagamento(totalCompra, formaDePagamento, desconto));

		String errorMessage = ErroCompra.FORMA_PAGAMENTO_INVALIDA + FormaDePagamento.valuesToString();
		assertEquals(errorMessage, erro.getMessage());
		verify(pagamentoRepository, never()).save(any());
	}

}
