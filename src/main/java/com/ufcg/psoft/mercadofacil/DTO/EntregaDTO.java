package com.ufcg.psoft.mercadofacil.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class EntregaDTO {

    private String tipoEntrega;

    private double distanciaEmKm;

    private String destino;
    
}
