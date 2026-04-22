package br.com.guilda.dto;

import java.math.BigDecimal;

public class RespostaRelatorioMissao {

    private Long missaoId;
    private String titulo;
    private String status;
    private String nivelPerigo;
    private Long quantidadeParticipantes;
    private BigDecimal totalRecompensas;

    public RespostaRelatorioMissao(Long idMissao,
                                   String tituloMissao,
                                   String statusAtual,
                                   String perigoDaMissao,
                                   Long totalParticipantes,
                                   BigDecimal somaRecompensas) {
        this.missaoId = idMissao;
        this.titulo = tituloMissao;
        this.status = statusAtual;
        this.nivelPerigo = perigoDaMissao;
        this.quantidadeParticipantes = totalParticipantes;
        this.totalRecompensas = somaRecompensas;
    }

    public Long getMissaoId() {
        return missaoId;
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

    public Long getQuantidadeParticipantes() {
        return quantidadeParticipantes;
    }

    public BigDecimal getTotalRecompensas() {
        return totalRecompensas;
    }
}

