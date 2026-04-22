package br.com.guilda.repository;

import br.com.guilda.model.IntegranteAventureiro;
import br.com.guilda.model.TipoClasseAventureiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioIntegranteAventureiro extends JpaRepository<IntegranteAventureiro, Long> {

    Page<IntegranteAventureiro> findByAtivo(Boolean ativo, Pageable pageable);

    Page<IntegranteAventureiro> findByClasse(TipoClasseAventureiro classe, Pageable pageable);

    Page<IntegranteAventureiro> findByNivelGreaterThanEqual(Integer nivel, Pageable pageable);

    Page<IntegranteAventureiro> findByAtivoAndClasse(Boolean ativo, TipoClasseAventureiro classe, Pageable pageable);

    Page<IntegranteAventureiro> findByAtivoAndNivelGreaterThanEqual(Boolean ativo, Integer nivel, Pageable pageable);

    Page<IntegranteAventureiro> findByClasseAndNivelGreaterThanEqual(TipoClasseAventureiro classe, Integer nivel, Pageable pageable);

    Page<IntegranteAventureiro> findByAtivoAndClasseAndNivelGreaterThanEqual(
            Boolean ativo,
            TipoClasseAventureiro classe,
            Integer nivel,
            Pageable pageable
    );

    Page<IntegranteAventureiro> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}

