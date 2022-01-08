package com.ufcg.psoft.mercadofacil.exception;

import org.springframework.http.HttpStatus;

public class ErroCarrinho {
	
	public static final String NAO_HA_CARRINHO = "Não há carrinho cadastrado para esse cliente.";

	public static final String CARRINHO_JA_CADASTRADO = "Carrinho desse cliente já existente.";

	public static final String CARRINHO_NAO_TEM_PRODUTO = "Carrinho desse cliente não tem esse produto.";

	public static CustomErrorType erroCarrinhoNaoEncontrado() {
		return new CustomErrorType(NAO_HA_CARRINHO, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroCarrinhoJaCadastrado() {
		return new CustomErrorType(CARRINHO_JA_CADASTRADO, HttpStatus.CONFLICT);
	}

	public static CustomErrorType erroCarrinhoNaoTemProduto() {
		return new CustomErrorType(CARRINHO_NAO_TEM_PRODUTO, HttpStatus.NOT_FOUND);
	}

}
