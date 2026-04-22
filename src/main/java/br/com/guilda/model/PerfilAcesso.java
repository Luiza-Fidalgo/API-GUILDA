package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "audit")
public class PerfilAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private NucleoOrganizacao organizacao;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<VinculoUsuarioPerfil> userRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<VinculoPerfilPermissao> rolePermissions = new LinkedHashSet<>();

    public PerfilAcesso() {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime criadoEm) {
        this.createdAt = criadoEm;
    }

    public Set<VinculoUsuarioPerfil> getUserRoles() {
        return userRoles;
    }

    public Set<VinculoPerfilPermissao> getRolePermissions() {
        return rolePermissions;
    }
}

