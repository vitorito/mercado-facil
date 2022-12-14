package com.ufcg.psoft.mercadofacil.DTO;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProdutoDTO {

	@NotEmpty
	private String nome;

	@NotNull
	@PositiveOrZero
	private BigDecimal preco;

	@NotEmpty
	private String codigoBarra;

	@NotEmpty
	private String fabricante;
	
	@NotEmpty
	private String categoria;
	
	@NotEmpty
	private String tipoTransporte;

}
