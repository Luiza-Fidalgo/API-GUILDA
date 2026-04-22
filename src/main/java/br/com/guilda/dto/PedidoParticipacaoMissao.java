package br.com.guilda.dto;

import java.math.BigDecimal;

public class PedidoParticipacaoMissao {

    private Long aventureiroId;
    private String papelMissao;
    private BigDecimal recompensaOuro;
    private Boolean destaque;

    public PedidoParticipacaoMissao() {
    }

    public Long getAventureiroId() {
        return aventureiroId;
    }

    public void setAventureiroId(Long idAventureiro) {
        this.aventureiroId = idAventureiro;
    }

    public String getPapelMissao() {
        return papelMissao;
    }

    public void setPapelMissao(String papelInformado) {
        this.papelMissao = papelInformado;
    }

    public BigDecimal getRecompensaOuro() {
        return recompensaOuro;
    }

    public void setRecompensaOuro(BigDecimal valorRecompensa) {
        this.recompensaOuro = valorRecompensa;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean destaqueInformado) {
        this.destaque = destaqueInformado;
    }
}

