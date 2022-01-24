package com.ufcg.psoft.mercadofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.DTO.ItemCarrinhoDTO;
import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.exception.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.exception.ErroProduto;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarrinhoServiceTest {

	@InjectMocks
	private CarrinhoService carrinhoService = new CarrinhoServiceImpl();

	@Mock
	private CarrinhoRepository carrinhoRepository;

	@Mock
	private ProdutoService produtoService;

	@Mock
	private Carrinho carrinhoMock;

	@Mock
	private Produto produtoMock;

	private ItemCarrinhoDTO itemCarrinhoDTO;

	private Optional<Carrinho> carrinhoOp;

	private Long idCarrinho = 1234567890L;

	private Long idProduto = 10001L;

	private Long idCarrinhoInexistente = 1000000000L;

	@BeforeEach
	public void setup() {
		this.carrinhoOp = Optional.of(carrinhoMock);
		this.itemCarrinhoDTO = new ItemCarrinhoDTO(idProduto, 2);
	}

	@Test
	public void getCarrinhoByIdRetornaCarrinho() {
		setCarrinhoMock();

		Carrinho carrinho = carrinhoService.getCarrinhoById(idCarrinho);

		assertEquals(carrinhoMock, carrinho);
	}

	@Test
	public void getCarrinhoByIdInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.getCarrinhoById(idCarrinhoInexistente));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());
	}

	@Test
	public void cadastraCarrinhoRetornaCarrinhoCadastrado() {
		when(carrinhoRepository.existsById(idCarrinho)).thenReturn(false);

		Carrinho carrinho = carrinhoService.cadastraCarrinho(idCarrinho);

		assertEquals(idCarrinho, carrinho.getId());
		assertTrue(carrinho.getProdutos().isEmpty());
	}

	@Test
	public void cadastraCarrinhoJaCadastradoLancaErro() {
		when(carrinhoRepository.existsById(idCarrinho)).thenReturn(true);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.cadastraCarrinho(idCarrinho));

		assertEquals(ErroCarrinho.CARRINHO_JA_CADASTRADO, erro.getMessage());
		verify(carrinhoRepository, never()).save(any());
	}

	@Test
	public void removeCarrinho() {
		setCarrinhoMock();

		carrinhoService.removeCarrinho(idCarrinho);

		verify(carrinhoRepository, times(1)).delete(carrinhoMock);
	}

	@Test
	public void removeCarrinhoInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.removeCarrinho(idCarrinhoInexistente));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());
		verify(carrinhoRepository, never()).delete(any());
	}

	@Test
	public void adicionaProdutosRetornaCarrinhoComOsProdutosAdicionados() {
		setCarrinhoMock();
		setProdutoMock();

		when(produtoService.isDisponivel(produtoMock)).thenReturn(true);

		Carrinho carrinho = carrinhoService.adicionaProdutos(idCarrinho, itemCarrinhoDTO);

		assertEquals(carrinhoMock, carrinho);

		verify(carrinhoMock, times(1))
				.adicionaProdutos(produtoMock, itemCarrinhoDTO.getNumDeItens());
		verify(carrinhoRepository, times(1)).save(carrinhoMock);
	}

	@Test
	public void adicionaProdutosEmCarrinhoInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.adicionaProdutos(idCarrinhoInexistente, itemCarrinhoDTO));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());

		verify(produtoService, never()).getProdutoById(any());
		verify(produtoService, never()).isDisponivel(any());
		verify(carrinhoRepository, never()).save(any());
	}

	@Test
	public void adicionaProdutoIndisponivelLancaErro() {
		setCarrinhoMock();
		setProdutoMock();

		when(produtoService.isDisponivel(produtoMock)).thenReturn(false);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.adicionaProdutos(idCarrinho, itemCarrinhoDTO));

		assertEquals(ErroProduto.PRODUTO_INDISPONIVEL, erro.getMessage());

		verify(carrinhoMock, never()).adicionaProdutos(any(), anyInt());
		verify(carrinhoRepository, never()).save(any());
	}

	@Test
	public void removeProdutoRetornaCarrinhoSemOProduto() {
		setCarrinhoMock();
		setProdutoMock();

		Carrinho carrinho = carrinhoService.removeProduto(idCarrinho, itemCarrinhoDTO);

		assertEquals(carrinhoMock, carrinho);

		verify(carrinhoMock, times(1)).removeProduto(produtoMock, 2);
		verify(carrinhoRepository, times(1)).save(carrinhoMock);
	}

	@Test
	public void removeProdutoDeCarrinhoInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.removeProduto(idCarrinhoInexistente, itemCarrinhoDTO));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());

		verify(produtoService, never()).getProdutoById(any());
		verify(carrinhoRepository, never()).save(any());
	}

	@Test
	public void removeTodosProdutos() {
		setCarrinhoMock();

		carrinhoService.removeTodosProdutos(idCarrinho);

		verify(carrinhoMock, times(1)).removeTodosProdutos();
		verify(carrinhoRepository, times(1)).save(carrinhoMock);
	}

	@Test
	public void removeTodosProdutosDeCarrinhoInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.removeTodosProdutos(idCarrinhoInexistente));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());
		verify(carrinhoRepository, never()).save(any());
	}

	@Test
	public void listaItensDoCarrinhoRetornaListaDeItens() {
		setCarrinhoMock();

		ItemCarrinho item = new ItemCarrinho(produtoMock, 2);
		List<ItemCarrinho> itens = List.of(item);

		when(carrinhoMock.getItens()).thenReturn(itens);

		List<ItemCarrinho> listaItens = carrinhoService.listaItensDoCarrinho(idCarrinho);

		assertEquals(itens, listaItens);
	}

	@Test
	public void listaItensDoCarrinhoRetornaListaVazia() {
		setCarrinhoMock();

		when(carrinhoMock.getItens()).thenReturn(List.of());

		List<ItemCarrinho> listaItens = carrinhoService.listaItensDoCarrinho(idCarrinho);

		assertTrue(listaItens.isEmpty());
	}

	@Test
	public void listaItensDeCarrinhoInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.listaItensDoCarrinho(idCarrinhoInexistente));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());
	}

	@Test
	public void listaProdutosDoCarrinhoRetornaListaDeProdutos() {
		setCarrinhoMock();

		List<Produto> produtos = List.of(produtoMock);
		when(carrinhoMock.getProdutos()).thenReturn(produtos);

		List<Produto> listaProdutos = carrinhoService.listaProdutosDoCarrinho(idCarrinho);

		assertEquals(produtos, listaProdutos);
	}

	@Test
	public void listaProdutosDoCarrinhoRetornaListaVazia() {
		setCarrinhoMock();

		when(carrinhoMock.getProdutos()).thenReturn(List.of());

		List<Produto> listaProdutos = carrinhoService.listaProdutosDoCarrinho(idCarrinho);

		assertTrue(listaProdutos.isEmpty());
	}

	@Test
	public void listaProdutosDeCarrinhoInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.listaProdutosDoCarrinho(idCarrinhoInexistente));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());
	}

	@Test
	public void calculaTotalRetornaOTotalDoCarrinho() {
		setCarrinhoMock();

		BigDecimal total = BigDecimal.valueOf(7.5);
		when(carrinhoMock.getTotal()).thenReturn(total);

		assertEquals(total, carrinhoService.calculaTotal(idCarrinho));
	}


	@Test
	public void calculaTotalDeCarrinhoInexistenteLancaErro() {
		setCarrinhoInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> carrinhoService.calculaTotal(idCarrinhoInexistente));

		assertEquals(ErroCarrinho.NAO_HA_CARRINHO, erro.getMessage());
	}

	private void setCarrinhoMock() {
		when(carrinhoRepository.findById(idCarrinho)).thenReturn(carrinhoOp);
	}

	private void setCarrinhoInexistente() {
		when(carrinhoRepository.findById(idCarrinhoInexistente))
				.thenReturn(Optional.empty());
	}

	private void setProdutoMock() {
		when(produtoService.getProdutoById(idProduto)).thenReturn(produtoMock);
	}

}
