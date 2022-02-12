package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.model.Cliente;

public interface ClienteService {

	public Cliente getClienteById(Long id);
	
	public Cliente getClienteByCpf(Long cpf);
	
	public void removeCliente(Long id);

	public List<Cliente> listaClientes();
	
	public Cliente cadastraCliente(ClienteDTO clienteDTO);
	
	public Cliente atualizaCliente(Long id, ClienteDTO clienteDTO);

	public void assertExisteClienteById(Long id);

	public void assertExisteClienteByCpf(Long cpf);

}
