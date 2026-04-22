package br.com.guilda.dto;

import java.math.BigDecimal;

public class RespostaProdutoLoja {

    private String nome;
    private String descricao;
    private String categoria;
    private String raridade;
    private BigDecimal preco;

    public RespostaProdutoLoja() {
    }

    public RespostaProdutoLoja(String nomeProduto,
                               String descricaoProduto,
                               String categoriaProduto,
                               String raridadeProduto,
                               BigDecimal precoProduto) {
        this.nome = nomeProduto;
        this.descricao = descricaoProduto;
        this.categoria = categoriaProduto;
        this.raridade = raridadeProduto;
        this.preco = precoProduto;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getRaridade() {
        return raridade;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}

