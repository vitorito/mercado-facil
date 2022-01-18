package com.ufcg.psoft.mercadofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.exception.ErroCliente;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

	@InjectMocks
	private ClienteService clienteService = new ClienteServiceImpl();

	@Mock
	private ClienteRepository clienteRepository;

	@Mock
	private CarrinhoService carrinhoService;

	private List<Cliente> clientes;

	private Cliente cliente;

	private final Long idCliente = 1L;

	@BeforeEach
	public void setup() {
		this.cliente = geraCliente("João Victor", 12345678910L, 20, "Rua Bacana");
		Cliente cliente2 = geraCliente("Lucas Bento", 12377788800L, 37, "Rua Tal");

		this.clientes = new ArrayList<Cliente>() {
			{
				add(cliente);
				add(cliente2);
			}
		};
	}

	@Test
	public void getClienteByIdRetornaCliente() {
		when(clienteRepository.findById(idCliente))
				.thenReturn(Optional.of(cliente));

		assertEquals(cliente, clienteService.getClienteById(idCliente));
		verify(clienteRepository, times(1)).findById(idCliente);
	}

	@Test
	public void getClienteByIdInexistenteRetornaErro() {
		Long idInexistente = 2L;

		when(clienteRepository.findById(idInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.getClienteById(idInexistente));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_ID, erro.getMessage());
		verify(clienteRepository, times(1)).findById(idInexistente);
	}

	@Test
	public void getClienteByCpfRetornaCliente() {
		Long cpf = cliente.getCpf();
		when(clienteRepository.findByCpf(cpf))
				.thenReturn(Optional.of(cliente));

		assertEquals(cliente, clienteService.getClienteByCpf(cpf));
		verify(clienteRepository, times(1)).findByCpf(cpf);
	}

	@Test
	public void getClienteByCpfInexistenteRetornaErro() {
		Long cpfInexistente = 100045600012L;

		when(clienteRepository.findByCpf(cpfInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.getClienteByCpf(cpfInexistente));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_CPF, erro.getMessage());
		verify(clienteRepository, times(1)).findByCpf(cpfInexistente);
	}

	@Test
	public void removeClienteSemRetorno() {
		when(clienteRepository.findById(idCliente))
				.thenReturn(Optional.of(cliente));

		clienteService.removeCliente(idCliente);

		verify(clienteRepository, times(1)).findById(idCliente);
		verify(clienteRepository, times(1)).delete(cliente);
		verify(carrinhoService, times(1)).removeCarrinho(cliente.getCpf());
	}

	@Test
	public void removeClienteIdInexistenteRetornaErro() {
		Long idInexistente = 2L;

		when(clienteRepository.findById(idInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.removeCliente(idInexistente));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_ID, erro.getMessage());

		verify(clienteRepository, times(1)).findById(idInexistente);
		verify(clienteRepository, never()).delete(any());
		verify(carrinhoService, never()).removeCarrinho(any());
	}

	@Test
	public void listaClientesRetornaClientesCadastrados() {
		when(clienteRepository.findAll())
				.thenReturn(this.clientes);

		List<Cliente> listaClientes = clienteService.listaClientes();

		assertEquals(this.clientes, listaClientes);
		verify(clienteRepository, times(1)).findAll();
	}

	@Test
	public void listaClientesRetornaListaVazia() {
		when(clienteRepository.findAll())
				.thenReturn(List.of());

		List<Cliente> listaClientes = clienteService.listaClientes();

		assertTrue(listaClientes.isEmpty());
		verify(clienteRepository, times(1)).findAll();
	}

	@Test
	public void cadastraClienteRetornaClienteCadastrado() {
		Long clienteDtoCpf = 10257965532L;
		ClienteDTO clienteDTO = geraClienteDTO("Zé Carlos", clienteDtoCpf, 53, "Rua Coala");
		Cliente clienteCadastrado = clienteService.cadastraCliente(clienteDTO);

		assertEquals(clienteDTO.getNome(), clienteCadastrado.getNome());
		assertEquals(clienteDTO.getCpf(), clienteCadastrado.getCpf());
		assertEquals(clienteDTO.getIdade(), clienteCadastrado.getIdade());
		assertEquals(clienteDTO.getEndereco(), clienteCadastrado.getEndereco());

		verify(clienteRepository, times(1)).existsByCpf(clienteDtoCpf);
		verify(carrinhoService, times(1)).cadastraCarrinho(clienteDtoCpf);
		verify(clienteRepository, times(1)).save(clienteCadastrado);
	}

	@Test
	public void cadastraClienteCpfJaExistenteRetornaErro() {
		ClienteDTO clienteDTO = geraClienteDTO("Antônio", 10257965532L, 53, "Rua Top");

		when(clienteRepository.existsByCpf(clienteDTO.getCpf()))
				.thenReturn(true);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.cadastraCliente(clienteDTO));

		assertEquals(ErroCliente.CLIENTE_JA_CADASTRADO, erro.getMessage());

		verify(carrinhoService, never()).cadastraCarrinho(any());
		verify(clienteRepository, never()).save(any());
	}

	@Test
	public void atualizaClienteRetornaClienteAtualizado() {
		Long clienteCpf = cliente.getCpf();
		ClienteDTO clienteDTO = geraClienteDTO("Maria", clienteCpf, 22, "Rua Massa");

		when(clienteRepository.findById(idCliente))
				.thenReturn(Optional.of(cliente));

		Cliente clienteAtualizado = clienteService.atualizaCliente(idCliente, clienteDTO);

		assertEquals(clienteCpf, clienteAtualizado.getCpf());
		assertEquals(clienteDTO.getNome(), clienteAtualizado.getNome());
		assertEquals(clienteDTO.getIdade(), clienteAtualizado.getIdade());
		assertEquals(clienteDTO.getEndereco(), clienteAtualizado.getEndereco());

		verify(clienteRepository, times(1)).save(cliente);
	}

	@Test
	public void atualizaClienteIdInexistenteRetornaErro() {
		String clienteNome = cliente.getNome();
		Long clienteCpf = cliente.getCpf();
		int clienteIdade = cliente.getIdade();
		String clienteEndereco = cliente.getEndereco();

		Long idInexistente = 2L;
		ClienteDTO clienteDTO = geraClienteDTO("Maria", clienteCpf, 22, "Rua Massa");

		when(clienteRepository.findById(idInexistente))
				.thenReturn(Optional.empty());

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.atualizaCliente(idInexistente, clienteDTO));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_ID, erro.getMessage());

		assertEquals(clienteNome, cliente.getNome());
		assertEquals(clienteCpf, cliente.getCpf());
		assertEquals(clienteIdade, cliente.getIdade());
		assertEquals(clienteEndereco, cliente.getEndereco());

		verify(clienteRepository, never()).save(any());
	}

	@Test
	public void atualizaClienteCpfInexistenteRetornaErro() {
		String clienteNome = cliente.getNome();
		Long clienteCpf = cliente.getCpf();
		int clienteIdade = cliente.getIdade();
		String clienteEndereco = cliente.getEndereco();

		Long cpfInexistente = 40028922L;
		ClienteDTO clienteDTO = geraClienteDTO("Maria", cpfInexistente, 22, "Rua Massa");

		when(clienteRepository.findById(idCliente))
				.thenReturn(Optional.of(cliente));

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.atualizaCliente(idCliente, clienteDTO));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_CPF, erro.getMessage());

		assertEquals(clienteNome, cliente.getNome());
		assertEquals(clienteCpf, cliente.getCpf());
		assertEquals(clienteIdade, cliente.getIdade());
		assertEquals(clienteEndereco, cliente.getEndereco());

		verify(clienteRepository, never()).save(any());
	}

	@Test
	public void assertExisteClienteByIdOk() {
		when(clienteRepository.existsById(idCliente))
				.thenReturn(true);

		clienteService.assertExisteClienteById(idCliente);
	}

	@Test
	public void assertExisteClienteByIdRetornaErro() {
		when(clienteRepository.existsById(idCliente))
				.thenReturn(false);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.assertExisteClienteById(idCliente));
			
		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_ID, erro.getMessage());
	}

	@Test
	public void assertExisteClienteByCpfOk() {
		when(clienteRepository.existsByCpf(idCliente))
				.thenReturn(true);

		clienteService.assertExisteClienteByCpf(idCliente);
	}

	@Test
	public void assertExisteClienteByCpfRetornaErro() {
		when(clienteRepository.existsByCpf(idCliente))
				.thenReturn(false);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.assertExisteClienteByCpf(idCliente));
			
		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_CPF, erro.getMessage());
	}

	private ClienteDTO geraClienteDTO(String nome, Long cpf, int idade, String endereco) {
		return ClienteDTO.builder()
				.nome(nome)
				.cpf(cpf)
				.idade(idade)
				.endereco(endereco)
				.build();
	}

	private Cliente geraCliente(String nome, Long cpf, int idade, String endereco) {
		return Cliente.builder()
				.nome(nome)
				.cpf(cpf)
				.idade(idade)
				.endereco(endereco)
				.build();
	}

}
