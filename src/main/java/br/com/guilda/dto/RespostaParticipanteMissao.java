package br.com.guilda.dto;

import java.math.BigDecimal;

public class RespostaParticipanteMissao {

    private Long aventureiroId;
    private String nome;
    private String classe;
    private Integer nivel;
    private String papelMissao;
    private BigDecimal recompensaOuro;
    private Boolean destaque;

    public RespostaParticipanteMissao() {
    }

    public RespostaParticipanteMissao(Long idAventureiro,
                                      String nomeAventureiro,
                                      String classeAventureiro,
                                      Integer nivelAventureiro,
                                      String papelNaMissao,
                                      BigDecimal recompensaRecebida,
                                      Boolean ficouEmDestaque) {
        this.aventureiroId = idAventureiro;
        this.nome = nomeAventureiro;
        this.classe = classeAventureiro;
        this.nivel = nivelAventureiro;
        this.papelMissao = papelNaMissao;
        this.recompensaOuro = recompensaRecebida;
        this.destaque = ficouEmDestaque;
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

    public Integer getNivel() {
        return nivel;
    }

    public String getPapelMissao() {
        return papelMissao;
    }

    public BigDecimal getRecompensaOuro() {
        return recompensaOuro;
    }

    public Boolean getDestaque() {
        return destaque;
    }
}

