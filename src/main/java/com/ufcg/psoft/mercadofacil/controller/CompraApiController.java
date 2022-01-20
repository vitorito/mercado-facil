package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.service.CompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente/{id}/compra")
@CrossOrigin
public class CompraApiController {

	private static final String MIN_DATA = "1970-01-01";

	private static final String MAX_DATA = "9999-12-31";

	@Autowired
	CompraService compraService;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public Compra finalizaCompra(@PathVariable("id") Long idCliente) {
		return compraService.finalizaCompra(idCliente);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Compra> listaCompras(
			@PathVariable("id") Long idCliente,
			@RequestParam(name = "do_dia") Optional<String> dia,
			@RequestParam(name = "depois_de", defaultValue = MIN_DATA) String inicio,
			@RequestParam(name = "antes_de", defaultValue = MAX_DATA) String fim) {
		// se um dia for passado, sao retornadas as compras feitas nesse dia
		if (dia.isPresent()) {
			inicio = dia.get();
			fim = dia.get();
		}
		return compraService.listaCompras(idCliente, inicio, fim);
	}

	@GetMapping("/{idCompra}")
	@ResponseStatus(HttpStatus.OK)
	public Compra getCompra(
			@PathVariable("id") Long idCliente,
			@PathVariable("idCompra") Long idCompra) {
		return compraService.getCompraById(idCliente, idCompra);
	}

}
