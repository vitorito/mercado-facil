package com.ufcg.psoft.mercadofacil.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.exception.ErroLote;
import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;

@Service
public class LoteServiceImpl implements LoteService {

	@Autowired
	private LoteRepository loteRepository;

	@Autowired
	ProdutoService produtoService;

	@Override
	public List<Lote> listaLotes() {
		List<Lote> lotes = loteRepository.findAll();

		if (lotes.isEmpty()) {
			throw ErroLote.erroSemLotesCadastrados();
		}

		return lotes;
	}

	@Override
	public Lote cadastraLote(Long idProduto, int numItens) {
		if (numItens <= 0) {
			throw new IllegalArgumentException("O número de itens não pode ser menor que 1.");
		}

		Produto produto = produtoService.getProdutoById(idProduto);
		Lote lote = new Lote(produto, numItens);
		salvaLote(lote);
		produtoService.tornaDisponivel(idProduto);

		return lote;
	}

	@Override
	public List<Lote> getLotesByProduto(Produto produto) {
		List<Lote> lotes = loteRepository.findByProduto(produto);
		ordenaLotes(lotes);
		return lotes;
	}

	@Override
	public void retiraItensDoEstoque(List<ItemCarrinho> produtos) {
		produtos.forEach(item -> {
			Produto produto = item.getProduto();
			List<Lote> lotes = getLotesByProduto(produto);

			int novaQntdDeProdutos = item.getNumDeItens();
			for (Lote lote : lotes) {
				novaQntdDeProdutos = lote.getNumeroDeItens() - Math.abs(novaQntdDeProdutos);
				if (novaQntdDeProdutos > 0) {
					lote.setNumeroDeItens(novaQntdDeProdutos);
					salvaLote(lote);
					break;
				}
				removeLote(lote);
			}
			if (novaQntdDeProdutos <= 0)
				produtoService.tornaIndisponivel(produto);
		});
	}

	@Override
	public List<ItemSemEstoque> temEmEstoque(List<ItemCarrinho> produtos) {
		List<ItemSemEstoque> naoTem = new ArrayList<>();

		for (ItemCarrinho item : produtos) {
			Produto produto = item.getProduto();
			int emEstoque = getTotalDeProdutosNoEstoque(produto);
			int retirar = item.getNumDeItens();
			if ((emEstoque - retirar) < 0) {
				naoTem.add(new ItemSemEstoque(produto.getId(), retirar, emEstoque));
			}
		}

		return naoTem;
	}

	private void salvaLote(Lote lote) {
		loteRepository.save(lote);
	}

	private void removeLote(Lote lote) {
		loteRepository.delete(lote);
	}

	/**
	 * Calcula a quantidade de um produto disponível no estoque,
	 * contabilizando todos os lotes.
	 * 
	 * @param produto O produto a ser buscado.
	 * @return O total desse produto no estoque.
	 */
	private int getTotalDeProdutosNoEstoque(Produto produto) {
		int total = getLotesByProduto(produto).stream()
				.mapToInt(Lote::getNumeroDeItens)
				.sum();

		return total;
	}

	/**
	 * Ordena crescentemente os lotes pela quantidades de produtos neles.
	 * 
	 * @param lotes Lista de lotes a ser ordenada.
	 */
	private void ordenaLotes(List<Lote> lotes) {
		lotes.sort(Comparator.comparing(Lote::getNumeroDeItens));
	}

}
