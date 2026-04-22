package br.com.guilda.dto;

public class RespostaAgrupamentoQuantidade {

    private String chave;
    private Long quantidade;

    public RespostaAgrupamentoQuantidade() {
    }

    public RespostaAgrupamentoQuantidade(String chaveAgregada, Long totalEncontrado) {
        this.chave = chaveAgregada;
        this.quantidade = totalEncontrado;
    }

    public String getChave() {
        return chave;
    }

    public Long getQuantidade() {
        return quantidade;
    }
}

