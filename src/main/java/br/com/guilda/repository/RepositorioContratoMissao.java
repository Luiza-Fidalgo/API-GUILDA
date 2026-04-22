package br.com.guilda.repository;

import br.com.guilda.model.ContratoMissao;
import br.com.guilda.model.TipoNivelPerigo;
import br.com.guilda.model.TipoStatusMissao;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.guilda.dto.RespostaRelatorioMissao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositorioContratoMissao extends JpaRepository<ContratoMissao, Long> {

    Page<ContratoMissao> findByStatus(TipoStatusMissao status, Pageable pageable);

    Page<ContratoMissao> findByNivelPerigo(TipoNivelPerigo nivelPerigo, Pageable pageable);

    Page<ContratoMissao> findByCreatedAtBetween(OffsetDateTime inicio, OffsetDateTime fim, Pageable pageable);

    Page<ContratoMissao> findByStatusAndNivelPerigo(TipoStatusMissao status, TipoNivelPerigo nivelPerigo, Pageable pageable);

    Page<ContratoMissao> findByStatusAndCreatedAtBetween(
            TipoStatusMissao status,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            Pageable pageable
    );

    Page<ContratoMissao> findByNivelPerigoAndCreatedAtBetween(
            TipoNivelPerigo nivelPerigo,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            Pageable pageable
    );

    Page<ContratoMissao> findByStatusAndNivelPerigoAndCreatedAtBetween(
            TipoStatusMissao status,
            TipoNivelPerigo nivelPerigo,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            Pageable pageable
    );

    @Query("""
    select new br.com.guilda.dto.RespostaRelatorioMissao(
        m.id,
        m.titulo,
        cast(m.status as string),
        cast(m.nivelPerigo as string),
        count(p),
        coalesce(sum(p.recompensaOuro), 0)
    )
    from ContratoMissao m
    left join m.participacoes p
    group by m.id, m.titulo, m.status, m.nivelPerigo
    order by m.titulo asc
""")
    List<RespostaRelatorioMissao> gerarRelatorioMissoes();

    @Query("""
    select new br.com.guilda.dto.RespostaRelatorioMissao(
        m.id,
        m.titulo,
        cast(m.status as string),
        cast(m.nivelPerigo as string),
        count(p),
        coalesce(sum(p.recompensaOuro), 0)
    )
    from ContratoMissao m
    left join m.participacoes p
    where m.createdAt >= :inicio
      and m.createdAt < :fim
    group by m.id, m.titulo, m.status, m.nivelPerigo
    order by m.titulo asc
""")
    List<RespostaRelatorioMissao> gerarRelatorioMissoesPorPeriodo(@Param("inicio") OffsetDateTime inicio,
                                                                  @Param("fim") OffsetDateTime fim);
}

