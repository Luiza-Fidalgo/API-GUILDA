package br.com.guilda.service;

import br.com.guilda.dto.PedidoCriacaoMissao;
import br.com.guilda.dto.RespostaDetalheMissao;
import br.com.guilda.dto.RespostaMissao;
import br.com.guilda.dto.RespostaResumoMissao;
import br.com.guilda.dto.PedidoParticipacaoMissao;
import br.com.guilda.dto.RespostaParticipanteMissao;
import br.com.guilda.dto.RespostaRankingParticipacao;
import br.com.guilda.dto.RespostaRelatorioMissao;
import br.com.guilda.exception.ExcecaoRequisicaoInvalida;
import br.com.guilda.exception.ExcecaoRecursoNaoEncontrado;
import br.com.guilda.model.IntegranteAventureiro;
import br.com.guilda.model.ContratoMissao;
import br.com.guilda.model.TipoNivelPerigo;
import br.com.guilda.model.TipoPapelMissao;
import br.com.guilda.model.VinculoParticipacaoMissao;
import br.com.guilda.model.ChaveParticipacaoMissao;
import br.com.guilda.model.TipoStatusMissao;
import br.com.guilda.model.ContaUsuario;
import br.com.guilda.repository.RepositorioIntegranteAventureiro;
import br.com.guilda.repository.RepositorioContratoMissao;
import br.com.guilda.repository.RepositorioVinculoParticipacaoMissao;
import br.com.guilda.repository.RepositorioContaUsuario;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoContratoMissao {

    private static final Sort ORDEM_PADRAO_TITULO = Sort.by("titulo").ascending();

    private final RepositorioContratoMissao missoes;
    private final RepositorioVinculoParticipacaoMissao participacoes;
    private final RepositorioIntegranteAventureiro aventureiros;
    private final RepositorioContaUsuario usuarios;

    public ServicoContratoMissao(RepositorioContratoMissao missaoRepository,
                         RepositorioVinculoParticipacaoMissao participacaoMissaoRepository,
                         RepositorioIntegranteAventureiro aventureiroRepository,
                         RepositorioContaUsuario usuarioRepository) {
        this.missoes = missaoRepository;
        this.participacoes = participacaoMissaoRepository;
        this.aventureiros = aventureiroRepository;
        this.usuarios = usuarioRepository;
    }

    @Transactional
    public RespostaMissao criar(PedidoCriacaoMissao dadosMissao) {
        validarMissao(dadosMissao);

        ContaUsuario responsavel = usuarios.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ExcecaoRecursoNaoEncontrado("Usuário responsáveis não encontrado"));

        ContratoMissao novaMissao = new ContratoMissao();
        novaMissao.setOrganizacao(responsavel.getOrganizacao());
        novaMissao.setTitulo(dadosMissao.getTitulo().trim());
        novaMissao.setNivelPerigo(TipoNivelPerigo.valueOf(dadosMissao.getNivelPerigo()));
        novaMissao.setStatus(TipoStatusMissao.PLANEJADA);

        return montarResposta(missoes.save(novaMissao));
    }

    @Transactional
    public void adicionarParticipante(Long missaoId, PedidoParticipacaoMissao dadosParticipacao) {
        validarParticipacao(dadosParticipacao);

        ContratoMissao missaoSelecionada = localizarMissao(missaoId);
        IntegranteAventureiro aventureiroSelecionado = aventureiros.findById(dadosParticipacao.getAventureiroId())
                .orElseThrow(() -> new ExcecaoRecursoNaoEncontrado("IntegranteAventureiro não encontrado"));

        validarEntradaNaMissao(missaoSelecionada, aventureiroSelecionado);

        ChaveParticipacaoMissao chaveParticipacao = new ChaveParticipacaoMissao(
                missaoSelecionada.getId(),
                aventureiroSelecionado.getId()
        );

        if (participacoes.existsById(chaveParticipacao)) {
            throw new ExcecaoRequisicaoInvalida("Solicitação inválida", List.of("aventureiro já participa dessa missão"));
        }

        VinculoParticipacaoMissao participacaoCriada = new VinculoParticipacaoMissao();
        participacaoCriada.setId(chaveParticipacao);
        participacaoCriada.setMissao(missaoSelecionada);
        participacaoCriada.setAventureiro(aventureiroSelecionado);
        participacaoCriada.setPapelMissao(TipoPapelMissao.valueOf(dadosParticipacao.getPapelMissao()));
        participacaoCriada.setRecompensaOuro(dadosParticipacao.getRecompensaOuro());
        participacaoCriada.setDestaque(dadosParticipacao.getDestaque());

        participacoes.save(participacaoCriada);
    }

    public List<RespostaRelatorioMissao> gerarRelatorioMissoes(String dataInicio, String dataFim) {
        List<String> inconsistencias = new ArrayList<>();
        IntervaloDatas intervalo = lerIntervaloDatas(dataInicio, dataFim, true, inconsistencias);
        dispararSeInvalido(inconsistencias);

        if (intervalo.completo()) {
            return missoes.gerarRelatorioMissoesPorPeriodo(intervalo.inicio(), intervalo.fim());
        }

        return missoes.gerarRelatorioMissoes();
    }

    public List<RespostaRankingParticipacao> gerarRankingParticipacao(String dataInicio,
                                                                      String dataFim,
                                                                      String status) {
        List<String> inconsistencias = new ArrayList<>();
        IntervaloDatas intervalo = lerIntervaloDatas(dataInicio, dataFim, true, inconsistencias);
        TipoStatusMissao situacao = lerStatusOpcional(status, inconsistencias);

        dispararSeInvalido(inconsistencias);

        if (intervalo.completo() && situacao != null) {
            return participacoes.gerarRankingParticipacaoPorPeriodoEStatus(intervalo.inicio(), intervalo.fim(), situacao);
        }

        if (intervalo.completo()) {
            return participacoes.gerarRankingParticipacaoPorPeriodo(intervalo.inicio(), intervalo.fim());
        }

        if (situacao != null) {
            return participacoes.gerarRankingParticipacaoPorStatus(situacao);
        }

        return participacoes.gerarRankingParticipacao();
    }

    public List<RespostaResumoMissao> listar(String status,
                                             String nivelPerigo,
                                             String dataInicio,
                                             String dataFim,
                                             Integer page,
                                             Integer size) {
        validarPaginacao(page, size);

        FiltroMissao filtros = montarFiltroMissao(status, nivelPerigo, dataInicio, dataFim);
        Pageable paginaSolicitada = PageRequest.of(page, size, ORDEM_PADRAO_TITULO);

        return buscarPagina(filtros, paginaSolicitada)
                .getContent()
                .stream()
                .map(this::montarResumo)
                .toList();
    }

    public int contarFiltradas(String status,
                               String nivelPerigo,
                               String dataInicio,
                               String dataFim) {
        FiltroMissao filtros = montarFiltroMissao(status, nivelPerigo, dataInicio, dataFim);

        if (filtros.semFiltros()) {
            return (int) missoes.count();
        }

        return (int) buscarPagina(filtros, PageRequest.of(0, 1)).getTotalElements();
    }

    public RespostaDetalheMissao buscarDetalhe(Long id) {
        ContratoMissao missaoSelecionada = localizarMissao(id);

        List<RespostaParticipanteMissao> participantes = participacoes.buscarParticipantesComAventureiro(id)
                .stream()
                .map(this::montarParticipante)
                .toList();

        return new RespostaDetalheMissao(
                missaoSelecionada.getId(),
                missaoSelecionada.getTitulo(),
                missaoSelecionada.getStatus().name(),
                missaoSelecionada.getNivelPerigo().name(),
                formatarData(missaoSelecionada.getCreatedAt()),
                formatarData(missaoSelecionada.getDataInicio()),
                formatarData(missaoSelecionada.getDataFim()),
                participantes
        );
    }

    private ContratoMissao localizarMissao(Long id) {
        return missoes.findById(id)
                .orElseThrow(() -> new ExcecaoRecursoNaoEncontrado("Missão não encontrada"));
    }

    private void validarEntradaNaMissao(ContratoMissao missaoSelecionada, IntegranteAventureiro aventureiroSelecionado) {
        if (!Boolean.TRUE.equals(aventureiroSelecionado.getAtivo())) {
            throw new ExcecaoRequisicaoInvalida("Solicitação inválida", List.of("aventureiro inativo não pode ser associado a missão"));
        }

        if (!missaoSelecionada.getOrganizacao().getId().equals(aventureiroSelecionado.getOrganizacao().getId())) {
            throw new ExcecaoRequisicaoInvalida("Solicitação inválida", List.of("aventureiro e missão devem pertencer à mesma organização"));
        }

        TipoStatusMissao situacaoAtual = missaoSelecionada.getStatus();
        if (situacaoAtual != TipoStatusMissao.PLANEJADA && situacaoAtual != TipoStatusMissao.EM_ANDAMENTO) {
            throw new ExcecaoRequisicaoInvalida("Solicitação inválida", List.of("missão não está em estado compatível para aceitar participantes"));
        }
    }

    private FiltroMissao montarFiltroMissao(String status,
                                            String nivelPerigo,
                                            String dataInicio,
                                            String dataFim) {
        List<String> inconsistencias = new ArrayList<>();
        TipoStatusMissao situacao = lerStatusOpcional(status, inconsistencias);
        TipoNivelPerigo perigo = lerPerigoOpcional(nivelPerigo, inconsistencias);
        IntervaloDatas intervalo = lerIntervaloDatas(dataInicio, dataFim, false, inconsistencias);

        dispararSeInvalido(inconsistencias);
        return new FiltroMissao(situacao, perigo, intervalo.inicio(), intervalo.fim());
    }

    private Page<ContratoMissao> buscarPagina(FiltroMissao filtros, Pageable paginaSolicitada) {
        if (filtros.temTudo()) {
            return missoes.findByStatusAndNivelPerigoAndCreatedAtBetween(
                    filtros.status(),
                    filtros.nivelPerigo(),
                    filtros.inicio(),
                    filtros.fim(),
                    paginaSolicitada
            );
        }

        if (filtros.status() != null && filtros.nivelPerigo() != null) {
            return missoes.findByStatusAndNivelPerigo(filtros.status(), filtros.nivelPerigo(), paginaSolicitada);
        }

        if (filtros.status() != null && filtros.temPeriodoCompleto()) {
            return missoes.findByStatusAndCreatedAtBetween(filtros.status(), filtros.inicio(), filtros.fim(), paginaSolicitada);
        }

        if (filtros.nivelPerigo() != null && filtros.temPeriodoCompleto()) {
            return missoes.findByNivelPerigoAndCreatedAtBetween(filtros.nivelPerigo(), filtros.inicio(), filtros.fim(), paginaSolicitada);
        }

        if (filtros.status() != null) {
            return missoes.findByStatus(filtros.status(), paginaSolicitada);
        }

        if (filtros.nivelPerigo() != null) {
            return missoes.findByNivelPerigo(filtros.nivelPerigo(), paginaSolicitada);
        }

        if (filtros.temPeriodoCompleto()) {
            return missoes.findByCreatedAtBetween(filtros.inicio(), filtros.fim(), paginaSolicitada);
        }

        return missoes.findAll(paginaSolicitada);
    }

    private RespostaParticipanteMissao montarParticipante(VinculoParticipacaoMissao participacao) {
        IntegranteAventureiro aventureiro = participacao.getAventureiro();

        return new RespostaParticipanteMissao(
                aventureiro.getId(),
                aventureiro.getNome(),
                aventureiro.getClasse().name(),
                aventureiro.getNivel(),
                participacao.getPapelMissao().name(),
                participacao.getRecompensaOuro(),
                participacao.getDestaque()
        );
    }

    private RespostaResumoMissao montarResumo(ContratoMissao missao) {
        return new RespostaResumoMissao(
                missao.getId(),
                missao.getTitulo(),
                missao.getStatus().name(),
                missao.getNivelPerigo().name(),
                formatarData(missao.getCreatedAt()),
                formatarData(missao.getDataInicio()),
                formatarData(missao.getDataFim())
        );
    }

    private RespostaMissao montarResposta(ContratoMissao missao) {
        return new RespostaMissao(
                missao.getId(),
                missao.getOrganizacao().getId(),
                missao.getTitulo(),
                missao.getNivelPerigo().name(),
                missao.getStatus().name(),
                formatarData(missao.getCreatedAt()),
                formatarData(missao.getDataInicio()),
                formatarData(missao.getDataFim())
        );
    }

    private String formatarData(OffsetDateTime data) {
        return data == null ? null : data.toString();
    }

    private void validarPaginacao(Integer page, Integer size) {
        List<String> inconsistencias = new ArrayList<>();

        if (page < 0) {
            inconsistencias.add("page não pode ser negativo");
        }

        if (size < 1 || size > 50) {
            inconsistencias.add("size deve estar entre 1 e 50");
        }

        dispararSeInvalido(inconsistencias);
    }

    private void validarMissao(PedidoCriacaoMissao dadosMissao) {
        List<String> inconsistencias = new ArrayList<>();

        if (dadosMissao.getTitulo() == null || dadosMissao.getTitulo().isBlank()) {
            inconsistencias.add("titulo é obrigatório");
        } else if (dadosMissao.getTitulo().length() > 150) {
            inconsistencias.add("titulo deve ter no máximo 150 caracteres");
        }

        if (dadosMissao.getNivelPerigo() == null || dadosMissao.getNivelPerigo().isBlank()) {
            inconsistencias.add("nivelPerigo é obrigatório");
        } else {
            lerPerigo(dadosMissao.getNivelPerigo(), inconsistencias);
        }

        dispararSeInvalido(inconsistencias);
    }

    private void validarParticipacao(PedidoParticipacaoMissao dadosParticipacao) {
        List<String> inconsistencias = new ArrayList<>();

        if (dadosParticipacao.getAventureiroId() == null) {
            inconsistencias.add("aventureiroId é obrigatório");
        }

        if (dadosParticipacao.getPapelMissao() == null || dadosParticipacao.getPapelMissao().isBlank()) {
            inconsistencias.add("papelMissao é obrigatório");
        } else {
            lerPapel(dadosParticipacao.getPapelMissao(), inconsistencias);
        }

        if (dadosParticipacao.getRecompensaOuro() != null && dadosParticipacao.getRecompensaOuro().signum() < 0) {
            inconsistencias.add("recompensaOuro deve ser maior ou igual a zero");
        }

        if (dadosParticipacao.getDestaque() == null) {
            inconsistencias.add("destaque é obrigatório");
        }

        dispararSeInvalido(inconsistencias);
    }

    private TipoStatusMissao lerStatusOpcional(String valor, List<String> inconsistencias) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return TipoStatusMissao.valueOf(valor);
        } catch (Exception exception) {
            inconsistencias.add("status inválido");
            return null;
        }
    }

    private TipoNivelPerigo lerPerigoOpcional(String valor, List<String> inconsistencias) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return lerPerigo(valor, inconsistencias);
    }

    private TipoNivelPerigo lerPerigo(String valor, List<String> inconsistencias) {
        try {
            return TipoNivelPerigo.valueOf(valor);
        } catch (Exception exception) {
            inconsistencias.add("nivelPerigo inválido");
            return null;
        }
    }

    private TipoPapelMissao lerPapel(String valor, List<String> inconsistencias) {
        try {
            return TipoPapelMissao.valueOf(valor);
        } catch (Exception exception) {
            inconsistencias.add("papelMissao inválido");
            return null;
        }
    }

    private IntervaloDatas lerIntervaloDatas(String dataInicio,
                                             String dataFim,
                                             boolean exigirParCompleto,
                                             List<String> inconsistencias) {
        OffsetDateTime inicio = lerDataInicio(dataInicio, inconsistencias);
        OffsetDateTime fim = lerDataFim(dataFim, inconsistencias);

        if (exigirParCompleto && ((inicio == null && fim != null) || (inicio != null && fim == null))) {
            inconsistencias.add("dataInicio e dataFim devem ser informadas juntas");
        }

        return new IntervaloDatas(inicio, fim);
    }

    private OffsetDateTime lerDataInicio(String valor, List<String> inconsistencias) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(valor).atStartOfDay().atOffset(ZoneOffset.UTC);
        } catch (Exception exception) {
            inconsistencias.add("dataInicio inválida");
            return null;
        }
    }

    private OffsetDateTime lerDataFim(String valor, List<String> inconsistencias) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(valor).plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        } catch (Exception exception) {
            inconsistencias.add("dataFim inválida");
            return null;
        }
    }

    private void dispararSeInvalido(List<String> inconsistencias) {
        if (!inconsistencias.isEmpty()) {
            throw new ExcecaoRequisicaoInvalida("Solicitação inválida", inconsistencias);
        }
    }

    private record IntervaloDatas(OffsetDateTime inicio, OffsetDateTime fim) {
        private boolean completo() {
            return inicio != null && fim != null;
        }
    }

    private record FiltroMissao(TipoStatusMissao status,
                                TipoNivelPerigo nivelPerigo,
                                OffsetDateTime inicio,
                                OffsetDateTime fim) {
        private boolean temPeriodoCompleto() {
            return inicio != null && fim != null;
        }

        private boolean temTudo() {
            return status != null && nivelPerigo != null && temPeriodoCompleto();
        }

        private boolean semFiltros() {
            return status == null && nivelPerigo == null && !temPeriodoCompleto();
        }
    }
}

