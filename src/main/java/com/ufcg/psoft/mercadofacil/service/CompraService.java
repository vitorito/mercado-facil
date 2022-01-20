package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.Compra;

public interface CompraService {

	List<Compra> listaCompras(Long idCliente, String inicio, String fim);

	Compra getCompraById(Long idCliente, Long idCompra);

	Compra finalizaCompra(Long idCliente);

}
