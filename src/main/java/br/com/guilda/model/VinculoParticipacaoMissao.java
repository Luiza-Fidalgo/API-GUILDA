package br.com.guilda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "participacoes_missao",
        schema = "aventura",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_participacao_missao_aventureiro", columnNames = {"missao_id", "aventureiro_id"})
        }
)
public class VinculoParticipacaoMissao {

    @EmbeddedId
    private ChaveParticipacaoMissao id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id", nullable = false)
    private ContratoMissao missao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("aventureiroId")
    @JoinColumn(name = "aventureiro_id", nullable = false)
    private IntegranteAventureiro aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel_missao", nullable = false, length = 30)
    private TipoPapelMissao papelMissao;

    @Column(name = "recompensa_ouro", precision = 10, scale = 2)
    private BigDecimal recompensaOuro;

    @Column(nullable = false)
    private Boolean destaque;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public VinculoParticipacaoMissao() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public ChaveParticipacaoMissao getId() {
        return id;
    }

    public void setId(ChaveParticipacaoMissao novaChave) {
        this.id = novaChave;
    }

    public ContratoMissao getMissao() {
        return missao;
    }

    public void setMissao(ContratoMissao missaoAssociada) {
        this.missao = missaoAssociada;
    }

    public IntegranteAventureiro getAventureiro() {
        return aventureiro;
    }

    public void setAventureiro(IntegranteAventureiro aventureiroAssociado) {
        this.aventureiro = aventureiroAssociado;
    }

    public TipoPapelMissao getPapelMissao() {
        return papelMissao;
    }

    public void setPapelMissao(TipoPapelMissao novoPapel) {
        this.papelMissao = novoPapel;
    }

    public BigDecimal getRecompensaOuro() {
        return recompensaOuro;
    }

    public void setRecompensaOuro(BigDecimal novaRecompensa) {
        this.recompensaOuro = novaRecompensa;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean novoDestaque) {
        this.destaque = novoDestaque;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

