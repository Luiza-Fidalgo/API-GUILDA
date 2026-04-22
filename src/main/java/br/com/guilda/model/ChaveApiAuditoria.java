package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "api_keys", schema = "audit")
public class ChaveApiAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private NucleoOrganizacao organizacao;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "key_hash", nullable = false, length = 255)
    private String keyHash;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_used_at")
    private OffsetDateTime lastUsedAt;

    public ChaveApiAuditoria() {
    }

    public Long getId() {
        return id;
    }

    public NucleoOrganizacao getOrganizacao() {
        return organizacao;
    }

    public void setOrganizacao(NucleoOrganizacao organizacaoDona) {
        this.organizacao = organizacaoDona;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

    public String getKeyHash() {
        return keyHash;
    }

    public void setKeyHash(String novoHash) {
        this.keyHash = novoHash;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean novoStatus) {
        this.ativo = novoStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime criadoEm) {
        this.createdAt = criadoEm;
    }

    public OffsetDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(OffsetDateTime ultimoUso) {
        this.lastUsedAt = ultimoUso;
    }
}

