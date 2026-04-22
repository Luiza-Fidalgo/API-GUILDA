package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", schema = "audit")
public class ContaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private NucleoOrganizacao organizacao;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "email", nullable = false, length = 180)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "ultimo_login_em")
    private OffsetDateTime ultimoLoginEm;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Set<VinculoUsuarioPerfil> userRoles = new LinkedHashSet<>();

    public ContaUsuario() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String novoEmail) {
        this.email = novoEmail;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String novoHashSenha) {
        this.senhaHash = novoHashSenha;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String novoStatus) {
        this.status = novoStatus;
    }

    public OffsetDateTime getUltimoLoginEm() {
        return ultimoLoginEm;
    }

    public void setUltimoLoginEm(OffsetDateTime ultimoLoginRegistrado) {
        this.ultimoLoginEm = ultimoLoginRegistrado;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime criadoEm) {
        this.createdAt = criadoEm;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime atualizadoEm) {
        this.updatedAt = atualizadoEm;
    }

    public Set<VinculoUsuarioPerfil> getUserRoles() {
        return userRoles;
    }
}

