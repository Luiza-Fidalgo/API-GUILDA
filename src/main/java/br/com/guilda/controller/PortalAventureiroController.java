package br.com.guilda.controller;

import br.com.guilda.dto.PedidoCriacaoAventureiro;
import br.com.guilda.dto.RespostaDetalheAventureiro;
import br.com.guilda.dto.RespostaResumoAventureiro;
import br.com.guilda.dto.PedidoAtualizacaoAventureiro;
import br.com.guilda.dto.PedidoCompanheiro;
import br.com.guilda.service.ServicoAventureiroGuilda;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guilda")
public class PortalAventureiroController {

    private final ServicoAventureiroGuilda aventureiroService;

    public PortalAventureiroController(ServicoAventureiroGuilda service) {
        this.aventureiroService = service;
    }

    @PostMapping
    public ResponseEntity<RespostaDetalheAventureiro> criar(@RequestBody PedidoCriacaoAventureiro request) {
        RespostaDetalheAventureiro cadastroCriado = aventureiroService.criar(request);
        URI recursoCriado = URI.create("/api/guilda/" + cadastroCriado.getId());

        return ResponseEntity.created(recursoCriado).body(cadastroCriado);
    }

    @GetMapping
    public ResponseEntity<List<RespostaResumoAventureiro>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMinimo,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        ResultadoPaginado<RespostaResumoAventureiro> pagina = buscarPagina(nome, classe, ativo, nivelMinimo, page, size);
        return responderComPaginacao(pagina.itens(), pagina.total(), page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaDetalheAventureiro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(aventureiroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaDetalheAventureiro> atualizar(
            @PathVariable Long id,
            @RequestBody PedidoAtualizacaoAventureiro request
    ) {
        return ResponseEntity.ok(aventureiroService.atualizar(id, request));
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrar(@PathVariable Long id) {
        aventureiroService.encerrarVinculo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recrutar")
    public ResponseEntity<Void> recrutar(@PathVariable Long id) {
        aventureiroService.recrutarNovamente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/companheiro")
    public ResponseEntity<RespostaDetalheAventureiro> definirCompanheiro(
            @PathVariable Long id,
            @RequestBody PedidoCompanheiro request
    ) {
        return ResponseEntity.ok(aventureiroService.definirCompanheiro(id, request));
    }

    @DeleteMapping("/{id}/companheiro")
    public ResponseEntity<Void> removerCompanheiro(@PathVariable Long id) {
        aventureiroService.removerCompanheiro(id);
        return ResponseEntity.noContent().build();
    }

    private ResultadoPaginado<RespostaResumoAventureiro> buscarPagina(String nome,
                                                                      String classe,
                                                                      Boolean ativo,
                                                                      Integer nivelMinimo,
                                                                      Integer page,
                                                                      Integer size) {
        if (nome != null && !nome.isBlank()) {
            return new ResultadoPaginado<>(
                    aventureiroService.buscarPorNome(nome, page, size),
                    aventureiroService.contarPorNome(nome)
            );
        }

        return new ResultadoPaginado<>(
                aventureiroService.listar(classe, ativo, nivelMinimo, page, size),
                aventureiroService.contarFiltrados(classe, ativo, nivelMinimo)
        );
    }

    private ResponseEntity<List<RespostaResumoAventureiro>> responderComPaginacao(List<RespostaResumoAventureiro> corpo,
                                                                                  int total,
                                                                                  Integer page,
                                                                                  Integer size) {
        int totalPages = (int) Math.ceil((double) total / size);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(total))
                .header("X-Page", String.valueOf(page))
                .header("X-Size", String.valueOf(size))
                .header("X-Total-Pages", String.valueOf(totalPages))
                .body(corpo);
    }

    private record ResultadoPaginado<T>(List<T> itens, int total) {
    }
}

