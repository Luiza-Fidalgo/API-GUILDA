package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_roles", schema = "audit")
public class VinculoUsuarioPerfil {

    @EmbeddedId
    private ChaveUsuarioPerfil id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", nullable = false)
    private ContaUsuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private PerfilAcesso role;

    @Column(name = "granted_at", nullable = false)
    private OffsetDateTime grantedAt;

    public VinculoUsuarioPerfil() {
    }

    public ChaveUsuarioPerfil getId() {
        return id;
    }

    public void setId(ChaveUsuarioPerfil novaChave) {
        this.id = novaChave;
    }

    public ContaUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(ContaUsuario usuarioAssociado) {
        this.usuario = usuarioAssociado;
    }

    public PerfilAcesso getRole() {
        return role;
    }

    public void setRole(PerfilAcesso roleAssociada) {
        this.role = roleAssociada;
    }

    public OffsetDateTime getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(OffsetDateTime concedidaEm) {
        this.grantedAt = concedidaEm;
    }
}

