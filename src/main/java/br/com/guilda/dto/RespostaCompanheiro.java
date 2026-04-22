package br.com.guilda.dto;

public class RespostaCompanheiro {
    private String nome;
    private String especie;
    private Integer lealdade;

    public RespostaCompanheiro(String nomeCompanheiro, String especieCompanheiro, Integer nivelLealdade) {
        this.nome = nomeCompanheiro;
        this.especie = especieCompanheiro;
        this.lealdade = nivelLealdade;
    }

    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public Integer getLealdade() { return lealdade; }
}

