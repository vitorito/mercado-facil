package com.ufcg.psoft.mercadofacil.exception;

import org.springframework.http.HttpStatus;

public class ErroProduto {

	public static final String PRODUTO_NAO_CADASTRADO_ID = "Não há produto cadastrado com esse ID.";

	public static final String PRODUTO_NAO_CADASTRADO_CODIGO = "Não há produto cadastrado com esse código de barras.";

	public static final String PRODUTO_JA_CADASTRADO = "Esse produto já esta cadastrado.";

	public static final String PRODUTO_INDISPONIVEL = "Esse produto não está disponível";

	public static CustomErrorType erroProdutoNaoEncontradoId() {
		return new CustomErrorType(PRODUTO_NAO_CADASTRADO_ID, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroProdutoNaoEncontradoCodigo() {
		return new CustomErrorType(PRODUTO_NAO_CADASTRADO_CODIGO, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroProdutoJaCadastrado() {
		return new CustomErrorType(PRODUTO_JA_CADASTRADO, HttpStatus.CONFLICT);
	}

	public static CustomErrorType erroProdutoIndisponivel() {
		return new CustomErrorType(PRODUTO_INDISPONIVEL, HttpStatus.CONFLICT);
	}

}
