package com.ufcg.psoft.mercadofacil.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.ufcg.psoft.mercadofacil.exception.ErroCompra;
import com.ufcg.psoft.mercadofacil.model.CalculoDeDescontoPorTipoCliente;
import com.ufcg.psoft.mercadofacil.model.CalculoDeDescontoFactory;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Pagamento;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CompraRepository;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ClienteService;
import com.ufcg.psoft.mercadofacil.service.CompraService;
import com.ufcg.psoft.mercadofacil.service.LoteService;
import com.ufcg.psoft.mercadofacil.service.PagamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraServiceImpl implements CompraService {

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CarrinhoService carrinhoService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private LoteService loteService;

	@Override
	public Compra finalizaCompra(Long idCliente, String formaDePagamento) {
		Cliente cliente = clienteService.getClienteById(idCliente);
		Long idCarrinho = cliente.getCpf();
		Carrinho carrinho = carrinhoService.getCarrinhoById(idCarrinho);
		List<ItemCarrinho> itens = getItensDoCarrinho(carrinho);

		assertIsDisponivel(itens);
		assertTemEmEstoque(itens);

		int totalItens = carrinho.getTotalItens();
		CalculoDeDescontoPorTipoCliente calculoDesconto = CalculoDeDescontoFactory.create(cliente.getTipo());
		BigDecimal desconto = calculoDesconto.calculaDesconto(totalItens);
		BigDecimal totalCompra = carrinho.getTotal();
		Pagamento pagamento = pagamentoService.geraPagamento(totalCompra, formaDePagamento, desconto);

		loteService.retiraItensDoEstoque(itens);
		carrinhoService.removeTodosProdutos(carrinho);

		Compra compra = new Compra(cliente, itens, pagamento);
		salvaCompra(compra);

		return compra;
	}

	@Override
	public List<Compra> listaCompras(Long idCliente, String inicio, String fim) {
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

	private List<ItemCarrinho> getItensDoCarrinho(Carrinho carrinho) {
		if (carrinho.isEmpty()) {
			throw ErroCompra.erroCarrinhoVazio();
		}
		return carrinho.getItens();
	}

	private void assertIsDisponivel(List<ItemCarrinho> itens) {
		List<Produto> indisponiveis = itens.stream()
				.map(ItemCarrinho::getProduto)
				.filter(p -> !p.isDisponivel())
				.collect(Collectors.toList());

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
			throw ErroCompra.erroFormatoDeDataInvalido();
		}
	}

}
