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
	public Optional<Carrinho> getcarrinhoById(Long id) {
		return  carrinhoRepository.findById(id);
	}

	@Override
	public Carrinho criaCarrinho(long id) {
		return new Carrinho(id);
	}

	@Override
	public void salvaCarrinho(Carrinho carrinho) {
		carrinhoRepository.save(carrinho);
	}

	@Override
	public void adicionaProduto(Carrinho carrinho, Produto produto, int numDeItens) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeProduto(Carrinho carrinho, Produto produto, int numDeItens) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTodosProdutos(Carrinho carrinho) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Produto> listaProdutos(Carrinho carrinho) {
		// TODO Auto-generated method stub
		return null;
	}

}
