package com.ufcg.psoft.mercadofacil.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
