package com.ufcg.psoft.mercadofacil.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErroCarrinho {
	
	static final String NAO_HA_CARRINHO = "Não há carrinho cadastrado para o cliente %s.";

	static final String CARRINHO_JA_CADASTRADO = "Carrinho do cliente %s já existe.";

	public static ResponseEntity<CustomErrorType> erroCarrinhoNaoEncontrado(long id) {
		return new ResponseEntity<CustomErrorType>(new CustomErrorType(String.format(ErroCarrinho.NAO_HA_CARRINHO, id)),
				HttpStatus.NOT_FOUND);
	}

	public static ResponseEntity<CustomErrorType> erroCarrinhoJaCadastrado(long id) {
		return new ResponseEntity<CustomErrorType>(new CustomErrorType(String.format(ErroCarrinho.CARRINHO_JA_CADASTRADO, id)),
				HttpStatus.CONFLICT);
	}

}
