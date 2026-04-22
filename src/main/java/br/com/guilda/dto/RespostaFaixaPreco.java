package br.com.guilda.dto;

public class RespostaFaixaPreco {

    private String faixa;
    private Long quantidade;

    public RespostaFaixaPreco() {
    }

    public RespostaFaixaPreco(String faixaPreco, Long quantidadeItens) {
        this.faixa = faixaPreco;
        this.quantidade = quantidadeItens;
    }

    public String getFaixa() {
        return faixa;
    }

    public Long getQuantidade() {
        return quantidade;
    }
}

