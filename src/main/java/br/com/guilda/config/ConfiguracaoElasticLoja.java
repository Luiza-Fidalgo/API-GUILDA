package br.com.guilda.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoElasticLoja {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient clienteHttp = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        ElasticsearchTransport transporteJson = new RestClientTransport(
                clienteHttp,
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transporteJson);
    }
}

