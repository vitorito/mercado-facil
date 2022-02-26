package com.ufcg.psoft.mercadofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.exception.ErroCliente;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.TipoCliente;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;
import com.ufcg.psoft.mercadofacil.service.impl.ClienteServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

	@Spy
	private Cliente clienteSpy;

	@Mock
	private ClienteDTO clienteDtoMock;

	private Optional<Cliente> clienteOp;

	private final Long idCliente = 1L;

	private Long idInexistente = 2L;

	private Long clienteDtoMockCpf = 10257965532L;

	@BeforeEach
	public void setup() {
		this.clienteSpy = geraCliente("João Victor", 12345678910L, 20, "Rua Bacana", TipoCliente.NORMAL);
		this.clienteOp = Optional.of(clienteSpy);
		Cliente cliente2 = geraCliente("Lucas Bento", 12377788800L, 37, "Rua Tal", TipoCliente.ESPECIAL);

		this.clientes = new ArrayList<Cliente>();
		this.clientes.add(clienteSpy);
		this.clientes.add(cliente2);
	}

	@Test
	public void getClienteByIdRetornaCliente() {
		setFindClienteExistente();

		assertEquals(clienteSpy, clienteService.getClienteById(idCliente));
		verify(clienteRepository, times(1)).findById(idCliente);
	}

	@Test
	public void getClienteByIdInexistenteRetornaErro() {
		setFindClienteInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.getClienteById(idInexistente));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_ID, erro.getMessage());
		verify(clienteRepository, times(1)).findById(idInexistente);
	}

	@Test
	public void getClienteByCpfRetornaCliente() {
		Long cpf = clienteSpy.getCpf();
		when(clienteRepository.findByCpf(cpf))
				.thenReturn(clienteOp);

		assertEquals(clienteSpy, clienteService.getClienteByCpf(cpf));
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
	public void testRemoveCliente() {
		setFindClienteExistente();

		clienteService.removeCliente(idCliente);

		verify(clienteRepository, times(1)).findById(idCliente);
		verify(clienteRepository, times(1)).delete(clienteSpy);
		verify(carrinhoService, times(1)).removeCarrinho(clienteSpy.getCpf());
	}

	@Test
	public void removeClienteIdInexistenteRetornaErro() {
		setFindClienteInexistente();

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
	public void cadastraClienteNormalRetornaClienteCadastrado() {
		setClienteDtoMock("Zé Carlos", clienteDtoMockCpf, 53, "Rua Coala", "normal");

		Cliente clienteCadastrado = clienteService.cadastraCliente(clienteDtoMock);

		assertEquals(clienteDtoMock.getNome(), clienteCadastrado.getNome());
		assertEquals(clienteDtoMock.getCpf(), clienteCadastrado.getCpf());
		assertEquals(clienteDtoMock.getIdade(), clienteCadastrado.getIdade());
		assertEquals(clienteDtoMock.getEndereco(), clienteCadastrado.getEndereco());
		assertEquals(TipoCliente.NORMAL, clienteCadastrado.getTipo());

		verify(clienteRepository, times(1)).existsByCpf(clienteDtoMockCpf);
		verify(carrinhoService, times(1)).cadastraCarrinho(clienteDtoMockCpf);
		verify(clienteRepository, times(1)).save(clienteCadastrado);
	}

	@Test
	public void cadastraClienteEspecialRetornaClienteCadastrado() {
		setClienteDtoMock("Zé Carlos", clienteDtoMockCpf, 53, "Rua Coala", "ESPECIAL");

		Cliente clienteCadastrado = clienteService.cadastraCliente(clienteDtoMock);

		assertEquals(clienteDtoMock.getNome(), clienteCadastrado.getNome());
		assertEquals(clienteDtoMock.getCpf(), clienteCadastrado.getCpf());
		assertEquals(clienteDtoMock.getIdade(), clienteCadastrado.getIdade());
		assertEquals(clienteDtoMock.getEndereco(), clienteCadastrado.getEndereco());
		assertEquals(TipoCliente.ESPECIAL, clienteCadastrado.getTipo());

		verify(clienteRepository, times(1)).existsByCpf(clienteDtoMockCpf);
		verify(carrinhoService, times(1)).cadastraCarrinho(clienteDtoMockCpf);
		verify(clienteRepository, times(1)).save(clienteCadastrado);
	}

	@Test
	public void cadastraClientePremiumRetornaClienteCadastrado() {
		setClienteDtoMock("Zé Carlos", clienteDtoMockCpf, 53, "Rua Coala", "Premium");

		Cliente clienteCadastrado = clienteService.cadastraCliente(clienteDtoMock);

		assertEquals(clienteDtoMock.getNome(), clienteCadastrado.getNome());
		assertEquals(clienteDtoMock.getCpf(), clienteCadastrado.getCpf());
		assertEquals(clienteDtoMock.getIdade(), clienteCadastrado.getIdade());
		assertEquals(clienteDtoMock.getEndereco(), clienteCadastrado.getEndereco());
		assertEquals(TipoCliente.PREMIUM, clienteCadastrado.getTipo());

		verify(clienteRepository, times(1)).existsByCpf(clienteDtoMockCpf);
		verify(carrinhoService, times(1)).cadastraCarrinho(clienteDtoMockCpf);
		verify(clienteRepository, times(1)).save(clienteCadastrado);
	}

	@Test
	public void cadastraClienteComTipoInexistenteRetornaErro() {
		when(clienteDtoMock.getCpf()).thenReturn(clienteDtoMockCpf);
		when(clienteDtoMock.getTipo()).thenReturn("TipoInexistente");

		setExistsByCpf(clienteDtoMockCpf, false);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.cadastraCliente(clienteDtoMock));

		assertEquals(ErroCliente.TIPO_CLIENTE_INVALIDO, erro.getMessage());

		verifyNoMoreInteractions(clienteDtoMock);
		verify(carrinhoService, never()).cadastraCarrinho(any());
		verify(clienteRepository, never()).save(any());
	}

	@Test
	public void cadastraClienteCpfJaExistenteRetornaErro() {
		when(clienteDtoMock.getCpf()).thenReturn(clienteDtoMockCpf);

		setExistsByCpf(clienteDtoMockCpf, true);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.cadastraCliente(clienteDtoMock));

		assertEquals(ErroCliente.CLIENTE_JA_CADASTRADO, erro.getMessage());

		verifyNoMoreInteractions(clienteDtoMock);
		verify(carrinhoService, never()).cadastraCarrinho(any());
		verify(clienteRepository, never()).save(any());
	}

	@Test
	public void atualizaClienteRetornaClienteAtualizado() {
		Long clienteCpf = clienteSpy.getCpf();
		setClienteDtoMock("Maria", clienteCpf, 22, "Rua Massa", "premium");
		setFindClienteExistente();

		Cliente clienteAtualizado = clienteService.atualizaCliente(idCliente, clienteDtoMock);

		assertEquals(clienteCpf, clienteAtualizado.getCpf());
		assertEquals(clienteDtoMock.getNome(), clienteAtualizado.getNome());
		assertEquals(clienteDtoMock.getIdade(), clienteAtualizado.getIdade());
		assertEquals(clienteDtoMock.getEndereco(), clienteAtualizado.getEndereco());
		assertEquals(TipoCliente.PREMIUM, clienteAtualizado.getTipo());

		verify(clienteRepository, times(1)).save(clienteSpy);
	}

	@Test
	public void atualizaClienteIdInexistenteRetornaErro() {
		setFindClienteInexistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.atualizaCliente(idInexistente, clienteDtoMock));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_ID, erro.getMessage());

		verifyNoInteractions(clienteDtoMock);
		verify(clienteRepository, never()).save(any());
	}

	@Test
	public void atualizaClienteCpfInexistenteRetornaErro() {
		when(clienteDtoMock.getCpf()).thenReturn(clienteDtoMockCpf);
		setFindClienteExistente();

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.atualizaCliente(idCliente, clienteDtoMock));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_CPF, erro.getMessage());

		verifyNoMoreInteractions(clienteDtoMock);
		verify(clienteRepository, never()).save(any());
	}

	@Test
	public void assertExisteClienteById() {
		setExistsById(idCliente, true);

		clienteService.assertExisteClienteById(idCliente);
	}

	@Test
	public void assertExisteClienteByIdRetornaErro() {
		setExistsById(idInexistente, false);

		CustomErrorType erro = assertThrows(CustomErrorType.class,
				() -> clienteService.assertExisteClienteById(idInexistente));

		assertEquals(ErroCliente.CLIENTE_NAO_CADASTRADO_ID, erro.getMessage());
	}

	private Cliente geraCliente(String nome, Long cpf, int idade, String endereco, TipoCliente tipo) {
		return Cliente.builder()
				.nome(nome)
				.cpf(cpf)
				.idade(idade)
				.endereco(endereco)
				.tipo(tipo)
				.build();
	}

	private void setFindClienteExistente() {
		setFindById(idCliente, clienteOp);
	}

	private void setFindClienteInexistente() {
		setFindById(idInexistente, Optional.empty());
	}

	private void setFindById(Long id, Optional<Cliente> retorno) {
		when(clienteRepository.findById(id))
				.thenReturn(retorno);
	}

	private void setExistsByCpf(Long cpf, boolean valor) {
		when(clienteRepository.existsByCpf(cpf)).thenReturn(valor);
	}

	private void setExistsById(Long id, boolean valor) {
		when(clienteRepository.existsById(id)).thenReturn(valor);
	}

	private void setClienteDtoMock(String nome, Long cpf, int idade, String endereco, String tipo) {
		when(clienteDtoMock.getNome()).thenReturn(nome);
		when(clienteDtoMock.getCpf()).thenReturn(cpf);
		when(clienteDtoMock.getIdade()).thenReturn(idade);
		when(clienteDtoMock.getEndereco()).thenReturn(endereco);
		when(clienteDtoMock.getTipo()).thenReturn(tipo);
	}

}
