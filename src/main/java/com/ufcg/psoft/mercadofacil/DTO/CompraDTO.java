package com.ufcg.psoft.mercadofacil.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class CompraDTO {
    
    @Setter
    private String formaDePagamento;

    private EntregaDTO entrega;

}
