package com.ufcg.psoft.mercadofacil.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

	@EqualsAndHashCode.Include
	private Long cpf;

	private String nome;

	private Integer idade;

	private String endereco;

	private TipoCliente tipo;

	@Builder
	private Cliente(Long cpf, String nome, Integer idade, String endereco, TipoCliente tipo) {
		this.cpf = cpf;
		this.nome = nome;
		this.idade = idade;
		this.endereco = endereco;
		this.tipo = tipo;
	}

}
