package br.com.guilda.repository;

import br.com.guilda.model.ChaveApiAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioChaveApiAuditoria extends JpaRepository<ChaveApiAuditoria, Long> {
}

