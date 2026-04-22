package br.com.guilda.service;

import br.com.guilda.dto.PedidoCriacaoAventureiro;
import br.com.guilda.dto.RespostaDetalheAventureiro;
import br.com.guilda.dto.RespostaResumoAventureiro;
import br.com.guilda.dto.PedidoAtualizacaoAventureiro;
import br.com.guilda.dto.PedidoCompanheiro;
import br.com.guilda.dto.RespostaCompanheiro;
import br.com.guilda.dto.RespostaUltimaMissao;
import br.com.guilda.exception.ExcecaoRequisicaoInvalida;
import br.com.guilda.exception.ExcecaoRecursoNaoEncontrado;
import br.com.guilda.model.IntegranteAventureiro;
import br.com.guilda.model.TipoClasseAventureiro;
import br.com.guilda.model.AliadoCompanheiro;
import br.com.guilda.model.TipoEspecieCompanheiro;
import br.com.guilda.model.VinculoParticipacaoMissao;
import br.com.guilda.model.ContaUsuario;
import br.com.guilda.repository.RepositorioIntegranteAventureiro;
import br.com.guilda.repository.RepositorioVinculoParticipacaoMissao;
import br.com.guilda.repository.RepositorioContaUsuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ServicoAventureiroGuilda {

    private static final Sort ORDEM_PADRAO_NOME = Sort.by("nome").ascending();

    private final RepositorioIntegranteAventureiro aventureiros;
    private final RepositorioContaUsuario usuarios;
    private final RepositorioVinculoParticipacaoMissao participacoes;

    public ServicoAventureiroGuilda(RepositorioIntegranteAventureiro repository,
                              RepositorioContaUsuario usuarioRepository,
                              RepositorioVinculoParticipacaoMissao participacaoMissaoRepository) {
        this.aventureiros = repository;
        this.usuarios = usuarioRepository;
        this.participacoes = participacaoMissaoRepository;
    }

    public RespostaDetalheAventureiro criar(PedidoCriacaoAventureiro dadosCadastro) {
        validarFichaAventureiro(dadosCadastro.getNome(), dadosCadastro.getClasse(), dadosCadastro.getNivel());

        ContaUsuario responsavelCadastro = carregarUsuarioDeReferencia();
        IntegranteAventureiro novoAventureiro = montarAventureiro(dadosCadastro, responsavelCadastro);

        aventureiros.save(novoAventureiro);
        return montarDetalhe(novoAventureiro);
    }

    public RespostaDetalheAventureiro buscarPorId(Long id) {
        return montarDetalhe(carregarAventureiro(id));
    }

    public List<RespostaResumoAventureiro> listar(String classe,
                                                  Boolean ativo,
                                                  Integer nivelMinimo,
                                                  Integer page,
                                                  Integer size) {
        validarPaginacao(page, size);

        Pageable paginaSolicitada = PageRequest.of(page, size, ORDEM_PADRAO_NOME);
        TipoClasseAventureiro classeFiltrada = interpretarClasseOpcional(classe);

        return pesquisarAventureiros(ativo, classeFiltrada, nivelMinimo, paginaSolicitada)
                .getContent()
                .stream()
                .map(this::montarResumo)
                .toList();
    }

    public List<RespostaResumoAventureiro> buscarPorNome(String nome, Integer page, Integer size) {
        validarPaginacao(page, size);
        exigirTextoBusca(nome);

        Pageable paginaSolicitada = PageRequest.of(page, size, ORDEM_PADRAO_NOME);

        return aventureiros.findByNomeContainingIgnoreCase(nome.trim(), paginaSolicitada)
                .getContent()
                .stream()
                .map(this::montarResumo)
                .toList();
    }

    public int contarPorNome(String nome) {
        exigirTextoBusca(nome);

        return (int) aventureiros.findByNomeContainingIgnoreCase(nome.trim(), PageRequest.of(0, 1))
                .getTotalElements();
    }

    public int contarFiltrados(String classe, Boolean ativo, Integer nivelMinimo) {
        TipoClasseAventureiro classeFiltrada = interpretarClasseOpcional(classe);

        if (classeFiltrada == null && ativo == null && nivelMinimo == null) {
            return (int) aventureiros.count();
        }

        return (int) pesquisarAventureiros(ativo, classeFiltrada, nivelMinimo, PageRequest.of(0, 1))
                .getTotalElements();
    }

    public RespostaDetalheAventureiro atualizar(Long id, PedidoAtualizacaoAventureiro dadosAtualizacao) {
        validarFichaAventureiro(
                dadosAtualizacao.getNome(),
                dadosAtualizacao.getClasse(),
                dadosAtualizacao.getNivel()
        );

        IntegranteAventureiro cadastroEncontrado = carregarAventureiro(id);
        aplicarDadosBasicos(cadastroEncontrado, dadosAtualizacao.getNome(), dadosAtualizacao.getClasse(), dadosAtualizacao.getNivel());

        aventureiros.save(cadastroEncontrado);
        return montarDetalhe(cadastroEncontrado);
    }

    public void encerrarVinculo(Long id) {
        alterarSituacao(id, false);
    }

    public void recrutarNovamente(Long id) {
        alterarSituacao(id, true);
    }

    public RespostaDetalheAventureiro definirCompanheiro(Long id, PedidoCompanheiro dadosCompanheiro) {
        validarCompanheiro(dadosCompanheiro);

        IntegranteAventureiro aventureiroLocalizado = carregarAventureiro(id);
        aventureiroLocalizado.setCompanheiro(new AliadoCompanheiro(
                dadosCompanheiro.getNome().trim(),
                TipoEspecieCompanheiro.valueOf(dadosCompanheiro.getEspecie()),
                dadosCompanheiro.getLealdade()
        ));

        aventureiros.save(aventureiroLocalizado);
        return montarDetalhe(aventureiroLocalizado);
    }

    public void removerCompanheiro(Long id) {
        IntegranteAventureiro aventureiroLocalizado = carregarAventureiro(id);

        aventureiroLocalizado.setCompanheiro(null);
        aventureiros.save(aventureiroLocalizado);
    }

    private void alterarSituacao(Long id, boolean ativo) {
        IntegranteAventureiro aventureiroLocalizado = carregarAventureiro(id);

        aventureiroLocalizado.setAtivo(ativo);
        aventureiros.save(aventureiroLocalizado);
    }

    private ContaUsuario carregarUsuarioDeReferencia() {
        return usuarios.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ExcecaoRecursoNaoEncontrado("Usuário de cadastro não encontrado"));
    }

    private IntegranteAventureiro carregarAventureiro(Long id) {
        return aventureiros.findById(id)
                .orElseThrow(() -> new ExcecaoRecursoNaoEncontrado("IntegranteAventureiro não encontrado"));
    }

    private IntegranteAventureiro montarAventureiro(PedidoCriacaoAventureiro dadosCadastro, ContaUsuario responsavelCadastro) {
        IntegranteAventureiro novoAventureiro = new IntegranteAventureiro();
        novoAventureiro.setOrganizacao(responsavelCadastro.getOrganizacao());
        novoAventureiro.setUsuarioCadastro(responsavelCadastro);
        aplicarDadosBasicos(
                novoAventureiro,
                dadosCadastro.getNome(),
                dadosCadastro.getClasse(),
                dadosCadastro.getNivel()
        );
        novoAventureiro.setAtivo(true);
        return novoAventureiro;
    }

    private void aplicarDadosBasicos(IntegranteAventureiro destino, String nome, String classe, Integer nivel) {
        destino.setNome(nome.trim());
        destino.setClasse(TipoClasseAventureiro.valueOf(classe));
        destino.setNivel(nivel);
    }

    private Page<IntegranteAventureiro> pesquisarAventureiros(Boolean ativo,
                                                    TipoClasseAventureiro classe,
                                                    Integer nivelMinimo,
                                                    Pageable paginaSolicitada) {
        if (ativo != null && classe != null && nivelMinimo != null) {
            return aventureiros.findByAtivoAndClasseAndNivelGreaterThanEqual(ativo, classe, nivelMinimo, paginaSolicitada);
        }

        if (ativo != null && classe != null) {
            return aventureiros.findByAtivoAndClasse(ativo, classe, paginaSolicitada);
        }

        if (ativo != null && nivelMinimo != null) {
            return aventureiros.findByAtivoAndNivelGreaterThanEqual(ativo, nivelMinimo, paginaSolicitada);
        }

        if (classe != null && nivelMinimo != null) {
            return aventureiros.findByClasseAndNivelGreaterThanEqual(classe, nivelMinimo, paginaSolicitada);
        }

        if (ativo != null) {
            return aventureiros.findByAtivo(ativo, paginaSolicitada);
        }

        if (classe != null) {
            return aventureiros.findByClasse(classe, paginaSolicitada);
        }

        if (nivelMinimo != null) {
            return aventureiros.findByNivelGreaterThanEqual(nivelMinimo, paginaSolicitada);
        }

        return aventureiros.findAll(paginaSolicitada);
    }

    private RespostaResumoAventureiro montarResumo(IntegranteAventureiro cadastro) {
        return new RespostaResumoAventureiro(
                cadastro.getId(),
                cadastro.getNome(),
                cadastro.getClasse().name(),
                cadastro.getNivel(),
                cadastro.getAtivo()
        );
    }

    private RespostaDetalheAventureiro montarDetalhe(IntegranteAventureiro cadastro) {
        RespostaCompanheiro companheiro = montarCompanheiro(cadastro.getCompanheiro());
        RespostaUltimaMissao ultimaMissao = montarUltimaMissao(cadastro.getId());
        int totalParticipacoes = (int) participacoes.countByAventureiroId(cadastro.getId());

        return new RespostaDetalheAventureiro(
                cadastro.getId(),
                cadastro.getNome(),
                cadastro.getClasse().name(),
                cadastro.getNivel(),
                cadastro.getAtivo(),
                companheiro,
                totalParticipacoes,
                ultimaMissao
        );
    }

    private RespostaCompanheiro montarCompanheiro(AliadoCompanheiro companheiroAtual) {
        if (companheiroAtual == null) {
            return null;
        }

        return new RespostaCompanheiro(
                companheiroAtual.getNome(),
                companheiroAtual.getEspecie().name(),
                companheiroAtual.getLealdade()
        );
    }

    private RespostaUltimaMissao montarUltimaMissao(Long aventureiroId) {
        List<VinculoParticipacaoMissao> historicoRecente = participacoes.buscarUltimasParticipacoesComMissao(aventureiroId);

        if (historicoRecente.isEmpty()) {
            return null;
        }

        var missaoMaisRecente = historicoRecente.get(0).getMissao();
        return new RespostaUltimaMissao(
                missaoMaisRecente.getId(),
                missaoMaisRecente.getTitulo(),
                missaoMaisRecente.getStatus().name(),
                missaoMaisRecente.getNivelPerigo().name()
        );
    }

    private void validarFichaAventureiro(String nome, String classe, Integer nivel) {
        List<String> inconsistencias = new ArrayList<>();

        if (nome == null || nome.isBlank()) {
            inconsistencias.add("nome é obrigatório");
        }

        if (classe == null || classe.isBlank()) {
            inconsistencias.add("classe é obrigatória");
        } else {
            tentarLerClasse(classe, inconsistencias);
        }

        if (nivel == null || nivel < 1) {
            inconsistencias.add("nível deve ser maior ou igual a 1");
        }

        dispararSeInvalido(inconsistencias);
    }

    private void validarCompanheiro(PedidoCompanheiro dadosCompanheiro) {
        List<String> inconsistencias = new ArrayList<>();

        if (dadosCompanheiro.getNome() == null || dadosCompanheiro.getNome().isBlank()) {
            inconsistencias.add("nome do companheiro é obrigatório");
        }

        if (dadosCompanheiro.getEspecie() == null || dadosCompanheiro.getEspecie().isBlank()) {
            inconsistencias.add("especie é obrigatória");
        } else {
            tentarLerEspecie(dadosCompanheiro.getEspecie(), inconsistencias);
        }

        Integer lealdadeInformada = dadosCompanheiro.getLealdade();
        if (lealdadeInformada == null || lealdadeInformada < 0 || lealdadeInformada > 100) {
            inconsistencias.add("lealdade deve estar entre 0 e 100");
        }

        dispararSeInvalido(inconsistencias);
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

    private void exigirTextoBusca(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ExcecaoRequisicaoInvalida("Solicitação inválida", List.of("nome é obrigatório para busca"));
        }
    }

    private TipoClasseAventureiro interpretarClasseOpcional(String classe) {
        if (classe == null || classe.isBlank()) {
            return null;
        }

        List<String> inconsistencias = new ArrayList<>();
        TipoClasseAventureiro classeConvertida = tentarLerClasse(classe, inconsistencias);
        dispararSeInvalido(inconsistencias);
        return classeConvertida;
    }

    private TipoClasseAventureiro tentarLerClasse(String valor, List<String> inconsistencias) {
        try {
            return TipoClasseAventureiro.valueOf(valor);
        } catch (Exception exception) {
            inconsistencias.add("classe inválida");
            return null;
        }
    }

    private TipoEspecieCompanheiro tentarLerEspecie(String valor, List<String> inconsistencias) {
        try {
            return TipoEspecieCompanheiro.valueOf(valor);
        } catch (Exception exception) {
            inconsistencias.add("espécie inválida");
            return null;
        }
    }

    private void dispararSeInvalido(List<String> inconsistencias) {
        if (!inconsistencias.isEmpty()) {
            throw new ExcecaoRequisicaoInvalida("Solicitação inválida", inconsistencias);
        }
    }
}

