package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.Compra;

public interface CompraService {

	public List<Compra> listaCompras(Long idCliente);

	public Compra getCompraById(Long idCliente, Long idCompra);

	public Compra finalizaCompra(Long idCliente);

}
