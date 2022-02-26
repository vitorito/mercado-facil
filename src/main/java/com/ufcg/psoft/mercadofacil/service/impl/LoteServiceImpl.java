package com.ufcg.psoft.mercadofacil.service.impl;

import static com.ufcg.psoft.mercadofacil.validation.FieldValidator.positive;

import java.util.ArrayList;
import java.util.List;

import com.ufcg.psoft.mercadofacil.model.ItemCarrinho;
import com.ufcg.psoft.mercadofacil.model.ItemSemEstoque;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import com.ufcg.psoft.mercadofacil.service.LoteService;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoteServiceImpl implements LoteService {

	@Autowired
	private LoteRepository loteRepository;

	@Autowired
	ProdutoService produtoService;

	@Override
	public List<Lote> listaLotes() {
		return loteRepository.findAll();
	}

	@Override
	public Lote cadastraLote(Long idProduto, int quantidade) {
		positive(quantidade, "A quantidade de itens tem que ser maior que 0.");

		Produto produto = produtoService.getProdutoById(idProduto);
		Lote lote = new Lote(produto, quantidade);
		salvaLote(lote);
		produtoService.tornaDisponivel(produto);

		return lote;
	}

	@Override
	public List<Lote> getLotesByProduto(Produto produto) {
		return loteRepository.findByProdutoOrderByQuantidade(produto);
	}

	@Override
	public void retiraItensDoEstoque(List<ItemCarrinho> itens) {
		for (ItemCarrinho item: itens) {
			int produtosEmEstoque = removeProdutoDoEstoque(item);
			if (produtosEmEstoque <= 0)
				produtoService.tornaIndisponivel(item.getProduto());
		}
	}

	@Override
	public List<ItemSemEstoque> temEmEstoque(List<ItemCarrinho> produtos) {
		List<ItemSemEstoque> naoTem = new ArrayList<>();

		for (ItemCarrinho item : produtos) {
			Produto produto = item.getProduto();
			int emEstoque = getTotalDeProdutosNoEstoque(produto);
			int retirar = item.getQuantidade();
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

	private int removeProdutoDoEstoque(ItemCarrinho item) {
		List<Lote> lotes = getLotesByProduto(item.getProduto());
		int quantidadeDeProdutos = item.getQuantidade();
		for (Lote lote : lotes) {
			quantidadeDeProdutos = lote.getQuantidade() - Math.abs(quantidadeDeProdutos);
			if (quantidadeDeProdutos > 0) {
				lote.setQuantidade(quantidadeDeProdutos);
				salvaLote(lote);
				break;
			}
			removeLote(lote);
		}
		return quantidadeDeProdutos;
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
				.mapToInt(Lote::getQuantidade)
				.sum();

		return total;
	}

}
