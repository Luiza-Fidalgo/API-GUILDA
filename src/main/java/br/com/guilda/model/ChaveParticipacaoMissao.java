package br.com.guilda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChaveParticipacaoMissao implements Serializable {

    @Column(name = "missao_id")
    private Long missaoId;

    @Column(name = "aventureiro_id")
    private Long aventureiroId;

    public ChaveParticipacaoMissao() {
    }

    public ChaveParticipacaoMissao(Long idMissao, Long idAventureiro) {
        this.missaoId = idMissao;
        this.aventureiroId = idAventureiro;
    }

    public Long getMissaoId() {
        return missaoId;
    }

    public void setMissaoId(Long idMissao) {
        this.missaoId = idMissao;
    }

    public Long getAventureiroId() {
        return aventureiroId;
    }

    public void setAventureiroId(Long idAventureiro) {
        this.aventureiroId = idAventureiro;
    }

    @Override
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) return true;
        if (!(outroObjeto instanceof ChaveParticipacaoMissao outraChave)) return false;
        return Objects.equals(missaoId, outraChave.missaoId)
                && Objects.equals(aventureiroId, outraChave.aventureiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missaoId, aventureiroId);
    }
}

