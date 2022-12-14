package com.ufcg.psoft.mercadofacil.controller;

import javax.validation.Valid;

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
	public Carrinho getCarrinho(@PathVariable("id") Long idCliente) {
		Long idCarrinho = getIdCarrinho(idCliente);
		return carrinhoService.getCarrinhoById(idCarrinho);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Carrinho adicionaProduto(
			@PathVariable("id") Long idCliente,
			@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		Long idCarrinho = getIdCarrinho(idCliente);
		return carrinhoService.adicionaProdutos(idCarrinho, itemCarrinhoDTO);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public Carrinho removeProdutos(
			@PathVariable("id") Long idCliente,
			@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		Long idCarrinho = getIdCarrinho(idCliente);
		return carrinhoService.removeProduto(idCarrinho, itemCarrinhoDTO);
	}

	private Long getIdCarrinho(Long idCliente) {
		return clienteService.getClienteById(idCliente).getCpf();
	}

}
