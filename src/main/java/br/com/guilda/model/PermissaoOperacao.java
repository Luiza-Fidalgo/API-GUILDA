package br.com.guilda.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "permissions", schema = "audit")
public class PermissaoOperacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 80)
    private String code;

    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    private Set<VinculoPerfilPermissao> rolePermissions = new LinkedHashSet<>();

    public PermissaoOperacao() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String novoCodigo) {
        this.code = novoCodigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    public Set<VinculoPerfilPermissao> getRolePermissions() {
        return rolePermissions;
    }
}

