package br.com.guilda.repository;

import br.com.guilda.model.VinculoUsuarioPerfil;
import br.com.guilda.model.ChaveUsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioVinculoUsuarioPerfil extends JpaRepository<VinculoUsuarioPerfil, ChaveUsuarioPerfil> {
}
