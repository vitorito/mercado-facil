package com.ufcg.psoft.mercadofacil.DTO;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class ProdutoDTO {

	private String nome;

	private BigDecimal preco;

	private String codigoBarra;

	private String fabricante;
	
	private String categoria;

}
