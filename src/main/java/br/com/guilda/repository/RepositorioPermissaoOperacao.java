package br.com.guilda.repository;

import br.com.guilda.model.PermissaoOperacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioPermissaoOperacao extends JpaRepository<PermissaoOperacao, Long> {
}
