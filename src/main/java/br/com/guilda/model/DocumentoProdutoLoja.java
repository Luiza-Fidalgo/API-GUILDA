package br.com.guilda.model;

import java.math.BigDecimal;

public class DocumentoProdutoLoja {

    private String nome;
    private String descricao;
    private String categoria;
    private String raridade;
    private BigDecimal preco;

    public DocumentoProdutoLoja() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String novaCategoria) {
        this.categoria = novaCategoria;
    }

    public String getRaridade() {
        return raridade;
    }

    public void setRaridade(String novaRaridade) {
        this.raridade = novaRaridade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal novoPreco) {
        this.preco = novoPreco;
    }
}

