package com.ufcg.psoft.mercadofacil.model;

/** 
* Representa um item do qual houve um pedido de compra porém não
* tinha a quantidade requerida no estoque.
*/
public class ItemInsuficienteNoEstoque {
	
	Produto produto;

	/**
	 * Quantidade de itens requeridos na compra.
	 */
	int qntdRequerida;

	/**
	 * Quantidade de itens disponíveis no estoque.
	 */
	int emEstoque; 

	public ItemInsuficienteNoEstoque(Produto produto, int requerido, int disponivel) {
		this.produto = produto;
		this.qntdRequerida = requerido;
		this.emEstoque = disponivel;
	}

	public Produto getProduto() {
		return produto;
	}

	public int getQntdRequerida() {
		return qntdRequerida;
	}

	public int getQntdDisponivel() {
		return emEstoque;
	}

	@Override
	public String toString() {
		String result = produto + ", em estoque: " + emEstoque + ", requerido: " + qntdRequerida;

		return result;
	}
	
}
