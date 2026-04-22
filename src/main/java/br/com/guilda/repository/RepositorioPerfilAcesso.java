package br.com.guilda.repository;

import br.com.guilda.model.PerfilAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioPerfilAcesso extends JpaRepository<PerfilAcesso, Long> {
}
