package br.com.guilda.dto;

import java.math.BigDecimal;

public class RespostaRankingParticipacao {

    private Long aventureiroId;
    private String nome;
    private String classe;
    private Long totalParticipacoes;
    private BigDecimal totalRecompensas;
    private Long totalDestaques;

    public RespostaRankingParticipacao(Long idAventureiro,
                                       String nomeAventureiro,
                                       String classeAventureiro,
                                       Long quantidadeParticipacoes,
                                       BigDecimal somaRecompensas,
                                       Long quantidadeDestaques) {
        this.aventureiroId = idAventureiro;
        this.nome = nomeAventureiro;
        this.classe = classeAventureiro;
        this.totalParticipacoes = quantidadeParticipacoes;
        this.totalRecompensas = somaRecompensas;
        this.totalDestaques = quantidadeDestaques;
    }

    public Long getAventureiroId() {
        return aventureiroId;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public Long getTotalParticipacoes() {
        return totalParticipacoes;
    }

    public BigDecimal getTotalRecompensas() {
        return totalRecompensas;
    }

    public Long getTotalDestaques() {
        return totalDestaques;
    }
}

