package br.com.guilda.dto;

public class PedidoAtualizacaoAventureiro {
    private String nome;
    private String classe;
    private Integer nivel;

    public String getNome() { return nome; }
    public void setNome(String novoNome) { this.nome = novoNome; }

    public String getClasse() { return classe; }
    public void setClasse(String novaClasse) { this.classe = novaClasse; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer novoNivel) { this.nivel = novoNivel; }
}

