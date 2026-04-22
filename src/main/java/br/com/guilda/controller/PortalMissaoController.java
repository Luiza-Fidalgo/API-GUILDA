package br.com.guilda.controller;

import br.com.guilda.dto.PedidoCriacaoMissao;
import br.com.guilda.dto.RespostaDetalheMissao;
import br.com.guilda.dto.RespostaMissao;
import br.com.guilda.dto.RespostaResumoMissao;
import br.com.guilda.dto.PedidoParticipacaoMissao;
import br.com.guilda.dto.RespostaRankingParticipacao;
import br.com.guilda.dto.RespostaRelatorioMissao;
import br.com.guilda.service.ServicoContratoMissao;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/missoes")
public class PortalMissaoController {

    private final ServicoContratoMissao missaoService;

    public PortalMissaoController(ServicoContratoMissao service) {
        this.missaoService = service;
    }

    @PostMapping
    public ResponseEntity<RespostaMissao> criar(@RequestBody PedidoCriacaoMissao request) {
        RespostaMissao missaoCriada = missaoService.criar(request);
        URI recursoCriado = URI.create("/api/missoes/" + missaoCriada.getId());

        return ResponseEntity.created(recursoCriado).body(missaoCriada);
    }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<Void> adicionarParticipante(@PathVariable Long id,
                                                      @RequestBody PedidoParticipacaoMissao request) {
        missaoService.adicionarParticipante(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RespostaResumoMissao>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String nivelPerigo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<RespostaResumoMissao> paginaAtual = missaoService.listar(
                status,
                nivelPerigo,
                dataInicio,
                dataFim,
                page,
                size
        );
        int totalEncontrado = missaoService.contarFiltradas(status, nivelPerigo, dataInicio, dataFim);

        return responderComPaginacao(paginaAtual, totalEncontrado, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaDetalheMissao> buscarDetalhe(@PathVariable Long id) {
        return ResponseEntity.ok(missaoService.buscarDetalhe(id));
    }

    @GetMapping("/relatorios/ranking-participacao")
    public ResponseEntity<List<RespostaRankingParticipacao>> gerarRankingParticipacao(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(missaoService.gerarRankingParticipacao(dataInicio, dataFim, status));
    }

    @GetMapping("/relatorios/missoes-metricas")
    public ResponseEntity<List<RespostaRelatorioMissao>> gerarRelatorioMissoes(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim
    ) {
        return ResponseEntity.ok(missaoService.gerarRelatorioMissoes(dataInicio, dataFim));
    }

    private ResponseEntity<List<RespostaResumoMissao>> responderComPaginacao(List<RespostaResumoMissao> corpo,
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
}

