package br.com.guilda.service;

import br.com.guilda.dto.RespostaDetalheMissao;
import br.com.guilda.dto.RespostaResumoMissao;
import br.com.guilda.dto.RespostaRankingParticipacao;
import br.com.guilda.dto.RespostaRelatorioMissao;
import br.com.guilda.model.IntegranteAventureiro;
import br.com.guilda.model.TipoClasseAventureiro;
import br.com.guilda.model.ContratoMissao;
import br.com.guilda.model.TipoNivelPerigo;
import br.com.guilda.model.NucleoOrganizacao;
import br.com.guilda.model.TipoPapelMissao;
import br.com.guilda.model.VinculoParticipacaoMissao;
import br.com.guilda.model.ChaveParticipacaoMissao;
import br.com.guilda.model.TipoStatusMissao;
import br.com.guilda.model.ContaUsuario;
import br.com.guilda.repository.RepositorioIntegranteAventureiro;
import br.com.guilda.repository.RepositorioContratoMissao;
import br.com.guilda.repository.RepositorioVinculoParticipacaoMissao;
import br.com.guilda.repository.RepositorioContaUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultaMissaoServicoTest {

    @Autowired
    private ServicoContratoMissao missaoService;

    @Autowired
    private RepositorioContaUsuario usuarioRepository;

    @Autowired
    private RepositorioContratoMissao missaoRepository;

    @Autowired
    private RepositorioIntegranteAventureiro aventureiroRepository;

    @Autowired
    private RepositorioVinculoParticipacaoMissao participacaoMissaoRepository;

    @Test
    void deveListarMissoesComFiltros() {
        ContaUsuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        NucleoOrganizacao organizacao = usuario.getOrganizacao();

        ContratoMissao alvo = criarMissao(organizacao, "ContratoMissao Filtro Planejada", TipoNivelPerigo.ALTO, TipoStatusMissao.PLANEJADA);
        criarMissao(organizacao, "ContratoMissao Filtro Cancelada", TipoNivelPerigo.ALTO, TipoStatusMissao.CANCELADA);
        criarMissao(organizacao, "ContratoMissao Filtro Baixo", TipoNivelPerigo.BAIXO, TipoStatusMissao.PLANEJADA);

        List<RespostaResumoMissao> resultado = missaoService.listar("PLANEJADA", "ALTO", null, null, 0, 10);

        assertThat(resultado).extracting(RespostaResumoMissao::getId).contains(alvo.getId());
        assertThat(resultado).allMatch(m -> m.getStatus().equals("PLANEJADA"));
        assertThat(resultado).allMatch(m -> m.getNivelPerigo().equals("ALTO"));
    }

    @Test
    void deveRetornarDetalhamentoDeMissaoComParticipantes() {
        ContaUsuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        NucleoOrganizacao organizacao = usuario.getOrganizacao();

        IntegranteAventureiro aventureiro = criarAventureiro(organizacao, usuario, "Participante Detalhe", TipoClasseAventureiro.GUERREIRO, 10, true);
        ContratoMissao missao = criarMissao(organizacao, "ContratoMissao Detalhada", TipoNivelPerigo.MEDIO, TipoStatusMissao.PLANEJADA);

        VinculoParticipacaoMissao participacao = new VinculoParticipacaoMissao();
        participacao.setId(new ChaveParticipacaoMissao(missao.getId(), aventureiro.getId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapelMissao(TipoPapelMissao.LIDER);
        participacao.setRecompensaOuro(new BigDecimal("200.00"));
        participacao.setDestaque(true);
        participacaoMissaoRepository.save(participacao);

        RespostaDetalheMissao detalhe = missaoService.buscarDetalhe(missao.getId());

        assertThat(detalhe.getId()).isEqualTo(missao.getId());
        assertThat(detalhe.getParticipantes()).hasSize(1);
        assertThat(detalhe.getParticipantes().get(0).getNome()).isEqualTo("Participante Detalhe");
        assertThat(detalhe.getParticipantes().get(0).getPapelMissao()).isEqualTo("LIDER");
        assertThat(detalhe.getParticipantes().get(0).getRecompensaOuro()).isEqualByComparingTo("200.00");
        assertThat(detalhe.getParticipantes().get(0).getDestaque()).isTrue();
    }

    @Test
    void deveGerarRankingDeParticipacao() {
        ContaUsuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        NucleoOrganizacao organizacao = usuario.getOrganizacao();

        IntegranteAventureiro a1 = criarAventureiro(organizacao, usuario, "Ranking Um", TipoClasseAventureiro.GUERREIRO, 10, true);
        IntegranteAventureiro a2 = criarAventureiro(organizacao, usuario, "Ranking Dois", TipoClasseAventureiro.MAGO, 8, true);

        ContratoMissao m1 = criarMissao(organizacao, "Ranking ContratoMissao 1", TipoNivelPerigo.MEDIO, TipoStatusMissao.PLANEJADA);
        ContratoMissao m2 = criarMissao(organizacao, "Ranking ContratoMissao 2", TipoNivelPerigo.ALTO, TipoStatusMissao.PLANEJADA);

        salvarParticipacao(m1, a1, TipoPapelMissao.LIDER, "100.00", true);
        salvarParticipacao(m2, a1, TipoPapelMissao.COMBATENTE, "50.00", false);
        salvarParticipacao(m1, a2, TipoPapelMissao.SUPORTE, "30.00", false);

        List<RespostaRankingParticipacao> ranking = missaoService.gerarRankingParticipacao(null, null, null);

        assertThat(ranking).isNotEmpty();
        RespostaRankingParticipacao primeiro = ranking.get(0);

        assertThat(primeiro.getAventureiroId()).isEqualTo(a1.getId());
        assertThat(primeiro.getTotalParticipacoes()).isEqualTo(2);
        assertThat(primeiro.getTotalRecompensas()).isEqualByComparingTo("150.00");
        assertThat(primeiro.getTotalDestaques()).isEqualTo(1);
    }

    @Test
    void deveGerarRelatorioDeMissoesComMetricas() {
        ContaUsuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        NucleoOrganizacao organizacao = usuario.getOrganizacao();

        IntegranteAventureiro aventureiro = criarAventureiro(organizacao, usuario, "Participante Relatorio", TipoClasseAventureiro.ARQUEIRO, 9, true);
        ContratoMissao missao = criarMissao(organizacao, "Relatorio ContratoMissao", TipoNivelPerigo.EXTREMO, TipoStatusMissao.PLANEJADA);

        salvarParticipacao(missao, aventureiro, TipoPapelMissao.EXPLORADOR, "80.00", true);

        List<RespostaRelatorioMissao> relatorio = missaoService.gerarRelatorioMissoes(null, null);

        RespostaRelatorioMissao linha = relatorio.stream()
                .filter(r -> r.getMissaoId().equals(missao.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(linha.getTitulo()).isEqualTo("Relatorio ContratoMissao");
        assertThat(linha.getStatus()).isEqualTo("PLANEJADA");
        assertThat(linha.getNivelPerigo()).isEqualTo("EXTREMO");
        assertThat(linha.getQuantidadeParticipantes()).isEqualTo(1);
        assertThat(linha.getTotalRecompensas()).isEqualByComparingTo("80.00");
    }

    private ContratoMissao criarMissao(NucleoOrganizacao organizacao, String titulo, TipoNivelPerigo nivelPerigo, TipoStatusMissao status) {
        ContratoMissao missao = new ContratoMissao();
        missao.setOrganizacao(organizacao);
        missao.setTitulo(titulo);
        missao.setNivelPerigo(nivelPerigo);
        missao.setStatus(status);
        return missaoRepository.save(missao);
    }

    private IntegranteAventureiro criarAventureiro(NucleoOrganizacao organizacao,
                                         ContaUsuario usuario,
                                         String nome,
                                         TipoClasseAventureiro classe,
                                         Integer nivel,
                                         Boolean ativo) {
        IntegranteAventureiro aventureiro = new IntegranteAventureiro();
        aventureiro.setOrganizacao(organizacao);
        aventureiro.setUsuarioCadastro(usuario);
        aventureiro.setNome(nome);
        aventureiro.setClasse(classe);
        aventureiro.setNivel(nivel);
        aventureiro.setAtivo(ativo);
        return aventureiroRepository.save(aventureiro);
    }

    private void salvarParticipacao(ContratoMissao missao,
                                    IntegranteAventureiro aventureiro,
                                    TipoPapelMissao papelMissao,
                                    String recompensa,
                                    Boolean destaque) {
        VinculoParticipacaoMissao participacao = new VinculoParticipacaoMissao();
        participacao.setId(new ChaveParticipacaoMissao(missao.getId(), aventureiro.getId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapelMissao(papelMissao);
        participacao.setRecompensaOuro(new BigDecimal(recompensa));
        participacao.setDestaque(destaque);
        participacaoMissaoRepository.save(participacao);
    }
}

