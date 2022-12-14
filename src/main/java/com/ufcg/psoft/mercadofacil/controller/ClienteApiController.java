package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;

import javax.validation.Valid;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.service.ClienteService;

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
@RequestMapping("/api/cliente")
@CrossOrigin
public class ClienteApiController {

	@Autowired
	ClienteService clienteService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Cliente> listaClientes() {
		return clienteService.listaClientes();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente cadastraCliente(@RequestBody @Valid ClienteDTO clienteDTO) {
		return clienteService.cadastraCliente(clienteDTO);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Cliente consultaCliente(@PathVariable("id") Long id) {
		return clienteService.getClienteById(id);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Cliente atualizaCliente(
			@PathVariable("id") Long id,
			@RequestBody @Valid ClienteDTO clienteDTO) {
		clienteService.assertExisteClienteById(id);
		return clienteService.atualizaCliente(id, clienteDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeCliente(@PathVariable("id") Long id) {
		clienteService.removeCliente(id);
	}

}