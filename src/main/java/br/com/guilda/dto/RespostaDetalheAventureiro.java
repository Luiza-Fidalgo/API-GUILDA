package br.com.guilda.dto;

public class RespostaDetalheAventureiro {

    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;
    private RespostaCompanheiro companheiro;
    private Integer totalParticipacoes;
    private RespostaUltimaMissao ultimaMissao;

    public RespostaDetalheAventureiro() {
    }

    public RespostaDetalheAventureiro(Long idAventureiro,
                                      String nomeAventureiro,
                                      String classeAventureiro,
                                      Integer nivelAventureiro,
                                      Boolean ativoNaGuilda,
                                      RespostaCompanheiro companheiroAtual,
                                      Integer quantidadeParticipacoes,
                                      RespostaUltimaMissao missaoMaisRecente) {
        this.id = idAventureiro;
        this.nome = nomeAventureiro;
        this.classe = classeAventureiro;
        this.nivel = nivelAventureiro;
        this.ativo = ativoNaGuilda;
        this.companheiro = companheiroAtual;
        this.totalParticipacoes = quantidadeParticipacoes;
        this.ultimaMissao = missaoMaisRecente;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public Integer getNivel() {
        return nivel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public RespostaCompanheiro getCompanheiro() {
        return companheiro;
    }

    public Integer getTotalParticipacoes() {
        return totalParticipacoes;
    }

    public RespostaUltimaMissao getUltimaMissao() {
        return ultimaMissao;
    }
}

