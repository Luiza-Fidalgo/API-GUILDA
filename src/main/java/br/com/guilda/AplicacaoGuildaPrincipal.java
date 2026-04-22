package br.com.guilda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AplicacaoGuildaPrincipal {

    public static void main(String[] argumentos) {
        SpringApplication.run(AplicacaoGuildaPrincipal.class, argumentos);
    }
}

