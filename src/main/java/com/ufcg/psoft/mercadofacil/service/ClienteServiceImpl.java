package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.exception.ErroCliente;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CarrinhoService carrinhoService;

	@Override
	public Cliente getClienteById(Long id) {
		return clienteRepository.findById(id).orElseThrow(
				() -> ErroCliente.erroClienteNaoEncontradoId());
	}

	@Override
	public Cliente getClienteByCpf(Long cpf) {
		return clienteRepository.findByCpf(cpf).orElseThrow(
				() -> ErroCliente.erroClienteNaoEncontradoCPF());
	}

	@Override
	public void removeCliente(Long id) {
		Cliente cliente = getClienteById(id);
		clienteRepository.delete(cliente);
		carrinhoService.removeCarrinho(cliente.getCpf());
	}

	@Override
	public List<Cliente> listaClientes() {
		List<Cliente> clientes = clienteRepository.findAll();

		if (clientes.isEmpty()) {
			throw ErroCliente.erroSemClientesCadastrados();
		}

		return clientes;
	}

	@Override
	public Cliente cadastraCliente(ClienteDTO clienteDTO) {
		Long cpf = clienteDTO.getCpf();
		assertClienteNaoCadastrado(cpf);

		Cliente cliente = criaCliente(clienteDTO);
		carrinhoService.cadastraCarrinho(cpf);
		salvaCliente(cliente);

		return cliente;
	}

	@Override
	public Cliente atualizaCliente(ClienteDTO clienteDTO) {
		Cliente cliente = getClienteByCpf(clienteDTO.getCpf());
		cliente.setIdade(clienteDTO.getIdade());
		cliente.setEndereco(clienteDTO.getEndereco());
		salvaCliente(cliente);

		return cliente;
	}

	@Override
	public void assertExisteClienteById(Long id) {
		if (!clienteRepository.existsById(id)) {
			throw ErroCliente.erroClienteNaoEncontradoId();
		}
	}

	@Override
	public void assertExisteClienteByCpf(Long cpf) {
		if (!clienteRepository.existsByCpf(cpf)) {
			throw ErroCliente.erroClienteNaoEncontradoCPF();
		}
	}

	private Cliente criaCliente(ClienteDTO clienteDTO) {
		return new Cliente(
				clienteDTO.getCpf(),
				clienteDTO.getNome(),
				clienteDTO.getIdade(),
				clienteDTO.getEndereco());
	}

	private void salvaCliente(Cliente cliente) {
		clienteRepository.save(cliente);
	}

	private void assertClienteNaoCadastrado(Long cpf) {
		if (clienteRepository.existsByCpf(cpf)) {
			throw ErroCliente.erroClienteJaCadastrado();
		}
	}

}
