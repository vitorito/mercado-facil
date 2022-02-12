package com.ufcg.psoft.mercadofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import com.ufcg.psoft.mercadofacil.service.impl.LoteServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoteServiceTest {

	@InjectMocks
	private LoteService loteService = new LoteServiceImpl();

	@Mock
	private ProdutoService produtoService;

	@Mock
	private LoteRepository loteRepository;

	private List<Lote> lotes;

	@Mock
	private Lote lote1;

	@Mock()
	private Lote lote2;

	@Mock
	private Produto produtoMock;

	private Long idProduto = 1L;

	private List<ItemCarrinho> itens;

	@BeforeEach
	public void setup() {
		this.lotes = List.of(lote1, lote2);
		this.itens = List.of(new ItemCarrinho(produtoMock, 2));
	}

	@Test
	public void listaLotesRetornaListaDeLotes() {
		when(loteRepository.findAll())
				.thenReturn(this.lotes);

		List<Lote> listaLotes = loteService.listaLotes();

		assertEquals(this.lotes, listaLotes);
	}

	@Test
	public void listaLotesRetornaListaVazia() {
		when(loteRepository.findAll())
				.thenReturn(List.of());

		List<Lote> listaLotes = loteService.listaLotes();

		assertTrue(listaLotes.isEmpty());
	}

	@Test
	public void cadastraLoteRetornaLoteCadastrado() {
		int numDeItens = 3;

		when(produtoService.getProdutoById(idProduto))
				.thenReturn(this.produtoMock);

		Lote lote = loteService.cadastraLote(idProduto, numDeItens);

		assertEquals(produtoMock, lote.getProduto());
		assertEquals(numDeItens, lote.getQuantidade());

		verify(loteRepository, times(1)).save(lote);
		verify(produtoService, times(1)).tornaDisponivel(produtoMock);
	}

	@Test
	public void cadastraLoteComQuantidadeNegativaRetornaErro() {
		int numDeItens = -2;

		IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
				() -> loteService.cadastraLote(idProduto, numDeItens));

		assertEquals("A quantidade de itens tem que ser maior que 0.",
				erro.getMessage());

		verify(produtoService, never()).getProdutoById(any());
		verify(loteRepository, never()).save(any());
		verify(produtoService, never()).tornaDisponivel(any());
	}

	@Test
	public void getLotesByProdutoRetornaListaDeLotes() {
		when(loteRepository.findByProdutoOrderByQuantidade(produtoMock))
				.thenReturn(this.lotes);

		List<Lote> lotesList = loteService.getLotesByProduto(produtoMock);
		assertEquals(this.lotes, lotesList);
	}

	@Test
	public void getLotesByProdutoRetornaListaVazia() {
		when(loteRepository.findByProdutoOrderByQuantidade(produtoMock))
				.thenReturn(List.of());

		List<Lote> lotesList = loteService.getLotesByProduto(produtoMock);
		assertTrue(lotesList.isEmpty());
	}

	@Test
	public void retiraItensDoEstoqueSobraProdutoNoEstoque() {
		List<Lote> lotesCopia = new ArrayList<>(this.lotes);

		doCallRealMethod()
				.when(lote1).getQuantidade();

		doCallRealMethod()
				.when(lote1).setQuantidade(anyInt());

		doCallRealMethod()
				.when(lote2).getQuantidade();

		doCallRealMethod()
				.when(lote2).setQuantidade(anyInt());

		lote1.setQuantidade(1);
		lote2.setQuantidade(2);

		when(loteRepository.findByProdutoOrderByQuantidade(produtoMock))
				.thenReturn(this.lotes);

		doAnswer(m -> lotesCopia.remove(lote1)).when(loteRepository).delete(lote1);

		loteService.retiraItensDoEstoque(itens);

		assertEquals(1, lotesCopia.size());
		assertEquals(lote2, lotesCopia.get(0));
		assertEquals(1, lote2.getQuantidade());

		verify(loteRepository, times(1)).findByProdutoOrderByQuantidade(produtoMock);
		verify(loteRepository, never()).save(lote1);
		verify(loteRepository, times(1)).save(lote2);
		verify(loteRepository, times(1)).delete(any());
		verify(produtoService, never()).tornaIndisponivel(any());
	}

	@Test
	public void retiraItensDoEstoqueNaoSobraProdutosEProdutoFicaIndisponivel() {
		List<Lote> lotesCopia = new ArrayList<>();
		lotesCopia.add(lote2);

		when(loteRepository.findByProdutoOrderByQuantidade(produtoMock))
				.thenReturn(List.of(lote2));

		when(lote2.getQuantidade()).thenReturn(2);

		doAnswer(m -> lotesCopia.remove(lote2)).when(loteRepository).delete(lote2);

		loteService.retiraItensDoEstoque(itens);

		assertTrue(lotesCopia.isEmpty());
		verify(lote2, never()).setQuantidade(any());
		verify(loteRepository, never()).save(lote2);
		verify(loteRepository, times(1)).delete(any());
		verify(produtoService, times(1)).tornaIndisponivel(produtoMock);
	}

	@Test
	public void retiraItensDoEstoqueSemLotesDoProduto() {
		when(loteRepository.findByProdutoOrderByQuantidade(produtoMock))
				.thenReturn(List.of());
		
		loteService.retiraItensDoEstoque(itens);
	
		verify(loteRepository, never()).save(any());
		verify(loteRepository, never()).delete(any());
		verify(produtoService, never()).tornaIndisponivel(any());
	}

	@Test
	public void temEmEstoqueRetornaListaVazia() {
		when(loteRepository.findByProdutoOrderByQuantidade(produtoMock))
				.thenReturn(this.lotes);

		when(lote1.getQuantidade()).thenReturn(1);
		when(lote2.getQuantidade()).thenReturn(1);

		List<ItemSemEstoque> semEstoque = loteService.temEmEstoque(itens);

		assertTrue(semEstoque.isEmpty());
	}

	@Test
	public void temEmEstoqueRetornaListaComOsProdutosSemEstoque() {
		when(loteRepository.findByProdutoOrderByQuantidade(produtoMock))
				.thenReturn(List.of(lote1));

		when(lote1.getQuantidade()).thenReturn(1);
		when(produtoMock.getId()).thenReturn(idProduto);

		List<ItemSemEstoque> semEstoque = loteService.temEmEstoque(itens);
		assertEquals(1, semEstoque.size());

		ItemSemEstoque itemSemEstoque = semEstoque.get(0);
		assertEquals(idProduto, itemSemEstoque.getIdProduto());
		assertEquals(1, itemSemEstoque.getDisponivel());
		assertEquals(2, itemSemEstoque.getRequerido());
	}

}
