package br.com.guilda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "role_permissions", schema = "audit")
public class VinculoPerfilPermissao {

    @EmbeddedId
    private ChavePerfilPermissao id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private PerfilAcesso role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", nullable = false)
    private PermissaoOperacao permission;

    public VinculoPerfilPermissao() {
    }

    public ChavePerfilPermissao getId() {
        return id;
    }

    public void setId(ChavePerfilPermissao novaChave) {
        this.id = novaChave;
    }

    public PerfilAcesso getRole() {
        return role;
    }

    public void setRole(PerfilAcesso roleAssociada) {
        this.role = roleAssociada;
    }

    public PermissaoOperacao getPermission() {
        return permission;
    }

    public void setPermission(PermissaoOperacao permissaoAssociada) {
        this.permission = permissaoAssociada;
    }
}

