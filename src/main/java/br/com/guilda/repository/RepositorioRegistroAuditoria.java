package br.com.guilda.repository;

import br.com.guilda.model.RegistroAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioRegistroAuditoria extends JpaRepository<RegistroAuditoria, Long> {
}

