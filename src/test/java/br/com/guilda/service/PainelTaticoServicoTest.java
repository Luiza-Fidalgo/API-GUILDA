package br.com.guilda.service;

import br.com.guilda.model.VisaoPainelTaticoMissao;
import br.com.guilda.repository.RepositorioVisaoPainelTaticoMissao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PainelTaticoServicoTest {

    @Mock
    private RepositorioVisaoPainelTaticoMissao repository;

    @InjectMocks
    private ServicoPainelTaticoMissao service;

    @Test
    void deveBuscarTopMissoesDosUltimos15Dias() {
        VisaoPainelTaticoMissao missao1 = new VisaoPainelTaticoMissao();
        VisaoPainelTaticoMissao missao2 = new VisaoPainelTaticoMissao();

        when(repository.findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(any(LocalDateTime.class)))
                .thenReturn(List.of(missao1, missao2));

        List<VisaoPainelTaticoMissao> resultado = service.buscarTopMissoesUltimos15Dias();

        assertThat(resultado).hasSize(2);
        verify(repository).findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(any(LocalDateTime.class));
    }
}

