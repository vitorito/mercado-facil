package com.ufcg.psoft.mercadofacil.model;

/** 
* Representa um item do qual houve um pedido de compra porém não
* tinha a quantidade requerida no estoque.
*/
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

	public ItemSemEstoque(Long idProduto, int requerido, int disponivel) {
		this.idProduto = idProduto;
		this.requerido = requerido;
		this.disponivel = disponivel;
	}

	public Long getProduto() {
		return idProduto;
	}

	public int getQntdRequerida() {
		return requerido;
	}

	public int getQntdDisponivel() {
		return disponivel;
	}

	@Override
	public String toString() {
		String result = idProduto + ", em estoque: " + disponivel + ", requerido: " + requerido;

		return result;
	}
	
}
