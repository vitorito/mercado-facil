package com.ufcg.psoft.mercadofacil.DTO;

public class ItemCarrinhoDTO {
	
	long idProduto;

	int numDeItens;

	public ItemCarrinhoDTO(Long idProduto, int numDeItens) {
		this.idProduto = idProduto;
		this.numDeItens = numDeItens;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public int getNumDeItens() {
		return numDeItens;
	}

	public void setNumDeItens(int numDeItens) {
		this.numDeItens = numDeItens;
	}

}
