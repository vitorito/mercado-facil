package com.ufcg.psoft.mercadofacil.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.exception.ApiExceptionHandler;
import com.ufcg.psoft.mercadofacil.exception.ErroCliente;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.service.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@WebMvcTest(ClienteApiController.class)
public class ClienteApiControllerTest {

	@Autowired
	private ApiExceptionHandler exceptionHandler;
	
	@Autowired
	private ClienteApiController clienteController;

	@MockBean
	private ClienteService clienteService;

	private Cliente cliente;

	private final Long idCliente = 1L;

	@BeforeEach
	public void setup() {
		standaloneSetup(this.clienteController, this.exceptionHandler);
		this.cliente = Cliente.builder()
			.nome("João Victor")
			.cpf(12345678910L)
			.idade(32)
			.endereco("Rua Bacana")
			.build();
	}

	@Test
	public void listaClientesRetornaListaUnitariaOk() {
		when(clienteService.listaClientes())
			.thenReturn(List.of(cliente));

		given()
			.contentType("application/json")
		.when()
			.get("/api/cliente")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("$.size", equalTo(1))
			.body("[0].cpf", equalTo(cliente.getCpf()));
	}

	@Test
	public void listaClientesRetornaListaVaziaOk() {
		when(clienteService.listaClientes())
			.thenReturn(List.of());

		given()
			.contentType("application/json")
		.when()
			.get("/api/cliente")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("$.size", equalTo(0));
	}

	@Test
	public void cadastraClienteRetornaClienteOk() {
		ClienteDTO clienteDTO = geraClienteDTO("Joao Victor", 12345678910L, 32, "Rua Bacana");

		when(clienteService.cadastraCliente(any(ClienteDTO.class)))
			.thenReturn(this.cliente);

		given()
			.contentType("application/json")
			.body(clienteDTO)
		.when()
			.post("api/cliente")
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.body("id", equalTo(cliente.getId()))
			.body("cpf", equalTo(cliente.getCpf()))
			.body("nome", equalTo(cliente.getNome()))
			.body("idade", equalTo(cliente.getIdade()))
			.body("endereco", equalTo(cliente.getEndereco()));
	}

	@Test
	public void cadastraClienteDtoInvalidoRetornaBadRequest() {
		ClienteDTO clienteDTO = geraClienteDTOInvalido();

		given()
			.contentType("application/json")
			.body(clienteDTO)
		.when()
			.post("api/cliente")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("message", equalTo("Erro na validação dos campos."))
			.body("details.itens.size", equalTo(4));
		
		verify(this.clienteService, never()).cadastraCliente(any());
	}

	@Test
	public void cosultaClienteRetornaClienteOk() {
		when(clienteService.getClienteById(idCliente))
			.thenReturn(cliente);

		given()
			.contentType("application/json")
		.when()
			.get("/api/cliente/{id}", idCliente)
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("cpf", equalTo(cliente.getCpf()));
	}

	@Test
	public void cosultaClienteInexistenteRetornaNotFound() {
		when(clienteService.getClienteById(idCliente))
			.thenThrow(ErroCliente.erroClienteNaoEncontradoId());

		given()
			.contentType("application/json")
		.when()
			.get("/api/cliente/{id}", idCliente)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("message", equalTo(ErroCliente.CLIENTE_NAO_CADASTRADO_ID));
	}

	@Test
	public void atualizaClienteRetornaClienteAtualizadoOk() {
		ClienteDTO clienteDTO = geraClienteDTO("Antonio Carlos", 12345678911L, 45, "Rua Treze");

		Cliente clienteAtualizado = Cliente.builder()
			.nome(clienteDTO.getNome())
			.cpf(12345678911L)
			.idade(clienteDTO.getIdade())
			.endereco(clienteDTO.getEndereco())
			.build();

		when(this.clienteService.atualizaCliente(eq(idCliente), any(ClienteDTO.class)))
			.thenReturn(clienteAtualizado);

		given()
			.contentType("application/json")
			.body(clienteDTO)
		.when()
			.patch("api/cliente/{id}", 1L)
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("id", equalTo(clienteAtualizado.getId()))
			.body("cpf", equalTo(clienteAtualizado.getCpf()))
			.body("nome", equalTo(clienteDTO.getNome()))
			.body("idade", equalTo(clienteDTO.getIdade()))
			.body("endereco", equalTo(clienteDTO.getEndereco()));
	}


	@Test
	public void atualizaClienteDtoInvalidoRetornaBadRequest() {
		ClienteDTO clienteDTO = geraClienteDTOInvalido();

		given()
			.contentType("application/json")
			.body(clienteDTO)
		.when()
			.patch("api/cliente/{id}", idCliente)
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("message", equalTo("Erro na validação dos campos."))
			.body("details.itens.size", equalTo(4));
		
		verify(this.clienteService, never()).atualizaCliente(eq(idCliente), any());
	}

	@Test
	public void atualizaClientdIdInexistenteRetornaNotFound() {
		ClienteDTO clienteDTO = geraClienteDTO("Vasco de Sá", 12345678910L, 48, "Rua Bela");
		Long idInexistente = 2L;

		when(this.clienteService.atualizaCliente(eq(idInexistente), any(ClienteDTO.class)))
			.thenThrow(ErroCliente.erroClienteNaoEncontradoId());

		given()
			.contentType("application/json")
			.body(clienteDTO)
		.when()
			.patch("api/cliente/{id}", idInexistente)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("message", equalTo(ErroCliente.CLIENTE_NAO_CADASTRADO_ID));	
	}

	@Test
	public void atualizaClienteCpfInexistenteRetornaNotFound() {
		ClienteDTO clienteDTO = geraClienteDTO("Vasco de Sá", 12300078900L, 48, "Rua Bela");

		when(this.clienteService.atualizaCliente(eq(idCliente), any(ClienteDTO.class)))
			.thenThrow(ErroCliente.erroClienteNaoEncontradoCPF());

		given()
			.contentType("application/json")
			.body(clienteDTO)
		.when()
			.patch("api/cliente/{id}", idCliente)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("message", equalTo(ErroCliente.CLIENTE_NAO_CADASTRADO_CPF));	
	}

	@Test
	public void removeClienteOk() {
		given()
			.contentType("application/json")
		.when()
			.delete("api/cliente/{id}", idCliente)
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value())
			.expect(r -> assertNull(r.getResponse().getContentType()));
	}

	@Test
	public void removeClienteIdInexistenteNotFound() {
		Long idInexistente = 2L;

		doThrow(ErroCliente.erroClienteNaoEncontradoId())
			.when(this.clienteService).removeCliente(idInexistente);

		given()
			.contentType("application/json")
		.when()
			.delete("api/cliente/{id}", idInexistente)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("message", equalTo(ErroCliente.CLIENTE_NAO_CADASTRADO_ID));	
	}

	private ClienteDTO geraClienteDTOInvalido() {
		return geraClienteDTO("", null, null, "");
	}

	private ClienteDTO geraClienteDTO(String nome, Long cpf, Integer idade, String endereco) {
		return ClienteDTO.builder()
			.nome(nome)
			.cpf(cpf)
			.idade(idade)
			.endereco(endereco)
			.build();
	}

}
