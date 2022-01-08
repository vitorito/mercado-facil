package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.model.Cliente;

public interface ClienteService {

	public Cliente getClienteById(Long id);
	
	public Cliente getClienteByCPF(Long cpf);
	
	public void removeCliente(Long id);

	public List<Cliente> listaClientes();
	
	public Cliente cadastraCliente(ClienteDTO clienteDTO);
	
	public Cliente atualizaCliente(ClienteDTO clienteDTO);

	public void assertExisteClienteById(Long id);

	public void assertExisteClienteByCPF(Long cpf);

}
