package com.ufcg.psoft.mercadofacil.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@AllArgsConstructor
public class CompraDTO {
    
    private String formaDePagamento;

    private EntregaDTO entrega;

}
