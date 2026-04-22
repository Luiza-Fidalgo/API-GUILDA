package br.com.guilda.dto;

public class PedidoCriacaoMissao {

    private String titulo;
    private String nivelPerigo;

    public PedidoCriacaoMissao() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String novoTitulo) {
        this.titulo = novoTitulo;
    }

    public String getNivelPerigo() {
        return nivelPerigo;
    }

    public void setNivelPerigo(String novoNivelPerigo) {
        this.nivelPerigo = novoNivelPerigo;
    }
}

