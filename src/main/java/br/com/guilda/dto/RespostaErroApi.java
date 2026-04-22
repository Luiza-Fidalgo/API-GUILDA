package br.com.guilda.dto;

import java.util.List;

public class RespostaErroApi {
    private String mensagem;
    private List<String> detalhes;

    public RespostaErroApi(String mensagemPrincipal, List<String> detalhesErro) {
        this.mensagem = mensagemPrincipal;
        this.detalhes = detalhesErro;
    }

    public String getMensagem() { return mensagem; }
    public List<String> getDetalhes() { return detalhes; }
}

