package com.ufcg.psoft.mercadofacil.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Produto;

import org.springframework.http.HttpStatus;

@SuppressWarnings("unused")
public class ErroCompra {

	public static final String ESTOQUE_INSUFICIENTE = "Produtos estão insuficientes no estoque.";

	public static final String PRODUTOS_INDISPONIVEIS = "Alguns produtos não estão disponiveis.";

	public static final String CARRINHO_VAZIO = "O carrinho desse cliente está vazio.";

	public static final String CLIENTE_NAO_POSSUI_COMPRA = "Esse cliente não possui essa compra.";

	public static final String CLIENTE_NAO_POSSUI_NENHUMA_COMPRA = "Esse cliente não possui nenhuma compra.";

	public static CustomErrorType erroEstoqueInsuficiente(List<ItemSemEstoque> itens) {
		return new CustomErrorType(ESTOQUE_INSUFICIENTE, HttpStatus.CONFLICT, itens);
	}

	public static CustomErrorType erroCrompraProdutosIndisponiveis(
			List<Produto> produtosIndisponiveis) {
		List<Long> indisponiveisIds = produtosIndisponiveis.stream()
				.map(Produto::getId)
				.collect(Collectors.toList());

		return new CustomErrorType(PRODUTOS_INDISPONIVEIS, HttpStatus.CONFLICT, indisponiveisIds);
	}

	public static CustomErrorType erroCarrinhoVazio() {
		return new CustomErrorType(CARRINHO_VAZIO, HttpStatus.CONFLICT);
	}

	public static CustomErrorType erroClienteNaoPossuiCompra() {
		return new CustomErrorType(CLIENTE_NAO_POSSUI_COMPRA, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroClienteNaoPossuiCompras() {
		return new CustomErrorType(CLIENTE_NAO_POSSUI_NENHUMA_COMPRA, HttpStatus.NO_CONTENT);
	}
}
