package com.ufcg.psoft.mercadofacil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ClienteService;
import com.ufcg.psoft.mercadofacil.service.LoteService;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;
import com.ufcg.psoft.mercadofacil.util.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;
import com.ufcg.psoft.mercadofacil.util.ErroProduto;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CarrinhoApiController {

	@Autowired
	CarrinhoService carrinhoService;

	@Autowired
	ClienteService clienteService;

	@Autowired
	LoteService loteService;

	@Autowired
	ProdutoService produtoService;

	@RequestMapping(value = "/cliente/{idCliente}/carrinho", method = RequestMethod.PUT)
	public ResponseEntity<?> adicionaProdutoNoCarrinho(
			@PathVariable("idCliente") long idCliente,
			@RequestBody long idProduto,
			@RequestBody int numDeItens) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(idCliente);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEnconrtrado(idCliente);
		}
		
		Optional<Produto> produtoOp = produtoService.getProdutoById(idProduto);

		if (!produtoOp.isPresent()) {
			return ErroProduto.erroProdutoNaoEnconrtrado(idProduto);
		}

		long idCarrinho = clienteOp.get().getCpf();
		Optional<Carrinho> carrinhoOp = carrinhoService.getCarrinhoById(idCarrinho);

		if (!carrinhoOp.isPresent()) {
			return ErroCarrinho.erroCarrinhoNaoEncontrado(idCliente);
		}

		Carrinho carrinho = carrinhoOp.get();
		carrinhoService.adicionaProdutos(carrinho, produtoOp.get(), numDeItens);
		carrinhoService.salvaCarrinho(carrinho);

		return new ResponseEntity<Carrinho>(carrinho, HttpStatus.OK);
	}

}
