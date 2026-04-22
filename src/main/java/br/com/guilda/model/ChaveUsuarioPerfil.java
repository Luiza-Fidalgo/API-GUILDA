package br.com.guilda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChaveUsuarioPerfil implements Serializable {

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "role_id")
    private Long roleId;

    public ChaveUsuarioPerfil() {
    }

    public ChaveUsuarioPerfil(Long idUsuario, Long idRole) {
        this.usuarioId = idUsuario;
        this.roleId = idRole;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long idUsuario) {
        this.usuarioId = idUsuario;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long idRole) {
        this.roleId = idRole;
    }

    @Override
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) return true;
        if (!(outroObjeto instanceof ChaveUsuarioPerfil outraChave)) return false;
        return Objects.equals(usuarioId, outraChave.usuarioId)
                && Objects.equals(roleId, outraChave.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, roleId);
    }
}

