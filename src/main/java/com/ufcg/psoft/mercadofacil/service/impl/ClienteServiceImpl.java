package com.ufcg.psoft.mercadofacil.service.impl;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.exception.ErroCliente;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.TipoCliente;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ClienteService;

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
		return clienteRepository.findAll();
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
	public Cliente atualizaCliente(Long id, ClienteDTO clienteDTO) {
		Cliente cliente = getClienteById(id);

		if (!cliente.getCpf().equals(clienteDTO.getCpf())) {
			throw ErroCliente.erroClienteNaoEncontradoCPF();
		}

		TipoCliente tipo = getTipoCliente(clienteDTO.getTipo());
		cliente.setTipo(tipo);
		cliente.setNome(clienteDTO.getNome());
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

	private Cliente criaCliente(ClienteDTO clienteDTO) {
		TipoCliente tipo = getTipoCliente(clienteDTO.getTipo());
		Cliente cliente = Cliente.builder()
				.nome(clienteDTO.getNome())
				.cpf(clienteDTO.getCpf())
				.idade(clienteDTO.getIdade())
				.endereco(clienteDTO.getEndereco())
				.tipo(tipo)
				.build();

		return cliente;
	}

	private TipoCliente getTipoCliente(String tipo) {
		try {
			return TipoCliente.valueOf(tipo.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw ErroCliente.erroTipoInvalidoDeCliente();
		}
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
