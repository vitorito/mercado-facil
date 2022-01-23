package com.ufcg.psoft.mercadofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.DTO.ProdutoDTO;
import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.exception.ErroProduto;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

	@InjectMocks
	private ProdutoService produtoService = new ProdutoServiceImpl();

	@Mock
	private ProdutoRepository produtoRepository;

	@Mock
	private Produto produto1;

	@Mock
	private Produto produto2;

	private Optional<Produto> produto1Op;

	private List<Produto> produtos;

	private Long idProduto = 1L;

	private Long idInexistente = 2L;

	private String codigoProduto = "100200300400";

	private String codigoInexistente = "123456";

	@BeforeEach
	public void setup() {
		this.produtos = List.of(produto1, produto2);
		this.produto1Op = Optional.of(produto1);
	}

	@Test
	public void getProdutoByIdRetornaProduto() {
		when(produtoRepository.findById(idProduto))
				.thenReturn(produto1Op);

		Produto produto = produtoService.getProdutoById(idProduto);

		assertEquals(this.produto1, produto);
	}

	@Test
	public void getProdutoByIdInexistenteLancaErro() {
		when(produtoRepository.findById(idInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> produtoService.getProdutoById(idInexistente));

		assertEquals(ErroProduto.PRODUTO_NAO_CADASTRADO_ID, erro.getMessage());
	}

	@Test
	public void getProdutoByCodigoBarraRetornaProduto() {
		when(produtoRepository.findByCodigoBarra(codigoProduto))
				.thenReturn(produto1Op);

		Produto produto = produtoService.getProdutoByCodigoBarra(codigoProduto);

		assertEquals(this.produto1, produto);
	}

	@Test
	public void getProdutoByCodigoBarraInexistenteLancaErro() {
		when(produtoRepository.findByCodigoBarra(codigoInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> produtoService.getProdutoByCodigoBarra(codigoInexistente));

		assertEquals(ErroProduto.PRODUTO_NAO_CADASTRADO_CODIGO, erro.getMessage());
	}

	@Test
	public void cadastraProdutoRetornaProduto() {
		ProdutoDTO produtoDTO = geraProdutoDTO("Sabão Omo", codigoProduto, "Omo",
				"Limpeza", BigDecimal.valueOf(5.50));

		when(produtoRepository.existsByCodigoBarra(codigoProduto))
				.thenReturn(false);

		Produto produto = produtoService.cadastraProduto(produtoDTO);

		assertEquals(produtoDTO.getNome(), produto.getNome());
		assertEquals(produtoDTO.getCodigoBarra(), produto.getCodigoBarra());
		assertEquals(produtoDTO.getFabricante(), produto.getFabricante());
		assertEquals(produtoDTO.getCategoria(), produto.getCategoria());
		assertEquals(produtoDTO.getPreco(), produto.getPreco());

		verify(produtoRepository, times(1)).save(produto);
	}

	@Test
	public void cadastraProdutoJaCadastradoLancaErro() {
		ProdutoDTO produtoDTO = mock(ProdutoDTO.class);

		when(produtoDTO.getCodigoBarra()).thenReturn(codigoProduto);

		when(produtoRepository.existsByCodigoBarra(codigoProduto))
				.thenReturn(true);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> produtoService.cadastraProduto(produtoDTO));

		assertEquals(ErroProduto.PRODUTO_JA_CADASTRADO, erro.getMessage());

		verify(produtoDTO, never()).getNome();
		verify(produtoDTO, never()).getFabricante();
		verify(produtoDTO, never()).getCategoria();
		verify(produtoDTO, never()).getPreco();
		verify(produtoRepository, never()).save(any());
	}

	@Test
	public void removeProdutoOK() {
		when(produtoRepository.findById(idProduto))
				.thenReturn(produto1Op);

		produtoService.removeProduto(idProduto);

		verify(produtoRepository, times(1)).delete(produto1);
	}

	@Test
	public void removeProdutoIdInexistenteLancaErro() {
		when(produtoRepository.findById(idInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> produtoService.removeProduto(idInexistente));

		assertEquals(ErroProduto.PRODUTO_NAO_CADASTRADO_ID, erro.getMessage());

		verify(produtoRepository, never()).delete(any());
	}

	@Test
	public void atualizaProdutoRetornaProdutoAtualizado() {
		ProdutoDTO produtoDTO = geraProdutoDTO("Sabão Omo", codigoProduto, "Omo",
				"Limpeza", BigDecimal.valueOf(5.50));
		when(produtoRepository.findByCodigoBarra(codigoProduto))
				.thenReturn(produto1Op);

		Produto produto = produtoService.atualizaProduto(produtoDTO);

		assertEquals(produto1, produto);

		verify(produto1, never()).setCodigoBarra(any());
		verify(produto1, times(1)).setNome(produtoDTO.getNome());
		verify(produto1, times(1)).setFabricante(produtoDTO.getFabricante());
		verify(produto1, times(1)).setCategoria(produtoDTO.getCategoria());
		verify(produto1, times(1)).setPreco(produtoDTO.getPreco());

		verify(produtoRepository, times(1)).save(produto);
	}

	@Test
	public void atualizaProdutoInexistenteLancaErro() {
		ProdutoDTO produtoDTO = mock(ProdutoDTO.class);

		when(produtoDTO.getCodigoBarra()).thenReturn(codigoInexistente);

		when(produtoRepository.findByCodigoBarra(codigoInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> produtoService.atualizaProduto(produtoDTO));

		assertEquals(ErroProduto.PRODUTO_NAO_CADASTRADO_CODIGO, erro.getMessage());

		verify(produtoDTO, never()).getNome();
		verify(produtoDTO, never()).getFabricante();
		verify(produtoDTO, never()).getCategoria();
		verify(produtoDTO, never()).getPreco();

		verify(produtoRepository, never()).save(any());
	}

	@Test
	public void listaProdutosRetornaListaDeProdutos() {
		when(produtoRepository.findAll()).thenReturn(produtos);

		List<Produto> produtosLista = produtoService.listaProdutos();

		assertEquals(produtos, produtosLista);
	}

	@Test
	public void listaProdutosRetornaListaVazia() {
		when(produtoRepository.findAll()).thenReturn(List.of());

		List<Produto> produtosLista = produtoService.listaProdutos();

		assertTrue(produtosLista.isEmpty());
	}

	@Test
	public void checaDisponibilidadeRetornaListaVazia() {
		when(produto1.isDisponivel()).thenReturn(true);
		when(produto2.isDisponivel()).thenReturn(true);

		List<Produto> indisponiveis = produtoService.checaDisponibilidade(produtos);

		assertTrue(indisponiveis.isEmpty());
	}

	@Test
	public void checaDisponibilidadeRetornaListaDeIndisponiveis() {
		when(produto1.isDisponivel()).thenReturn(true);
		when(produto2.isDisponivel()).thenReturn(false);

		List<Produto> indisponiveis = produtoService.checaDisponibilidade(produtos);

		assertEquals(1, indisponiveis.size());
		assertEquals(produto2, indisponiveis.get(0));
	}

	@Test
	public void isDisponivelRetornaSeOProdutoEstaDisponivel() {
		when(produto1.isDisponivel()).thenReturn(true);

		assertTrue(produtoService.isDisponivel(produto1));

		when(produto2.isDisponivel()).thenReturn(false);

		assertFalse(produtoService.isDisponivel(produto2));
	}

	@Test
	public void tornaDisponivelDeixaOProdutoDisponivel() {
		doCallRealMethod().when(produto1).tornaDisponivel();
		doCallRealMethod().when(produto1).isDisponivel();

		produtoService.tornaDisponivel(produto1);

		assertTrue(produto1.isDisponivel());

		verify(produtoRepository, times(1)).save(produto1);
	}

	@Test
	public void tornaIndisponivelDeixaOProdutoIndisponivel() {
		doCallRealMethod().when(produto2).tornaIndisponivel();
		doCallRealMethod().when(produto2).isDisponivel();

		produtoService.tornaIndisponivel(produto2);

		assertFalse(produto2.isDisponivel());

		verify(produtoRepository, times(1)).save(produto2);
	}


	private ProdutoDTO geraProdutoDTO(String nome, String codigoBarra,
			String fabricante, String categoria, BigDecimal preco) {
		return ProdutoDTO.builder()
				.nome(nome)
				.codigoBarra(codigoBarra)
				.fabricante(fabricante)
				.categoria(categoria)
				.preco(preco)
				.build();
	}

}
