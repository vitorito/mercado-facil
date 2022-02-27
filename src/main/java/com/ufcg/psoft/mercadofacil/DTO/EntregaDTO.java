package com.ufcg.psoft.mercadofacil.DTO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EntregaDTO {

    private String TipoEntrega;

    private double distanciaEmKm;

    private String destino;
    
}
