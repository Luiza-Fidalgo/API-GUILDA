package br.com.guilda.repository;

import br.com.guilda.model.VinculoPerfilPermissao;
import br.com.guilda.model.ChavePerfilPermissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioVinculoPerfilPermissao extends JpaRepository<VinculoPerfilPermissao, ChavePerfilPermissao> {
}
