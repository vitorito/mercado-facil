package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
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
	}

	@Override
	public void removeProdutos(Carrinho carrinho, Produto produto, int numDeItens) {
		carrinho.removeProdutos(produto, numDeItens);
	}

	@Override
	public void removeTodosProdutos(Carrinho carrinho) {
		carrinho.removeTodosProdutos();
	}

	@Override
	public List<Produto> listaProdutos(Carrinho carrinho) {
		// TODO Auto-generated method stub
		return null;
	}

}
