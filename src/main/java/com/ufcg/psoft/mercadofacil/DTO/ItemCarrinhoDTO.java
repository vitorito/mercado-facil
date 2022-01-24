package com.ufcg.psoft.mercadofacil.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemCarrinhoDTO {
	
	@NotNull
	@Positive
	Long idProduto;

	@NotNull
	@Positive
	Integer numDeItens;

}
