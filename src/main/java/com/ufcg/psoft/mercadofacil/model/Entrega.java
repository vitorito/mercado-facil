package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public abstract class Entrega {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destino;

    private double distanciaEmKm;

    @Enumerated(EnumType.STRING)
    private TipoEntrega tipo;

    protected Entrega(String destino, double distanciaEmKm, TipoEntrega tipo) {
        this.destino = destino;
        this.distanciaEmKm = distanciaEmKm;
        this.tipo = tipo;
    }

   public  abstract BigDecimal calculaCustoEntrega(CalculoDeEntregaPorTipoTransporte calculo);
    
}