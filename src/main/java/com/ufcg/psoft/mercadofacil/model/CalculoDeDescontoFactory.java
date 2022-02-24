package com.ufcg.psoft.mercadofacil.model;

public abstract class CalculoDeDescontoFactory {
    
    public static CalculoDeDescontoPorTipoCliente create(TipoCliente tipoCliente) {
        CalculoDeDescontoPorTipoCliente calculoDesconto = null;

        if (tipoCliente.equals(TipoCliente.NORMAL))
            calculoDesconto  = new CalculoDeDescontoClienteNormal();

        if (tipoCliente.equals(TipoCliente.ESPECIAL))
            calculoDesconto = new CalculoDeDescontoClienteEspecial();

        if (tipoCliente.equals(TipoCliente.PREMIUM))
            calculoDesconto = new CalculoDeDescontoClientePremium();

        return calculoDesconto;
    }

}
