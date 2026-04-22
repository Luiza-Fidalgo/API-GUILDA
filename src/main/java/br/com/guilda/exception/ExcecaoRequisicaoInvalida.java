package br.com.guilda.exception;

import java.util.List;

public class ExcecaoRequisicaoInvalida extends RuntimeException {
    private final List<String> details;

    public ExcecaoRequisicaoInvalida(String mensagem, List<String> detalhes) {
        super(mensagem);
        this.details = detalhes;
    }

    public List<String> getDetails() {
        return details;
    }
}

