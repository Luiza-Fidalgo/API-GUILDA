package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "missoes", schema = "aventura")
public class ContratoMissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private NucleoOrganizacao organizacao;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false, length = 20)
    private TipoNivelPerigo nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoStatusMissao status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "data_inicio")
    private OffsetDateTime dataInicio;

    @Column(name = "data_fim")
    private OffsetDateTime dataFim;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.Set<VinculoParticipacaoMissao> participacoes = new java.util.LinkedHashSet<>();

    public java.util.Set<VinculoParticipacaoMissao> getParticipacoes() {
        return participacoes;
    }

    public ContratoMissao() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public NucleoOrganizacao getOrganizacao() {
        return organizacao;
    }

    public void setOrganizacao(NucleoOrganizacao organizacaoResponsavel) {
        this.organizacao = organizacaoResponsavel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String novoTitulo) {
        this.titulo = novoTitulo;
    }

    public TipoNivelPerigo getNivelPerigo() {
        return nivelPerigo;
    }

    public void setNivelPerigo(TipoNivelPerigo novoNivelPerigo) {
        this.nivelPerigo = novoNivelPerigo;
    }

    public TipoStatusMissao getStatus() {
        return status;
    }

    public void setStatus(TipoStatusMissao novoStatus) {
        this.status = novoStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(OffsetDateTime novoInicio) {
        this.dataInicio = novoInicio;
    }

    public OffsetDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(OffsetDateTime novoFim) {
        this.dataFim = novoFim;
    }
}

