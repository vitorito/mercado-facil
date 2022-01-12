package com.ufcg.psoft.mercadofacil.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClienteDTO {
	
	@NotEmpty
	private String nome;

	@NotNull
	@Positive
	private Long cpf;

	@NotNull
	@Positive
	private Integer idade;

	@NotEmpty
	private String endereco;

}
