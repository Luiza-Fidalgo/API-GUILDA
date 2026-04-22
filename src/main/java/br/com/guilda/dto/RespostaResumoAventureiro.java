package br.com.guilda.dto;

public class RespostaResumoAventureiro {
    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;

    public RespostaResumoAventureiro(Long idAventureiro,
                                     String nomeAventureiro,
                                     String classeAventureiro,
                                     Integer nivelAventureiro,
                                     Boolean ativoNaGuilda) {
        this.id = idAventureiro;
        this.nome = nomeAventureiro;
        this.classe = classeAventureiro;
        this.nivel = nivelAventureiro;
        this.ativo = ativoNaGuilda;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getClasse() { return classe; }
    public Integer getNivel() { return nivel; }
    public Boolean getAtivo() { return ativo; }
}

