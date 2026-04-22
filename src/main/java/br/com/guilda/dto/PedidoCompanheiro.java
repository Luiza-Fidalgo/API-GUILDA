package br.com.guilda.dto;

public class PedidoCompanheiro {
    private String nome;
    private String especie;
    private Integer lealdade;

    public String getNome() { return nome; }
    public void setNome(String novoNome) { this.nome = novoNome; }

    public String getEspecie() { return especie; }
    public void setEspecie(String novaEspecie) { this.especie = novaEspecie; }

    public Integer getLealdade() { return lealdade; }
    public void setLealdade(Integer novaLealdade) { this.lealdade = novaLealdade; }
}

