package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.ItemDoCarrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ClienteService;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;
import com.ufcg.psoft.mercadofacil.util.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;
import com.ufcg.psoft.mercadofacil.util.ErroProduto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CarrinhoApiController {

	@Autowired
	CarrinhoService carrinhoService;

	@Autowired
	ClienteService clienteService;

	@Autowired
	ProdutoService produtoService;

	@RequestMapping(value = "/cliente/carrinho", method = RequestMethod.GET)
	public ResponseEntity<?> listaProdutosNoCarrinho(@RequestParam("id_cliente") long idCliente) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(idCliente);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEncontrado(idCliente);
		}

		Long idCarrinho = clienteOp.get().getCpf();
		Optional<Carrinho> carrinhoOp = carrinhoService.getCarrinhoById(idCarrinho);

		if (!carrinhoOp.isPresent()) {
			return ErroCarrinho.erroCarrinhoNaoEncontrado(idCliente);
		}

		List<ItemDoCarrinho> produtos = carrinhoOp.get().getProdutos();

		return new  ResponseEntity<List<ItemDoCarrinho>>(produtos, HttpStatus.OK);
	}

	@RequestMapping(value = "/cliente/carrinho", method = RequestMethod.PUT)
	public ResponseEntity<?> adicionaProdutoNoCarrinho(
			@RequestBody long idCliente,
			@RequestBody long idProduto,
			@RequestBody int numDeItens) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(idCliente);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEncontrado(idCliente);
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

	@RequestMapping(value = "/cliente/carrinho", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeProdutosDoCarrinho(
			@RequestBody long idCliente,
			@RequestBody long idProduto,
			@RequestBody int numDeItens) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(idCliente);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEncontrado(idCliente);
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
		Produto produto = produtoOp.get();
		
		if (!carrinhoService.containsProduto(carrinho, produto)) {
			return ErroCarrinho.erroCarrinhoNaoTemProduto(idProduto);
		}

		carrinhoService.removeProdutos(carrinho, produto, numDeItens);
		carrinhoService.salvaCarrinho(carrinho);

		return new ResponseEntity<Carrinho>(carrinho, HttpStatus.OK);
	}

}
