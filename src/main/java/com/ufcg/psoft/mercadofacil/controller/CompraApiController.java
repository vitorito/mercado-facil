package com.ufcg.psoft.mercadofacil.controller;

import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ClienteService;
import com.ufcg.psoft.mercadofacil.service.CompraService;
import com.ufcg.psoft.mercadofacil.util.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CompraApiController {
	
	@Autowired
	ClienteService clienteService;

	@Autowired
	CarrinhoService carrinhoService;

	@Autowired
	CompraService compraService;

	@RequestMapping(value = "/cliente/compra", method = RequestMethod.POST)
	public ResponseEntity<?> finalizaCompra(@RequestBody long idCliente) {
		
		Optional<Cliente> clienteOp = clienteService.getClienteById(idCliente);
		
		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEncontrado(idCliente);
		}

		Optional<Carrinho> carrinhoOp = carrinhoService.getCarrinhoById(idCliente);
		
		if (!carrinhoOp.isPresent()) {
			return ErroCarrinho.erroCarrinhoNaoEncontrado(idCliente);
		}
		
		Carrinho carrinho = carrinhoOp.get();
		Compra compra = compraService.finalizaCompra(clienteOp.get(), carrinho);
		compraService.salvaCompra(compra);
		carrinhoService.removeTodosProdutos(carrinho);

		return new ResponseEntity<Compra>(compra, HttpStatus.OK);
	}
}
