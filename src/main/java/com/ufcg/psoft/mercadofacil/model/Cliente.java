package com.ufcg.psoft.mercadofacil.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;

	@NonNull
	@EqualsAndHashCode.Include
	private Long cpf;

	@NotEmpty
	private String nome;

	@NonNull
	private Integer idade;

	@NotEmpty
	private String endereco;

	@Builder
	private Cliente(Long cpf, String nome, Integer idade, String endereco) {
	this.cpf = cpf;
	this.nome = nome;
	this.idade = idade;
	this.endereco = endereco;
	}

}
