package br.com.guilda.service;

import br.com.guilda.model.VisaoPainelTaticoMissao;
import br.com.guilda.repository.RepositorioVisaoPainelTaticoMissao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicoPainelTaticoMissao {

    private final RepositorioVisaoPainelTaticoMissao painelTatico;

    public ServicoPainelTaticoMissao(RepositorioVisaoPainelTaticoMissao repository) {
        this.painelTatico = repository;
    }

    @Cacheable(value = "topMissoes15Dias", key = "'rankingTopMissoes'")
    public List<VisaoPainelTaticoMissao> buscarTopMissoesUltimos15Dias() {
        LocalDateTime corteTemporal = LocalDateTime.now().minusDays(15);
        return painelTatico.findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(corteTemporal);
    }
}

