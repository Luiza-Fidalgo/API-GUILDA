package br.com.guilda.repository;

import br.com.guilda.model.NucleoOrganizacao;
import br.com.guilda.model.ContaUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenciaUsuarioRepositorioTest {

    @Autowired
    private RepositorioContaUsuario usuarioRepository;

    @Autowired
    private RepositorioNucleoOrganizacao organizacaoRepository;

    @Test
    void devePersistirUsuarioComOrganizacaoExistente() {
        NucleoOrganizacao organizacao = organizacaoRepository.findById(1L).orElseThrow();

        ContaUsuario usuario = new ContaUsuario();
        usuario.setOrganizacao(organizacao);
        usuario.setNome("ContaUsuario Teste");
        usuario.setEmail("usuario.teste." + System.currentTimeMillis() + "@guilda.com");
        usuario.setSenhaHash("hash-teste");
        usuario.setStatus("ATIVO");
        usuario.setCreatedAt(OffsetDateTime.now());
        usuario.setUpdatedAt(OffsetDateTime.now());

        ContaUsuario salvo = usuarioRepository.save(usuario);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getOrganizacao()).isNotNull();
        assertThat(salvo.getOrganizacao().getId()).isEqualTo(organizacao.getId());
    }
}
