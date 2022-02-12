package com.ufcg.psoft.mercadofacil.exception;

import com.ufcg.psoft.mercadofacil.model.TipoCliente;

import org.springframework.http.HttpStatus;

public class ErroCliente {

	public static final String CLIENTE_NAO_CADASTRADO_ID = "Não há cliente cadastrado com esse ID.";

	public static final String CLIENTE_NAO_CADASTRADO_CPF = "Não há cliente cadastrado com esse CPF.";

	public static final String CLIENTE_JA_CADASTRADO = "Cliente com esse CPF já cadastrado.";

	public static final String TIPO_CLIENTE_INVALIDO = "Tipo de cliente inválido.Tipos válidos: ";

	public static CustomErrorType erroClienteNaoEncontradoId() {
		return new CustomErrorType(CLIENTE_NAO_CADASTRADO_ID, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroClienteNaoEncontradoCPF() {
		return new CustomErrorType(CLIENTE_NAO_CADASTRADO_CPF, HttpStatus.NOT_FOUND);
	}

	public static CustomErrorType erroClienteJaCadastrado() {
		return new CustomErrorType(CLIENTE_JA_CADASTRADO, HttpStatus.CONFLICT);
	}

	public static CustomErrorType erroTipoInvalidoDeCliente() {
		String tiposValidos = TipoCliente.valuesToString();
		String message = TIPO_CLIENTE_INVALIDO + tiposValidos;
		
		return new CustomErrorType(message, HttpStatus.BAD_REQUEST);
	}

}
