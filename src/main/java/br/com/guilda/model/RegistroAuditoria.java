package br.com.guilda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_entries", schema = "audit")
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private NucleoOrganizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private ContaUsuario actorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id")
    private ChaveApiAuditoria actorApiKey;

    @Column(name = "action", nullable = false, length = 30)
    private String action;

    @Column(name = "entity_schema", nullable = false, length = 60)
    private String entitySchema;

    @Column(name = "entity_name", nullable = false, length = 80)
    private String entityName;

    @Column(name = "entity_id", length = 80)
    private String entityId;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @Column(name = "ip")
    private String ip;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "diff", columnDefinition = "jsonb")
    private String diff;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "success", nullable = false)
    private Boolean success;

    public RegistroAuditoria() {
    }
}
