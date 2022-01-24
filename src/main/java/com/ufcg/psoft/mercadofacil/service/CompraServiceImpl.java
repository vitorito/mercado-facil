package com.ufcg.psoft.mercadofacil.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.exception.ErroCompra;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CompraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		carrinhoService.removeTodosProdutos(idCarrinho);

		Compra compra = new Compra(cliente, produtos, total);
		salvaCompra(compra);

		return compra;
	}

	@Override
	public List<Compra> listaCompras(
			Long idCliente, String inicio, String fim) {
		Cliente cliente = clienteService.getClienteById(idCliente);

		LocalDateTime inicioPeriodo = converteAoInicioDoDia(inicio);
		LocalDateTime fimPeriodo = converteAoFimDoDia(fim);

		return compraRepository.findByClienteAndDataBetween(cliente, inicioPeriodo, fimPeriodo);
	}

	@Override
	public Compra getCompraById(Long idCliente, Long idCompra) {
		clienteService.assertExisteClienteById(idCliente);
		return compraRepository.findById(idCompra).orElseThrow(
				() -> ErroCompra.erroClienteNaoPossuiCompra());
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
				carrinhoService.listaProdutosDoCarrinho(idCarrinho));

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

	private LocalDateTime converteAoInicioDoDia(String dataStr) {
		LocalDate data = toLocalDate(dataStr);

		return data.atStartOfDay();
	}

	private LocalDateTime converteAoFimDoDia(String dataStr) {
		LocalDate data = toLocalDate(dataStr);

		return data.atTime(23, 59, 59, 999999999);
	}

	private LocalDate toLocalDate(String dataStr) {
		try {
			return LocalDate.parse(dataStr);
		} catch (DateTimeParseException ex) {
			throw new CustomErrorType("Formato de data inválido.", HttpStatus.BAD_REQUEST);
		}
	}

}
