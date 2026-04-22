package br.com.guilda.dto;

public class RespostaUltimaMissao {

    private Long id;
    private String titulo;
    private String status;
    private String nivelPerigo;

    public RespostaUltimaMissao() {
    }

    public RespostaUltimaMissao(Long idMissao, String tituloMissao, String statusAtual, String perigoDaMissao) {
        this.id = idMissao;
        this.titulo = tituloMissao;
        this.status = statusAtual;
        this.nivelPerigo = perigoDaMissao;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getStatus() {
        return status;
    }

    public String getNivelPerigo() {
        return nivelPerigo;
    }
}

