package br.com.guilda.util;

import br.com.guilda.model.IntegranteAventureiro;
import br.com.guilda.model.TipoClasseAventureiro;
import br.com.guilda.model.AliadoCompanheiro;
import br.com.guilda.model.TipoEspecieCompanheiro;
import br.com.guilda.repository.RepositorioIntegranteAventureiro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GeradorHistoricoDados implements CommandLineRunner {
    private final RepositorioIntegranteAventureiro aventureiros;

    public GeradorHistoricoDados(RepositorioIntegranteAventureiro repository) {
        this.aventureiros = repository;
    }

    @Override
    public void run(String... argumentos) {
        List<IntegranteAventureiro> registrosIniciais = new ArrayList<>();
        TipoClasseAventureiro[] classesDisponiveis = TipoClasseAventureiro.values();
        TipoEspecieCompanheiro[] especiesDisponiveis = TipoEspecieCompanheiro.values();

        for (long contador = 1; contador <= 100; contador++) {
            IntegranteAventureiro aventureiroFake = new IntegranteAventureiro(
                    contador,
                    "IntegranteAventureiro " + contador,
                    classesDisponiveis[(int) ((contador - 1) % classesDisponiveis.length)],
                    (int) ((contador % 20) + 1),
                    contador % 7 != 0
            );

            if (contador % 4 == 0) {
                aventureiroFake.setCompanheiro(new AliadoCompanheiro(
                        "AliadoCompanheiro " + contador,
                        especiesDisponiveis[(int) ((contador - 1) % especiesDisponiveis.length)],
                        (int) (50 + (contador % 51))
                ));
            }

            registrosIniciais.add(aventureiroFake);
        }

        //aventureiros.seed(registrosIniciais);
    }
}

