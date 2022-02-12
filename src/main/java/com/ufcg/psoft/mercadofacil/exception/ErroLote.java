package com.ufcg.psoft.mercadofacil.exception;

import org.springframework.http.HttpStatus;

public class ErroLote {

	public static final String SEM_LOTES_CADASTRADOS = "Não há lotes cadastrados";
	
	public static CustomErrorType erroSemLotesCadastrados() {		
		return new CustomErrorType(SEM_LOTES_CADASTRADOS, HttpStatus.NO_CONTENT);
	}
}

