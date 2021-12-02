package com.ufcg.psoft.mercadofacil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.LoteService;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CarrinhoApiController {
	
	@Autowired
	CarrinhoService carrinhoService;
	
	@Autowired
	LoteService loteService;
	
	@Autowired
	ProdutoService produtoService;

}
