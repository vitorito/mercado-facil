package com.ufcg.psoft.mercadofacil.exception;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomErrorResponseDetails {
	
	private String titulo;

	private List<?> itens;
	
}
