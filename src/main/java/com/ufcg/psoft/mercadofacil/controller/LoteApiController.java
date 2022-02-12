package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.service.LoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lote")
@CrossOrigin
public class LoteApiController {

	@Autowired
	LoteService loteService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Lote> listaLotes() {
		return loteService.listaLotes();
	}

	@PostMapping("/{idProduto}")
	@ResponseStatus(HttpStatus.CREATED)
	public Lote cadastraLote(
			@PathVariable("idProduto") Long idProduto,
			@RequestBody int numItens) {
		return loteService.cadastraLote(idProduto, numItens);
	}
}