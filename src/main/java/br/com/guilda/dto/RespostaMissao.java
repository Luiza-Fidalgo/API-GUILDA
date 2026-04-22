package br.com.guilda.dto;

public class RespostaMissao {

    private Long id;
    private Long organizacaoId;
    private String titulo;
    private String nivelPerigo;
    private String status;
    private String createdAt;
    private String dataInicio;
    private String dataFim;

    public RespostaMissao() {
    }

    public RespostaMissao(Long idMissao,
                          Long idOrganizacao,
                          String tituloMissao,
                          String perigoDaMissao,
                          String statusAtual,
                          String criadaEm,
                          String inicioPlanejado,
                          String fimPlanejado) {
        this.id = idMissao;
        this.organizacaoId = idOrganizacao;
        this.titulo = tituloMissao;
        this.nivelPerigo = perigoDaMissao;
        this.status = statusAtual;
        this.createdAt = criadaEm;
        this.dataInicio = inicioPlanejado;
        this.dataFim = fimPlanejado;
    }

    public Long getId() {
        return id;
    }

    public Long getOrganizacaoId() {
        return organizacaoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNivelPerigo() {
        return nivelPerigo;
    }

    public String getStatus() {
        return status;
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
}

