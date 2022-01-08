package com.ufcg.psoft.mercadofacil.controller;

import com.ufcg.psoft.mercadofacil.DTO.ItemCarrinhoDTO;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente/{id}/carrinho")
@CrossOrigin
public class CarrinhoApiController {

	@Autowired
	CarrinhoService carrinhoService;

	@Autowired
	ClienteService clienteService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Carrinho getCarrinho(@PathVariable("id") long idCliente) {
		return carrinhoService.getCarrinhoByCliente(idCliente);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Carrinho adicionaProduto(
			@PathVariable("id") long idCliente,
			@RequestBody ItemCarrinhoDTO itemDoCarrinhoDTO) {
		return carrinhoService.adicionaProdutos(idCliente, itemDoCarrinhoDTO);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public Carrinho removeProdutos(
			@PathVariable("id") long idCliente,
			@RequestBody ItemCarrinhoDTO itemDoCarrinhoDTO) {
		return carrinhoService.removeProduto(idCliente, itemDoCarrinhoDTO);
	}

}
