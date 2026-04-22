package br.com.guilda.controller;

import br.com.guilda.dto.RespostaProdutoLoja;
import br.com.guilda.service.ServicoBuscaProdutoLoja;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos/busca")
public class PortalBuscaProdutoController {

    private final ServicoBuscaProdutoLoja produtosService;

    public PortalBuscaProdutoController(ServicoBuscaProdutoLoja service) {
        this.produtosService = service;
    }

    @GetMapping("/nome")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarPorNome(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtosService.buscarPorNome(termo));
    }

    @GetMapping("/descricao")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarPorDescricao(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtosService.buscarPorDescricao(termo));
    }

    @GetMapping("/frase")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarPorFrase(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtosService.buscarPorFraseExata(termo));
    }

    @GetMapping("/fuzzy")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarFuzzy(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtosService.buscarFuzzyPorNome(termo));
    }

    @GetMapping("/multicampos")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarMulticampos(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtosService.buscarEmMultiplosCampos(termo));
    }

    @GetMapping("/com-filtro")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarComFiltro(@RequestParam String termo,
                                                                     @RequestParam String categoria) throws IOException {
        return ResponseEntity.ok(produtosService.buscarPorDescricaoComCategoria(termo, categoria));
    }

    @GetMapping("/faixa-preco")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarPorFaixaDePreco(@RequestParam Double min,
                                                                           @RequestParam Double max) throws IOException {
        return ResponseEntity.ok(produtosService.buscarPorFaixaDePreco(min, max));
    }

    @GetMapping("/avancada")
    public ResponseEntity<List<RespostaProdutoLoja>> buscarAvancada(@RequestParam String categoria,
                                                                    @RequestParam String raridade,
                                                                    @RequestParam Double min,
                                                                    @RequestParam Double max) throws IOException {
        return ResponseEntity.ok(produtosService.buscarAvancada(categoria, raridade, min, max));
    }
}

