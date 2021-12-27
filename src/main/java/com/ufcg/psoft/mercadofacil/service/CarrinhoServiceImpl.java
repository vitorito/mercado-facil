package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.ItemDoCarrinho;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoRepository;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

	@Autowired
	private CarrinhoRepository carrinhoRepository;

	@Override
	public Optional<Carrinho> getCarrinhoById(Long id) {
		return carrinhoRepository.findById(id);
	}

	@Override
	public Carrinho criaCarrinho(Long id) {
		return new Carrinho(id);
	}

	@Override
	public void removeCarrinho(Carrinho carrinho) {
		carrinhoRepository.delete(carrinho);
	}

	@Override
	public void salvaCarrinho(Carrinho carrinho) {
		carrinhoRepository.save(carrinho);
	}

	@Override
	public void adicionaProdutos(Carrinho carrinho, Produto produto, int numDeItens) {
		carrinho.adicionaProdutos(produto, numDeItens);
		salvaCarrinho(carrinho);
	}

	@Override
	public void removeProdutos(Carrinho carrinho, Produto produto, int numDeItens) {
		carrinho.removeProdutos(produto, numDeItens);
		salvaCarrinho(carrinho);
	}

	@Override
	public void removeTodosProdutos(Carrinho carrinho) {
		carrinho.removeTodosProdutos();
		salvaCarrinho(carrinho);
	}

	@Override
	public List<ItemDoCarrinho> listaProdutos(Carrinho carrinho) {
		return carrinho.getProdutos();
	}

	@Override
	public boolean containsProduto(Carrinho carrinho, Produto produto) {
		return carrinho.containsProduto(produto);
	}

}
