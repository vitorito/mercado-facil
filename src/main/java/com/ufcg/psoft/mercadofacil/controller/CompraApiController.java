package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.service.CompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente/{id}/compra")
@CrossOrigin
public class CompraApiController {

	@Autowired
	CompraService compraService;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public Compra finalizaCompra(@PathVariable("id") Long idCliente) {
		return compraService.finalizaCompra(idCliente);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Compra> listaCompras(@PathVariable("id") long idCliente) {
		return compraService.listaCompras(idCliente);
	}

	@GetMapping("/{idCompra}")
	@ResponseStatus(HttpStatus.OK)
	public Compra getCompra(
			@PathVariable("id") long idCliente, 
			@PathVariable("idCompra") long idCompra) {
		return compraService.getCompraById(idCliente, idCompra);
	}

}
