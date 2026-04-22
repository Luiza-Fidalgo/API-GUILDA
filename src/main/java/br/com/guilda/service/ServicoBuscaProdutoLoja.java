package br.com.guilda.service;

import br.com.guilda.dto.RespostaAgrupamentoQuantidade;
import br.com.guilda.dto.RespostaFaixaPreco;
import br.com.guilda.dto.RespostaPrecoMedio;
import br.com.guilda.dto.RespostaProdutoLoja;
import br.com.guilda.model.DocumentoProdutoLoja;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServicoBuscaProdutoLoja {

    private static final String INDICE_LOJA = "guilda_loja";
    private static final String AGREGACAO_CATEGORIA = "por_categoria";
    private static final String AGREGACAO_RARIDADE = "por_raridade";
    private static final String AGREGACAO_PRECO_MEDIO = "preco_medio";
    private static final String AGREGACAO_FAIXAS_PRECO = "faixas_preco";

    private final ElasticsearchClient elasticsearch;

    public ServicoBuscaProdutoLoja(ElasticsearchClient client) {
        this.elasticsearch = client;
    }

    public List<RespostaProdutoLoja> buscarPorNome(String termo) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .match(match -> match
                                        .field("nome")
                                        .query(termo)
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaProdutoLoja> buscarPorDescricao(String termo) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .match(match -> match
                                        .field("descricao")
                                        .query(termo)
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaProdutoLoja> buscarPorFraseExata(String termo) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .matchPhrase(frase -> frase
                                        .field("descricao")
                                        .query(termo)
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaProdutoLoja> buscarFuzzyPorNome(String termo) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .fuzzy(fuzzy -> fuzzy
                                        .field("nome")
                                        .value(termo)
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaProdutoLoja> buscarEmMultiplosCampos(String termo) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .multiMatch(multicampo -> multicampo
                                        .query(termo)
                                        .fields("nome", "descricao")
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaProdutoLoja> buscarPorDescricaoComCategoria(String termo, String categoria) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .bool(composicao -> composicao
                                        .must(obrigatorio -> obrigatorio
                                                .match(match -> match
                                                        .field("descricao")
                                                        .query(termo)
                                                )
                                        )
                                        .filter(filtro -> filtro
                                                .term(termoExato -> termoExato
                                                        .field("categoria")
                                                        .value(categoria)
                                                )
                                        )
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaProdutoLoja> buscarPorFaixaDePreco(Double min, Double max) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .range(intervalo -> intervalo
                                        .field("preco")
                                        .gte(JsonData.of(min))
                                        .lte(JsonData.of(max))
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaProdutoLoja> buscarAvancada(String categoria,
                                                    String raridade,
                                                    Double min,
                                                    Double max) throws IOException {
        SearchResponse<DocumentoProdutoLoja> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .query(consulta -> consulta
                                .bool(composicao -> composicao
                                        .filter(filtro -> filtro
                                                .term(termo -> termo
                                                        .field("categoria")
                                                        .value(categoria)
                                                )
                                        )
                                        .filter(filtro -> filtro
                                                .term(termo -> termo
                                                        .field("raridade")
                                                        .value(raridade)
                                                )
                                        )
                                        .filter(filtro -> filtro
                                                .range(intervalo -> intervalo
                                                        .field("preco")
                                                        .gte(JsonData.of(min))
                                                        .lte(JsonData.of(max))
                                                )
                                        )
                                )
                        ),
                DocumentoProdutoLoja.class
        );

        return converterProdutos(retorno);
    }

    public List<RespostaAgrupamentoQuantidade> agruparPorCategoria() throws IOException {
        SearchResponse<Void> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .size(0)
                        .aggregations(AGREGACAO_CATEGORIA, agregacao -> agregacao
                                .terms(termos -> termos
                                        .field("categoria")
                                        .size(20)
                                )
                        ),
                Void.class
        );

        return converterAgrupamento(retorno, AGREGACAO_CATEGORIA);
    }

    public List<RespostaAgrupamentoQuantidade> agruparPorRaridade() throws IOException {
        SearchResponse<Void> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .size(0)
                        .aggregations(AGREGACAO_RARIDADE, agregacao -> agregacao
                                .terms(termos -> termos
                                        .field("raridade")
                                        .size(20)
                                )
                        ),
                Void.class
        );

        return converterAgrupamento(retorno, AGREGACAO_RARIDADE);
    }

    public RespostaPrecoMedio calcularPrecoMedio() throws IOException {
        SearchResponse<Void> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .size(0)
                        .aggregations(AGREGACAO_PRECO_MEDIO, agregacao -> agregacao
                                .avg(media -> media.field("preco"))
                        ),
                Void.class
        );

        Double valorMedio = retorno.aggregations()
                .get(AGREGACAO_PRECO_MEDIO)
                .avg()
                .value();

        BigDecimal precoMedio = valorMedio == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(valorMedio);

        return new RespostaPrecoMedio(precoMedio);
    }

    public List<RespostaFaixaPreco> agruparPorFaixaDePreco() throws IOException {
        SearchResponse<Void> retorno = elasticsearch.search(busca -> busca
                        .index(INDICE_LOJA)
                        .size(0)
                        .aggregations(AGREGACAO_FAIXAS_PRECO, agregacao -> agregacao
                                .range(intervalo -> intervalo
                                        .field("preco")
                                        .ranges(faixa -> faixa.to("100"))
                                        .ranges(faixa -> faixa.from("100").to("300"))
                                        .ranges(faixa -> faixa.from("300").to("700"))
                                        .ranges(faixa -> faixa.from("700"))
                                )
                        ),
                Void.class
        );

        List<RespostaFaixaPreco> faixasCalculadas = new ArrayList<>();
        var agrupamentos = retorno.aggregations()
                .get(AGREGACAO_FAIXAS_PRECO)
                .range()
                .buckets()
                .array();

        for (int posicao = 0; posicao < agrupamentos.size(); posicao++) {
            faixasCalculadas.add(new RespostaFaixaPreco(
                    descreverFaixa(posicao),
                    agrupamentos.get(posicao).docCount()
            ));
        }

        return faixasCalculadas;
    }

    private List<RespostaAgrupamentoQuantidade> converterAgrupamento(SearchResponse<Void> retorno,
                                                                     String nomeAgregacao) {
        List<RespostaAgrupamentoQuantidade> agrupamentos = new ArrayList<>();
        var buckets = retorno.aggregations()
                .get(nomeAgregacao)
                .sterms()
                .buckets()
                .array();

        for (var bucket : buckets) {
            agrupamentos.add(new RespostaAgrupamentoQuantidade(
                    bucket.key().stringValue(),
                    bucket.docCount()
            ));
        }

        return agrupamentos;
    }

    private String descreverFaixa(int posicao) {
        if (posicao == 0) {
            return "Abaixo de 100";
        }

        if (posicao == 1) {
            return "De 100 a 300";
        }

        if (posicao == 2) {
            return "De 300 a 700";
        }

        return "Acima de 700";
    }

    private List<RespostaProdutoLoja> converterProdutos(SearchResponse<DocumentoProdutoLoja> retorno) {
        return retorno.hits().hits().stream()
                .map(hit -> hit.source())
                .filter(documento -> documento != null)
                .map(this::converterProduto)
                .toList();
    }

    private RespostaProdutoLoja converterProduto(DocumentoProdutoLoja documento) {
        return new RespostaProdutoLoja(
                documento.getNome(),
                documento.getDescricao(),
                documento.getCategoria(),
                documento.getRaridade(),
                documento.getPreco()
        );
    }
}

