package br.com.guilda.service;

import br.com.guilda.dto.RespostaDetalheAventureiro;
import br.com.guilda.dto.RespostaResumoAventureiro;
import br.com.guilda.model.IntegranteAventureiro;
import br.com.guilda.model.TipoClasseAventureiro;
import br.com.guilda.model.AliadoCompanheiro;
import br.com.guilda.model.TipoEspecieCompanheiro;
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
class ConsultaAventureiroServicoTest {

    @Autowired
    private ServicoAventureiroGuilda aventureiroService;

    @Autowired
    private RepositorioContaUsuario usuarioRepository;

    @Autowired
    private RepositorioIntegranteAventureiro aventureiroRepository;

    @Autowired
    private RepositorioContratoMissao missaoRepository;

    @Autowired
    private RepositorioVinculoParticipacaoMissao participacaoMissaoRepository;

    @Test
    void deveListarAventureirosComFiltros() {
        ContaUsuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        NucleoOrganizacao organizacao = usuario.getOrganizacao();

        IntegranteAventureiro a1 = criarAventureiro(organizacao, usuario, "Filtro Guerreiro Ativo", TipoClasseAventureiro.GUERREIRO, 10, true);
        criarAventureiro(organizacao, usuario, "Filtro Guerreiro Inativo", TipoClasseAventureiro.GUERREIRO, 10, false);
        criarAventureiro(organizacao, usuario, "Filtro Mago Ativo", TipoClasseAventureiro.MAGO, 10, true);
        criarAventureiro(organizacao, usuario, "Filtro Guerreiro Nivel Baixo", TipoClasseAventureiro.GUERREIRO, 1, true);

        List<RespostaResumoAventureiro> resultado = aventureiroService.listar("GUERREIRO", true, 5, 0, 10);

        assertThat(resultado).extracting(RespostaResumoAventureiro::getId).contains(a1.getId());
        assertThat(resultado).allMatch(a -> a.getClasse().equals("GUERREIRO"));
        assertThat(resultado).allMatch(RespostaResumoAventureiro::getAtivo);
        assertThat(resultado).allMatch(a -> a.getNivel() >= 5);
    }

    @Test
    void deveBuscarAventureiroPorNomeParcial() {
        ContaUsuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        NucleoOrganizacao organizacao = usuario.getOrganizacao();

        IntegranteAventureiro alvo = criarAventureiro(organizacao, usuario, "Arthorius Busca Parcial", TipoClasseAventureiro.GUERREIRO, 8, true);
        criarAventureiro(organizacao, usuario, "Beltrano", TipoClasseAventureiro.MAGO, 5, true);

        List<RespostaResumoAventureiro> resultado = aventureiroService.buscarPorNome("thor", 0, 10);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).extracting(RespostaResumoAventureiro::getId).contains(alvo.getId());
        assertThat(resultado).allMatch(a -> a.getNome().toLowerCase().contains("thor"));
    }

    @Test
    void deveRetornarVisualizacaoCompletaDoAventureiro() {
        ContaUsuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        NucleoOrganizacao organizacao = usuario.getOrganizacao();

        IntegranteAventureiro aventureiro = criarAventureiro(organizacao, usuario, "Detalhe Completo", TipoClasseAventureiro.ARQUEIRO, 12, true);
        aventureiro.setCompanheiro(new AliadoCompanheiro("Fenrir Teste", TipoEspecieCompanheiro.LOBO, 90));
        aventureiro = aventureiroRepository.save(aventureiro);

        ContratoMissao missao = new ContratoMissao();
        missao.setOrganizacao(organizacao);
        missao.setTitulo("ContratoMissao do Detalhe");
        missao.setNivelPerigo(TipoNivelPerigo.MEDIO);
        missao.setStatus(TipoStatusMissao.PLANEJADA);
        missao = missaoRepository.save(missao);

        VinculoParticipacaoMissao participacao = new VinculoParticipacaoMissao();
        participacao.setId(new ChaveParticipacaoMissao(missao.getId(), aventureiro.getId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapelMissao(TipoPapelMissao.LIDER);
        participacao.setRecompensaOuro(new BigDecimal("150.00"));
        participacao.setDestaque(true);
        participacaoMissaoRepository.save(participacao);

        RespostaDetalheAventureiro detalhe = aventureiroService.buscarPorId(aventureiro.getId());

        assertThat(detalhe.getId()).isEqualTo(aventureiro.getId());
        assertThat(detalhe.getCompanheiro()).isNotNull();
        assertThat(detalhe.getCompanheiro().getNome()).isEqualTo("Fenrir Teste");
        assertThat(detalhe.getTotalParticipacoes()).isEqualTo(1);
        assertThat(detalhe.getUltimaMissao()).isNotNull();
        assertThat(detalhe.getUltimaMissao().getTitulo()).isEqualTo("ContratoMissao do Detalhe");
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
}
