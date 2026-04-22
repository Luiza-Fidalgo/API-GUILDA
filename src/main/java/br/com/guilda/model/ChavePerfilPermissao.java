package br.com.guilda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChavePerfilPermissao implements Serializable {

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;

    public ChavePerfilPermissao() {
    }

    public ChavePerfilPermissao(Long idRole, Long idPermission) {
        this.roleId = idRole;
        this.permissionId = idPermission;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long idRole) {
        this.roleId = idRole;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long idPermission) {
        this.permissionId = idPermission;
    }

    @Override
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) return true;
        if (!(outroObjeto instanceof ChavePerfilPermissao outraChave)) return false;
        return Objects.equals(roleId, outraChave.roleId)
                && Objects.equals(permissionId, outraChave.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }
}

