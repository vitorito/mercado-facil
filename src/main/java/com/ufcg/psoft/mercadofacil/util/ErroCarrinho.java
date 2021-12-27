package com.ufcg.psoft.mercadofacil.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErroCarrinho {
	
	static final String NAO_HA_CARRINHO = "Não há carrinho cadastrado para o cliente %s.";

	static final String CARRINHO_JA_CADASTRADO = "Carrinho do cliente %s já existe.";

	static final String CARRINHO_NAO_TEM_PRODUTO = "Carrinho não tem o produto %s.";

	public static ResponseEntity<CustomErrorType> erroCarrinhoNaoEncontrado(long idCliente) {
		return new ResponseEntity<CustomErrorType>(new CustomErrorType(String.format(ErroCarrinho.NAO_HA_CARRINHO, idCliente)),
				HttpStatus.NOT_FOUND);
	}

	public static ResponseEntity<CustomErrorType> erroCarrinhoJaCadastrado(long idCliente) {
		return new ResponseEntity<CustomErrorType>(new CustomErrorType(String.format(ErroCarrinho.CARRINHO_JA_CADASTRADO, idCliente)),
				HttpStatus.CONFLICT);
	}

	public static ResponseEntity<?> erroCarrinhoNaoTemProduto(long idProduto) {
		return new ResponseEntity<CustomErrorType>(
			new CustomErrorType(String.format(ErroCarrinho.CARRINHO_NAO_TEM_PRODUTO, idProduto)),
			HttpStatus.NOT_FOUND);
	}

}
