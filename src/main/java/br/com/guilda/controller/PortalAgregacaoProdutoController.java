package br.com.guilda.controller;

import br.com.guilda.dto.RespostaAgrupamentoQuantidade;
import br.com.guilda.dto.RespostaFaixaPreco;
import br.com.guilda.dto.RespostaPrecoMedio;
import br.com.guilda.service.ServicoBuscaProdutoLoja;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos/agregacoes")
public class PortalAgregacaoProdutoController {

    private final ServicoBuscaProdutoLoja produtosService;

    public PortalAgregacaoProdutoController(ServicoBuscaProdutoLoja service) {
        this.produtosService = service;
    }

    @GetMapping("/por-categoria")
    public ResponseEntity<List<RespostaAgrupamentoQuantidade>> agruparPorCategoria() throws IOException {
        return ResponseEntity.ok(produtosService.agruparPorCategoria());
    }

    @GetMapping("/por-raridade")
    public ResponseEntity<List<RespostaAgrupamentoQuantidade>> agruparPorRaridade() throws IOException {
        return ResponseEntity.ok(produtosService.agruparPorRaridade());
    }

    @GetMapping("/preco-medio")
    public ResponseEntity<RespostaPrecoMedio> calcularPrecoMedio() throws IOException {
        return ResponseEntity.ok(produtosService.calcularPrecoMedio());
    }

    @GetMapping("/faixas-preco")
    public ResponseEntity<List<RespostaFaixaPreco>> agruparPorFaixaDePreco() throws IOException {
        return ResponseEntity.ok(produtosService.agruparPorFaixaDePreco());
    }
}

