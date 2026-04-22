package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "aventureiros", schema = "aventura")
public class IntegranteAventureiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private NucleoOrganizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_cadastro_id", nullable = false)
    private ContaUsuario usuarioCadastro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoClasseAventureiro classe;

    @Column(nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(mappedBy = "aventureiro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AliadoCompanheiro companheiro;

    @OneToMany(mappedBy = "aventureiro", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private java.util.Set<VinculoParticipacaoMissao> participacoes = new java.util.LinkedHashSet<>();

    public java.util.Set<VinculoParticipacaoMissao> getParticipacoes() {
        return participacoes;
    }

    public IntegranteAventureiro() {
    }

    public IntegranteAventureiro(Long idAventureiro,
                       String nomeAventureiro,
                       TipoClasseAventureiro classeAventureiro,
                       Integer nivelAventureiro,
                       Boolean ativoNaGuilda) {
        this.id = idAventureiro;
        this.nome = nomeAventureiro;
        this.classe = classeAventureiro;
        this.nivel = nivelAventureiro;
        this.ativo = ativoNaGuilda;
    }

    @PrePersist
    public void prePersist() {
        OffsetDateTime momentoAtual = OffsetDateTime.now();
        this.createdAt = momentoAtual;
        this.updatedAt = momentoAtual;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
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

    public ContaUsuario getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(ContaUsuario usuarioResponsavel) {
        this.usuarioCadastro = usuarioResponsavel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

    public TipoClasseAventureiro getClasse() {
        return classe;
    }

    public void setClasse(TipoClasseAventureiro novaClasse) {
        this.classe = novaClasse;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer novoNivel) {
        this.nivel = novoNivel;
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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AliadoCompanheiro getCompanheiro() {
        return companheiro;
    }

    public void setCompanheiro(AliadoCompanheiro companheiroAtual) {
        this.companheiro = companheiroAtual;

        if (companheiroAtual != null) {
            companheiroAtual.setAventureiro(this);
        }
    }
}

