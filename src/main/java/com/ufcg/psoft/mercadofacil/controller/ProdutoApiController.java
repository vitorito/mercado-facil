package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ProdutoDTO;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin
public class ProdutoApiController {

	@Autowired
	ProdutoService produtoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Produto> listaProdutos() {
		return produtoService.listaProdutos();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Produto cadastraProduto(@RequestBody ProdutoDTO produtoDTO) {
		return produtoService.cadastraProduto(produtoDTO);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Produto consultaProduto(@PathVariable("id") Long id) {
		return produtoService.getProdutoById(id);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Produto atualizaProduto(
			@PathVariable("id") Long id,
			@RequestBody ProdutoDTO produtoDTO) {
		return produtoService.atualizaProduto(produtoDTO);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void removeProduto(@PathVariable("id") Long id) {
		produtoService.removeProduto(id);
	}
}