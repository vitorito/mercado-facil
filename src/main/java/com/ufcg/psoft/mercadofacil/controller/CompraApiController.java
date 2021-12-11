package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.ItemDoCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemInsuficienteNoEstoque;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ClienteService;
import com.ufcg.psoft.mercadofacil.service.CompraService;
import com.ufcg.psoft.mercadofacil.service.LoteService;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;
import com.ufcg.psoft.mercadofacil.util.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;
import com.ufcg.psoft.mercadofacil.util.ErroCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Autowired
	LoteService loteService;

	@Autowired
	ProdutoService produtoService;

	@RequestMapping(value = "/cliente/{id}/compra", method = RequestMethod.POST)
	public ResponseEntity<?> finalizaCompra(@PathVariable("id") long idCliente) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(idCliente);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEncontrado(idCliente);
		}

		long idCarrinho = clienteOp.get().getCpf();
		Optional<Carrinho> carrinhoOp = carrinhoService.getCarrinhoById(idCarrinho);

		if (!carrinhoOp.isPresent()) {
			return ErroCarrinho.erroCarrinhoNaoEncontrado(idCliente);
		}

		Carrinho carrinho = carrinhoOp.get();
		List<ItemDoCarrinho> produtos = carrinho.getProdutos();

		List<Produto> produtosIndisponiveis = produtoService.checaDisponibilidade(produtos);

		if (!produtosIndisponiveis.isEmpty()) {
			return ErroCompra.erroCrompraProdutosIndisponiveis(produtosIndisponiveis);
		}

		List<ItemInsuficienteNoEstoque> itensInsuficientes = loteService.temNoEstoque(produtos);

		if (!itensInsuficientes.isEmpty()) {
			return ErroCompra.erroCompraEstoqueInsuficiente(itensInsuficientes);
		}

		Compra compra = compraService.criaCompra(clienteOp.get(), carrinho);
		compraService.salvaCompra(compra);
		loteService.retiraItensDoEstoque(produtos);
		carrinhoService.removeTodosProdutos(carrinho);

		return new ResponseEntity<Compra>(compra, HttpStatus.OK);
	}

	
}
