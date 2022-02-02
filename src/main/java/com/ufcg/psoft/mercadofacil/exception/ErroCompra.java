package com.ufcg.psoft.mercadofacil.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ufcg.psoft.mercadofacil.model.FormaDePagamento;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Produto;

import org.springframework.http.HttpStatus;

public class ErroCompra {

	public static final String ESTOQUE_INSUFICIENTE = "Produtos estão insuficientes no estoque.";

	public static final String PRODUTOS_INDISPONIVEIS = "Alguns produtos não estão disponiveis.";

	public static final String CARRINHO_VAZIO = "O carrinho desse cliente está vazio.";

	public static final String CLIENTE_NAO_POSSUI_COMPRA = "Esse cliente não possui essa compra.";

	public static final String FORMATO_DATA_INVALIDO = "Formato de data inválido."
			+ "O formato válido é yyyy-MM-dd.";

	public static final String FORMA_PAGAMENTO_INVALIDA = "Forma de pagamento inválida."
			+ "As formas válidas são: ";

	public static CustomErrorType erroEstoqueInsuficiente(List<ItemSemEstoque> itens) {
		var details = CustomErrorResponseDetails.builder()
				.titulo("Produtos que estão insuficientes.")
				.itens(itens)
				.build();

		return new CustomErrorType(ESTOQUE_INSUFICIENTE, HttpStatus.CONFLICT, details);
	}

	public static CustomErrorType erroCrompraProdutosIndisponiveis(
			List<Produto> produtosIndisponiveis) {
		List<Long> indisponiveisIds = produtosIndisponiveis.stream()
				.map(Produto::getId)
				.collect(Collectors.toList());

		var details = CustomErrorResponseDetails.builder()
				.titulo("IDs dos produtos indisponíveis.")
				.itens(indisponiveisIds)
				.build();

		return new CustomErrorType(PRODUTOS_INDISPONIVEIS, HttpStatus.CONFLICT, details);
	}

	public static CustomErrorType erroCarrinhoVazio() {
		return new CustomErrorType(CARRINHO_VAZIO, HttpStatus.CONFLICT);
	}

	public static CustomErrorType erroClienteNaoPossuiCompra() {
		return new CustomErrorType(CLIENTE_NAO_POSSUI_COMPRA, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroFormatoDeDataInvalido() {
		return new CustomErrorType(FORMATO_DATA_INVALIDO, HttpStatus.BAD_REQUEST);
	}

	public static CustomErrorType erroFormaDePagamentoInvalida() {
		String formasValidas = Arrays.toString(FormaDePagamento.values());
		String message = FORMA_PAGAMENTO_INVALIDA + formasValidas;

		return new CustomErrorType(message, HttpStatus.BAD_REQUEST);
	}

}
