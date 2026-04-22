package br.com.guilda.dto;

import java.util.List;

public class RespostaDetalheMissao {

    private Long id;
    private String titulo;
    private String status;
    private String nivelPerigo;
    private String createdAt;
    private String dataInicio;
    private String dataFim;
    private List<RespostaParticipanteMissao> participantes;

    public RespostaDetalheMissao() {
    }

    public RespostaDetalheMissao(Long idMissao,
                                 String tituloMissao,
                                 String statusAtual,
                                 String perigoDaMissao,
                                 String criadaEm,
                                 String inicioPlanejado,
                                 String fimPlanejado,
                                 List<RespostaParticipanteMissao> participantesAtuais) {
        this.id = idMissao;
        this.titulo = tituloMissao;
        this.status = statusAtual;
        this.nivelPerigo = perigoDaMissao;
        this.createdAt = criadaEm;
        this.dataInicio = inicioPlanejado;
        this.dataFim = fimPlanejado;
        this.participantes = participantesAtuais;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public List<RespostaParticipanteMissao> getParticipantes() {
        return participantes;
    }
}

