package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizacoes", schema = "audit")
public class NucleoOrganizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<ContaUsuario> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<PerfilAcesso> roles = new ArrayList<>();

    public NucleoOrganizacao() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public List<ContaUsuario> getUsuarios() {
        return usuarios;
    }

    public List<PerfilAcesso> getRoles() {
        return roles;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

    public void setAtivo(Boolean novoStatus) {
        this.ativo = novoStatus;
    }
}

