package br.com.guilda.dto;

import java.math.BigDecimal;

public class RespostaPrecoMedio {

    private BigDecimal precoMedio;

    public RespostaPrecoMedio() {
    }

    public RespostaPrecoMedio(BigDecimal valorMedio) {
        this.precoMedio = valorMedio;
    }

    public BigDecimal getPrecoMedio() {
        return precoMedio;
    }
}

