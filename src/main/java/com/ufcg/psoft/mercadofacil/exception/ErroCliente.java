package com.ufcg.psoft.mercadofacil.exception;

import org.springframework.http.HttpStatus;

public class ErroCliente {

	public static final String CLIENTE_NAO_CADASTRADO_ID = "Não há cliente cadastrado com esse ID.";

	public static final String CLIENTE_NAO_CADASTRADO_CPF = "Não há cliente cadastrado com esse CPF.";

	public static final String CLIENTES_NAO_CADASTRADOS = "Não há clientes cadastrados.";

	public static final String CLIENTE_JA_CADASTRADO = "Cliente com esse CPF já cadastrado.";

	public static CustomErrorType erroClienteNaoEncontradoId() {
		return new CustomErrorType(CLIENTE_NAO_CADASTRADO_ID, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroClienteNaoEncontradoCPF() {
		return new CustomErrorType(CLIENTE_NAO_CADASTRADO_CPF, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroSemClientesCadastrados() {
		return new CustomErrorType(CLIENTES_NAO_CADASTRADOS, HttpStatus.NO_CONTENT);
	}

	public static CustomErrorType erroClienteJaCadastrado() {
		return new CustomErrorType(CLIENTE_JA_CADASTRADO,HttpStatus.CONFLICT);
	}
	
}
