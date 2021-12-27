package com.ufcg.psoft.mercadofacil.util;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.ItemInsuficienteNoEstoque;
import com.ufcg.psoft.mercadofacil.model.Produto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErroCompra {

	static final String ESTOQUE_INSUFICIENTE = "Produtos insuficientes no estoque: %s";

	static final String PRODUTOS_INDISPONIVEIS = "Os seguintes produtos estão indisponiveis: %s";

	static final String CLIENTE_NAO_POSSUI_COMPRA = "O cliente %s não possui a compra de id %s";

	public static ResponseEntity<CustomErrorType> erroCompraEstoqueInsuficiente(
			List<ItemInsuficienteNoEstoque> itens) {

		String produtosInsuficientes = "";

		for (ItemInsuficienteNoEstoque item : itens) {
			produtosInsuficientes += item + "; ";
		}

		return new ResponseEntity<CustomErrorType>(
				new CustomErrorType(String.format(ErroCompra.ESTOQUE_INSUFICIENTE, produtosInsuficientes)),
				HttpStatus.CONFLICT);
	}

	public static ResponseEntity<?> erroCrompraProdutosIndisponiveis(
			List<Produto> produtosIndisponiveis) {

		String indisponiveis = "";

		for (Produto produto : produtosIndisponiveis) {
			indisponiveis += produto + "; ";
		}

		return new ResponseEntity<CustomErrorType>(
				new CustomErrorType(String.format(ErroCompra.PRODUTOS_INDISPONIVEIS, indisponiveis)),
				HttpStatus.CONFLICT);
	}

	public static ResponseEntity<?> erroClienteNaoPossuiCompra(long idCliente, long idCompra) {
		return new ResponseEntity<CustomErrorType>(new CustomErrorType(String.format(
				ErroCompra.CLIENTE_NAO_POSSUI_COMPRA, idCliente, idCompra)), HttpStatus.CONFLICT);
	}
}
