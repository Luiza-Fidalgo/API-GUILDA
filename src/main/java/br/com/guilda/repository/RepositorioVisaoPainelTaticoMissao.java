package br.com.guilda.repository;

import br.com.guilda.model.VisaoPainelTaticoMissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RepositorioVisaoPainelTaticoMissao extends JpaRepository<VisaoPainelTaticoMissao, Long> {

    List<VisaoPainelTaticoMissao> findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(
            LocalDateTime dataLimite
    );
}
