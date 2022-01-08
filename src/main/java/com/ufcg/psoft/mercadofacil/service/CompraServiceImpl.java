package com.ufcg.psoft.mercadofacil.service;

import java.math.BigDecimal;
import java.util.List;

import com.ufcg.psoft.mercadofacil.exception.ErroCompra;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CompraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraServiceImpl implements CompraService {

	@Autowired
	CompraRepository compraRepository;

	@Autowired
	ClienteService clienteService;

	@Autowired
	CarrinhoService carrinhoService;

	@Autowired
	ProdutoService produtoService;

	@Autowired
	LoteService loteService;

	@Override
	public Compra finalizaCompra(Long idCliente) {
		Cliente cliente = clienteService.getClienteById(idCliente);
		Long idCarrinho = cliente.getCpf();
		List<ItemCarrinho> produtos = getItensDoCarrinho(idCarrinho);

		assertIsDisponivel(idCarrinho);
		assertTemEmEstoque(produtos);

		BigDecimal total = carrinhoService.calculaTotal(idCarrinho);
		
		loteService.retiraItensDoEstoque(produtos);
		carrinhoService.removeTodosProdutos(idCliente);

		Compra compra = new Compra(cliente, produtos, total);
		salvaCompra(compra);
		
		return compra;
	}
	
	@Override
	public List<Compra> listaCompras(Long idCliente) {
		Cliente cliente = clienteService.getClienteById(idCliente);
		List<Compra> compras = compraRepository.findByCliente(cliente);

		if (compras.isEmpty()) {
			throw ErroCompra.erroClienteNaoPossuiCompras();
		}

		return compras;
	}
	
	@Override
	public Compra getCompraById(Long idCliente, Long idCompra) {
		clienteService.assertExisteClienteById(idCliente);
		return compraRepository.findById(idCompra).orElseThrow(
			() -> ErroCompra.erroClienteNaoPossuiCompra()
		);
	}

	private void salvaCompra(Compra compra) {
		compraRepository.save(compra);
	}

	private List<ItemCarrinho> getItensDoCarrinho(Long idCarrinho) {
		List<ItemCarrinho> produtos = carrinhoService.listaItensDoCarrinho(idCarrinho);

		if (produtos.isEmpty()) {
			throw ErroCompra.erroCarrinhoVazio();
		}
		return produtos;
	}

	private void assertIsDisponivel(Long idCarrinho) {
		List<Produto> indisponiveis = produtoService.checaDisponibilidade(
			carrinhoService.getProdutosDoCarrinho(idCarrinho)
		);

		if (!indisponiveis.isEmpty()) {
			throw ErroCompra.erroCrompraProdutosIndisponiveis(indisponiveis);
		}
	}
	
	private void assertTemEmEstoque(List<ItemCarrinho> produtos) {
		List<ItemSemEstoque> insuficientes = loteService.temEmEstoque(produtos);

		if (!insuficientes.isEmpty()) {
			throw ErroCompra.erroEstoqueInsuficiente(insuficientes);
		}
	}

}
