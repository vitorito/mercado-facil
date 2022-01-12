package com.ufcg.psoft.mercadofacil.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;

@Getter
public class ItemCarrinhoDTO {
	
	@NotNull
	@Positive
	Long idProduto;

	@NotNull
	@Positive
	Integer numDeItens;

}
