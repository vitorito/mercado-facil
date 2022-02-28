package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.EntregaDTO;
import com.ufcg.psoft.mercadofacil.model.CalculoDeEntregaPorTipoTransporte;
import com.ufcg.psoft.mercadofacil.model.Entrega;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface EntregaService {

    Entrega geraEntrega(EntregaDTO entrega);

    CalculoDeEntregaPorTipoTransporte getCalculoTransporte(List<Produto> produtos);
    
}
