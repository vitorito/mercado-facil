package com.ufcg.psoft.mercadofacil.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.model.ItemDoCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemInsuficienteNoEstoque;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;

@Service
public class LoteServiceImpl implements LoteService {

	@Autowired
	private LoteRepository loteRepository;

	public List<Lote> listarLotes() {
		return loteRepository.findAll();
	}

	public void salvarLote(Lote lote) {
		loteRepository.save(lote);
	}

	public Lote criaLote(int numItens, Produto produto) {
		Lote lote = new Lote(produto, numItens);
		return lote;
	}

	@Override
	public void removeLote(Lote lote) {
		loteRepository.delete(lote);
	}

	@Override
	public List<Lote> getLotesByProduto(Produto produto) {
		List<Lote> lotes = loteRepository.findByProduto(produto);
		ordenaLotes(lotes);
		return lotes;
	}

	@Override
	public void retiraItensDoEstoque(List<ItemDoCarrinho> produtos) {
		for (ItemDoCarrinho item : produtos) {
			List<Lote> lotes = getLotesByProduto(item.getProduto());

			int novaQntdDeProdutos = item.getNumDeItens();
			for (Lote lote : lotes) {
				novaQntdDeProdutos = lote.getNumeroDeItens() - Math.abs(novaQntdDeProdutos);
				if (novaQntdDeProdutos > 0) {
					lote.setNumeroDeItens(novaQntdDeProdutos);
					salvarLote(lote);
					break;
				}
				removeLote(lote);
			}
		}
	}

	@Override
	public List<ItemInsuficienteNoEstoque> temNoEstoque(List<ItemDoCarrinho> produtos) {
		List<ItemInsuficienteNoEstoque> naoTem = new ArrayList<>();

		for (ItemDoCarrinho item : produtos) {
			Produto produto = item.getProduto();
			int emEstoque = getTotalDeProdutosNoEstoque(produto);
			int retirar = item.getNumDeItens();
			if ((emEstoque - retirar) < 0) {
				naoTem.add(new ItemInsuficienteNoEstoque(produto, retirar, emEstoque));
			}
		}

		return naoTem;
	}

	/**
	 * Calcula a quantidade de um produto disponível no estoque,
	 * contabilizando todos os lotes.
	 * 
	 * @param produto O produto a ser buscado.
	 * @return O total desse produto no estoque.
	 */
	private int getTotalDeProdutosNoEstoque(Produto produto) {
		int total = 0;
		List<Lote> lotes = getLotesByProduto(produto);

		for (Lote lote : lotes) {
			total += lote.getNumeroDeItens();
		}

		return total;
	}

	/**
	 * Ordena crescentemente os lotes pela quantidades de produtos neles.
	 * 
	 * @param lotes Lista de lotes a ser ordenada.
	 */
	private void ordenaLotes(List<Lote> lotes) {
		Collections.sort(lotes, new Comparator<Lote>() {
			@Override
			public int compare(Lote lote1, Lote lote2) {
				return lote1.getNumeroDeItens() - lote2.getNumeroDeItens();
			}
		});
	}

}
