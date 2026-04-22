package br.com.guilda.repository;

import br.com.guilda.model.PerfilAcesso;
import br.com.guilda.model.VinculoPerfilPermissao;
import br.com.guilda.model.VinculoUsuarioPerfil;
import br.com.guilda.model.ContaUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultaUsuarioRepositorioTest {

    @Autowired
    private RepositorioContaUsuario usuarioRepository;

    @Test
    void deveCarregarUsuarioComOrganizacaoRolesEPermissions() {
        Optional<ContaUsuario> resultado = usuarioRepository.buscarPorIdComRolesEPermissions(1L);

        assertThat(resultado).isPresent();

        ContaUsuario usuario = resultado.get();

        assertThat(usuario.getOrganizacao()).isNotNull();
        assertThat(usuario.getUserRoles()).isNotEmpty();

        List<PerfilAcesso> roles = usuario.getUserRoles()
                .stream()
                .map(VinculoUsuarioPerfil::getRole)
                .toList();

        assertThat(roles).isNotEmpty();

        long totalPermissions = roles.stream()
                .flatMap(role -> role.getRolePermissions().stream())
                .map(VinculoPerfilPermissao::getPermission)
                .count();

        assertThat(totalPermissions).isGreaterThan(0);
    }
}
