package br.com.guilda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "companheiros", schema = "aventura")
public class AliadoCompanheiro {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "aventureiro_id")
    private IntegranteAventureiro aventureiro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoEspecieCompanheiro especie;

    @Column(nullable = false)
    @jakarta.validation.constraints.Min(0)
    @jakarta.validation.constraints.Max(100)
    private Integer lealdade;

    public AliadoCompanheiro() {
    }

    public AliadoCompanheiro(String nomeCompanheiro, TipoEspecieCompanheiro especieCompanheiro, Integer nivelLealdade) {
        this.nome = nomeCompanheiro;
        this.especie = especieCompanheiro;
        this.lealdade = nivelLealdade;
    }

    public Long getId() {
        return id;
    }

    public IntegranteAventureiro getAventureiro() {
        return aventureiro;
    }

    public void setAventureiro(IntegranteAventureiro donoCompanheiro) {
        this.aventureiro = donoCompanheiro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

    public TipoEspecieCompanheiro getEspecie() {
        return especie;
    }

    public void setEspecie(TipoEspecieCompanheiro novaEspecie) {
        this.especie = novaEspecie;
    }

    public Integer getLealdade() {
        return lealdade;
    }

    public void setLealdade(Integer novaLealdade) {
        this.lealdade = novaLealdade;
    }
}

