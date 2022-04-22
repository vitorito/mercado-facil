package com.ufcg.psoft.mercadofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.DTO.CompraDTO;
import com.ufcg.psoft.mercadofacil.DTO.EntregaDTO;
import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.exception.ErroCompra;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.Entrega;
import com.ufcg.psoft.mercadofacil.model.EntregaPadrao;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Pagamento;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.model.TipoCliente;
import com.ufcg.psoft.mercadofacil.repository.CompraRepository;
import com.ufcg.psoft.mercadofacil.service.impl.CompraServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {

	@InjectMocks
	private CompraService compraService = new CompraServiceImpl();

	@Mock
	private CompraRepository compraRepository;

	@Mock
	private ClienteService clienteService;

	@Mock
	private CarrinhoService carrinhoService;

	@Mock
	private PagamentoService pagamentoService;

	@Mock
	private LoteService loteService;

	@Mock
	private EntregaService entregaService;

	@Mock
	private Carrinho carrinhoMock;

	@Mock
	private Cliente clienteMock;

	@Mock
	private Produto produtoMock;

	@Mock
	private Pagamento pagamentoMock;

	private Long idCliente = 1L;

	private Long clienteCpf = 12345678910L;

	@Test
	public void finalizaCompraBoletoRetornaCompra() {
		List<ItemCarrinho> itens = List.of(new ItemCarrinho(produtoMock, 2));
        EntregaDTO entregaDto = new EntregaDTO("padrao", 5.3, "Paraiba");
        CompraDTO compraDto = new CompraDTO("boleto", entregaDto);
        BigDecimal totalCompra = BigDecimal.TEN;
        Entrega entregaMock = mock(EntregaPadrao.class);
        
		setGetClienteById();
		when(clienteMock.getCpf()).thenReturn(clienteCpf);
		when(carrinhoService.getCarrinhoById(clienteCpf)).thenReturn(carrinhoMock);
		when(carrinhoMock.isEmpty()).thenReturn(false);
		when(carrinhoMock.getItens()).thenReturn(itens);
		when(produtoMock.isDisponivel()).thenReturn(true);
		when(loteService.temEmEstoque(itens)).thenReturn(List.of());
		when(carrinhoMock.getTotalItens()).thenReturn(2);
		when(clienteMock.getTipo()).thenReturn(TipoCliente.NORMAL);
		when(carrinhoMock.getTotal()).thenReturn(totalCompra);
        when(entregaService.geraEntrega(any())).thenReturn(entregaMock);
		when(pagamentoService.geraPagamento(any(), anyString(), any(), any()))
						.thenReturn(pagamentoMock);

		Compra compra = compraService.finalizaCompra(idCliente, compraDto);

		assertEquals(clienteMock, compra.getCliente());
		assertEquals(itens, compra.getProdutos());
		assertEquals(pagamentoMock, compra.getPagamento());

		verify(loteService).retiraItensDoEstoque(itens);
		verify(carrinhoService).removeTodosProdutos(carrinhoMock);
		verify(compraRepository).save(compra);
	}

	@Test
	public void finalizaCompraCreditoRetornaCompra() {
		List<ItemCarrinho> itens = List.of(new ItemCarrinho(produtoMock, 4));
		BigDecimal totalCompra = BigDecimal.valueOf(20);
        EntregaDTO entregaDto = new EntregaDTO("padrao", 5.3, "Paraiba");
        CompraDTO compraDto = new CompraDTO("credito", entregaDto);
        Entrega entregaMock = mock(EntregaPadrao.class);

		setGetClienteById();
		when(clienteMock.getCpf()).thenReturn(clienteCpf);
		when(carrinhoService.getCarrinhoById(clienteCpf)).thenReturn(carrinhoMock);
		when(carrinhoMock.isEmpty()).thenReturn(false);
		when(carrinhoMock.getItens()).thenReturn(itens);
		when(produtoMock.isDisponivel()).thenReturn(true);
		when(loteService.temEmEstoque(itens)).thenReturn(List.of());
		when(carrinhoMock.getTotalItens()).thenReturn(4);
		when(clienteMock.getTipo()).thenReturn(TipoCliente.NORMAL);
		when(carrinhoMock.getTotal()).thenReturn(totalCompra);
		when(entregaService.geraEntrega(any())).thenReturn(entregaMock);
		when(pagamentoService.geraPagamento(any(), anyString(), any(), any()))
						.thenReturn(pagamentoMock);

		Compra compra = compraService.finalizaCompra(idCliente, compraDto);

		assertEquals(clienteMock, compra.getCliente());
		assertEquals(itens, compra.getProdutos());
		assertEquals(pagamentoMock, compra.getPagamento());

		verify(loteService).retiraItensDoEstoque(itens);
		verify(carrinhoService).removeTodosProdutos(carrinhoMock);
		verify(compraRepository).save(compra);
	}

	@Test
	public void finalizaCompraPaypalRetornaCompra() {
		List<ItemCarrinho> itens = List.of(new ItemCarrinho(produtoMock, 6));
		BigDecimal totalCompra = BigDecimal.valueOf(30);
        EntregaDTO entregaDto = new EntregaDTO("express", 5.3, "Paraiba");
        CompraDTO compraDto = new CompraDTO("paypal", entregaDto);
        Entrega entregaMock = mock(EntregaPadrao.class);

		setGetClienteById();
		when(clienteMock.getCpf()).thenReturn(clienteCpf);
		when(carrinhoService.getCarrinhoById(clienteCpf)).thenReturn(carrinhoMock);
		when(carrinhoMock.isEmpty()).thenReturn(false);
		when(carrinhoMock.getItens()).thenReturn(itens);
		when(produtoMock.isDisponivel()).thenReturn(true);
		when(loteService.temEmEstoque(itens)).thenReturn(List.of());
		when(carrinhoMock.getTotalItens()).thenReturn(6);
		when(clienteMock.getTipo()).thenReturn(TipoCliente.NORMAL);
		when(carrinhoMock.getTotal()).thenReturn(totalCompra);
        when(entregaService.geraEntrega(any())).thenReturn(entregaMock);
		when(pagamentoService.geraPagamento(any(), anyString(), any(), any()))
						.thenReturn(pagamentoMock);

		Compra compra = compraService.finalizaCompra(idCliente, compraDto);

		assertEquals(clienteMock, compra.getCliente());
		assertEquals(itens, compra.getProdutos());
		assertEquals(pagamentoMock, compra.getPagamento());

		verify(loteService).retiraItensDoEstoque(itens);
		verify(carrinhoService).removeTodosProdutos(carrinhoMock);
		verify(compraRepository).save(compra);
	}

	@Test
	public void finalizaCompraComCarrinhoVazioLancaErro() {
        CompraDTO compraDto = mock(CompraDTO.class);

		setGetClienteById();
		when(clienteMock.getCpf()).thenReturn(clienteCpf);
		when(carrinhoService.getCarrinhoById(clienteCpf)).thenReturn(carrinhoMock);
		when(carrinhoMock.isEmpty()).thenReturn(true);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> compraService.finalizaCompra(idCliente, compraDto));

		assertEquals(ErroCompra.CARRINHO_VAZIO, erro.getMessage());

		verifyNoInteractions(loteService, pagamentoService);
		verifyNoMoreInteractions(carrinhoService, carrinhoMock, clienteMock);
		verify(compraRepository, never()).save(any());
	}

	@Test
	public void finalizaCompraComProdutoIndisponivelLancaErro() {
		List<ItemCarrinho> itens = List.of(new ItemCarrinho(produtoMock, 6));
        CompraDTO compraDto = mock(CompraDTO.class);

		setGetClienteById();
		when(clienteMock.getCpf()).thenReturn(clienteCpf);
		when(carrinhoService.getCarrinhoById(clienteCpf)).thenReturn(carrinhoMock);
		when(carrinhoMock.isEmpty()).thenReturn(false);
		when(carrinhoMock.getItens()).thenReturn(itens);
		when(produtoMock.isDisponivel()).thenReturn(false);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> compraService.finalizaCompra(idCliente, compraDto));

		assertEquals(ErroCompra.PRODUTOS_INDISPONIVEIS, erro.getMessage());

		verifyNoInteractions(loteService, pagamentoService);
		verifyNoMoreInteractions(carrinhoService, carrinhoMock, clienteMock);
		verify(compraRepository, never()).save(any());
	}

	@Test
	public void finalizaCompraComItensSemEstoqueLancaErro() {
		List<ItemCarrinho> itens = List.of(new ItemCarrinho(produtoMock, 6));
		List<ItemSemEstoque> semEstoque = List.of(new ItemSemEstoque(1L, 6, 3));
        CompraDTO compraDto = mock(CompraDTO.class);

		setGetClienteById();
		when(clienteMock.getCpf()).thenReturn(clienteCpf);
		when(carrinhoService.getCarrinhoById(clienteCpf)).thenReturn(carrinhoMock);
		when(carrinhoMock.isEmpty()).thenReturn(false);
		when(carrinhoMock.getItens()).thenReturn(itens);
		when(produtoMock.isDisponivel()).thenReturn(true);
		when(loteService.temEmEstoque(itens)).thenReturn(semEstoque);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> compraService.finalizaCompra(idCliente, compraDto));

		assertEquals(ErroCompra.ESTOQUE_INSUFICIENTE, erro.getMessage());

		verifyNoInteractions(pagamentoService);
		verifyNoMoreInteractions(carrinhoService, carrinhoMock, clienteMock);
		verify(loteService, never()).retiraItensDoEstoque(any());
		verify(compraRepository, never()).save(any());
	}

	@Test
	public void listaComprasRetornaTodasCompras() {
		String inicio = "2021-01-01";
		String fim = "2021-12-31";
		List<Compra> compras = List.of(mock(Compra.class));

		setGetClienteById();
		when(compraRepository.findByClienteAndDataBetween(eq(clienteMock), any(), any()))
				.thenReturn(compras);

		List<Compra> comprasList = compraService.listaCompras(idCliente, inicio, fim);

		assertEquals(compras, comprasList);
	}

	@Test
	public void listaComprasRetornaListaVazia() {
		String inicio = "2021-01-01";
		String fim = "2021-12-31";

		setGetClienteById();
		when(compraRepository.findByClienteAndDataBetween(eq(clienteMock), any(), any()))
				.thenReturn(List.of());

		List<Compra> comprasList = compraService.listaCompras(idCliente, inicio, fim);

		assertTrue(comprasList.isEmpty());
	}

	@Test
	public void getCompraByIdRetornaCompra() {
		Compra compraMock = mock(Compra.class);
		Long idCompra = 1L;

		when(compraRepository.findById(idCompra))
				.thenReturn(Optional.of(compraMock));

		Compra compra = compraService.getCompraById(idCliente, idCompra);

		assertEquals(compraMock, compra);
	}

	@Test
	public void getCompraByIdInexistenteLancaErro() {
		Long idCompraInexistente = 1L;

		when(compraRepository.findById(idCompraInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> compraService.getCompraById(idCliente, idCompraInexistente));

		assertEquals(ErroCompra.CLIENTE_NAO_POSSUI_COMPRA, erro.getMessage());
	}

	private void setGetClienteById() {
		when(clienteService.getClienteById(idCliente)).thenReturn(clienteMock);
	}

}
