package com.ufcg.psoft.mercadofacil.service.impl;

import java.util.List;

import com.ufcg.psoft.mercadofacil.DTO.EntregaDTO;
import com.ufcg.psoft.mercadofacil.model.CalculoDeEntregaComum;
import com.ufcg.psoft.mercadofacil.model.CalculoDeEntregaFragil;
import com.ufcg.psoft.mercadofacil.model.CalculoDeEntregaPorTipoTransporte;
import com.ufcg.psoft.mercadofacil.model.CalculoDeEntregaRefrigerada;
import com.ufcg.psoft.mercadofacil.model.Entrega;
import com.ufcg.psoft.mercadofacil.model.EntregaExpress;
import com.ufcg.psoft.mercadofacil.model.EntregaPadrao;
import com.ufcg.psoft.mercadofacil.model.EntregaRetirada;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.model.TipoEntrega;
import com.ufcg.psoft.mercadofacil.model.TipoTransporte;
import com.ufcg.psoft.mercadofacil.service.EntregaService;

import org.springframework.stereotype.Service;

@Service
public class EntregaServiceImpl implements EntregaService {

    @Override
    public Entrega geraEntrega(EntregaDTO entregaDTO) {
        TipoEntrega tipo = TipoEntrega.get(entregaDTO.getTipoEntrega());
        String destino = entregaDTO.getDestino();
        double distanciaEmKm = entregaDTO.getDistanciaEmKm();

        if (tipo.equals(TipoEntrega.PADRAO)) 
            return new EntregaPadrao(destino, distanciaEmKm);
        
        if (tipo.equals(TipoEntrega.EXPRESS)) 
            return new EntregaExpress(destino, distanciaEmKm);
        
        if (tipo.equals(TipoEntrega.RETIRADA))
            return new EntregaRetirada(destino, distanciaEmKm);

        return null;
    }

    @Override
    public CalculoDeEntregaPorTipoTransporte getCalculoTransporte(List<Produto> produtos) {
        boolean isFragil = false;

        for (Produto p: produtos) {
            if (p.getTipoTransporte().equals(TipoTransporte.REFRIGERADO)) 
                return new CalculoDeEntregaRefrigerada();
            
            if (!isFragil && p.getTipoTransporte().equals(TipoTransporte.FRAGIL)) 
                isFragil = true;
        }

        return isFragil ? new CalculoDeEntregaFragil() : new CalculoDeEntregaComum();
    }
    
}
