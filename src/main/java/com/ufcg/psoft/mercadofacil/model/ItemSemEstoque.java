package com.ufcg.psoft.mercadofacil.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/** 
* Representa um item do qual houve um pedido de compra porém não
* tinha a quantidade requerida no estoque.
*/
@Getter
@ToString
@AllArgsConstructor
public class ItemSemEstoque {
	
	Long idProduto;

	/**
	 * Quantidade de itens requeridos na compra.
	 */
	int requerido;

	/**
	 * Quantidade de itens disponíveis no estoque.
	 */
	int disponivel; 
	
}
