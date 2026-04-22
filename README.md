# Explicacao detalhada do funcionamento da API da Guilda

## Visao geral

Este projeto e uma API REST criada em Java 17 com Spring Boot. O sistema representa uma Guilda de Aventureiros, com cadastro de aventureiros, companheiros, missoes, participacoes em missoes, relatorios, ranking de participacao, consultas de produtos de loja em Elasticsearch e um painel tatico baseado em uma view de banco.

## Tecnologias e dependencias

O arquivo pom.xml declara as principais dependencias:

- Spring Boot Starter Web: cria a API HTTP REST.
- Spring Boot Starter Validation: fornece suporte a validacoes.
- Spring Boot Starter Data JPA: integra a aplicacao com banco relacional via JPA/Hibernate.
- PostgreSQL Driver: permite conexao com PostgreSQL.
- Elasticsearch Java Client 8.13.4: usado nas buscas e agregacoes de produtos.
- Apache HttpClient 5: suporte HTTP usado pelo cliente Elasticsearch.
- Spring Boot Starter Test: JUnit, Mockito e ferramentas de teste.

A configuracao em src/main/resources/application.properties aponta para PostgreSQL local:

- URL: jdbc:postgresql://localhost:5432/postgres
- Usuario: postgres
- Senha: postgres
- Hibernate ddl-auto: update
- SQL formatado e exibido no console.

O Elasticsearch e configurado por ConfiguracaoElasticLoja para conectar em localhost:9200 usando HTTP.

## Estrutura em camadas

O projeto segue uma estrutura classica do Spring:

- controller: recebe requisicoes HTTP, extrai parametros e devolve ResponseEntity.
- service: concentra regras de negocio, validacoes, conversoes e orquestracao.
- repository: interfaces Spring Data JPA, responsaveis por consultas e persistencia.
- model: entidades JPA, enums, chaves compostas e documentos de busca.
- dto: objetos de entrada e saida da API.
- exception: excecoes de dominio e handler global.
- config: beans de configuracao, como ElasticsearchClient.
- util: utilitario historico para geracao de dados ficticios.

## Inicializacao da aplicacao

A classe AplicacaoGuildaPrincipal e o ponto de entrada. Ela tem:

- @SpringBootApplication: ativa auto-configuracao, component scan e configuracao Spring.
- @EnableCaching: habilita cache usado no painel tatico.
- main: chama SpringApplication.run para subir a aplicacao.

Quando a aplicacao inicia, o Spring localiza controllers, services, repositories e entidades dentro do pacote br.com.guilda. Depois configura o servidor web, JPA, banco de dados, cache e beans definidos na aplicacao.

## Configuracao do Elasticsearch

A classe ConfiguracaoElasticLoja cria um bean ElasticsearchClient. Internamente:

1. Monta um RestClient apontando para localhost:9200.
2. Cria um RestClientTransport com JacksonJsonpMapper.
3. Retorna um ElasticsearchClient para ser injetado em ServicoBuscaProdutoLoja.

Esse cliente e usado para executar queries match, match_phrase, fuzzy, multi_match, bool, range e agregacoes.

## Fluxo geral de uma requisicao

Uma chamada HTTP segue este caminho:

1. O controller recebe a requisicao.
2. O Spring desserializa o JSON para um DTO de pedido, quando existe body.
3. O controller chama um metodo do service.
4. O service valida os dados e consulta repositories.
5. O repository executa query JPA ou consulta derivada.
6. O service converte entidades para DTOs de resposta.
7. O controller retorna status HTTP, headers e body.
8. Se ocorrer excecao, TratadorGlobalExcecoes transforma em resposta padronizada.

## Endpoints de aventureiros

Controller: PortalAventureiroController
Service: ServicoAventureiroGuilda
Entidade principal: IntegranteAventureiro

### POST /api/guilda

Cria um novo aventureiro.

Body esperado:

```json
{
  "nome": "Marcio",
  "classe": "MAGO",
  "nivel": 5
}
```

Regras:

- nome e obrigatorio.
- classe e obrigatoria e precisa ser um valor de TipoClasseAventureiro.
- nivel e obrigatorio e deve ser maior ou igual a 1.
- o aventureiro criado sempre comeca ativo.
- a organizacao e o usuario de cadastro sao obtidos a partir do primeiro usuario existente no banco.

Resposta:

- HTTP 201 Created.
- Header Location apontando para /api/guilda/{id}.
- Body RespostaDetalheAventureiro.

### GET /api/guilda

Lista aventureiros resumidos.

Parametros opcionais:

- nome: busca parcial case-insensitive.
- classe: filtra por classe.
- ativo: filtra por status ativo/inativo.
- nivelMinimo: filtra por nivel maior ou igual.
- page: pagina, padrao 0.
- size: tamanho, padrao 10.

Regras de paginacao:

- page nao pode ser negativo.
- size precisa ficar entre 1 e 50.
- ordenacao padrao por nome crescente.

Headers de resposta:

- X-Total-Count
- X-Page
- X-Size
- X-Total-Pages

### GET /api/guilda/{id}

Busca um aventureiro por ID e retorna detalhes completos. A resposta inclui:

- dados basicos do aventureiro.
- companheiro atual, se existir.
- total de participacoes em missoes.
- ultima missao encontrada no historico de participacoes.

Se o ID nao existir, retorna 404.

### PUT /api/guilda/{id}

Atualiza nome, classe e nivel de um aventureiro. As mesmas validacoes da criacao sao aplicadas. O status ativo e o companheiro nao sao alterados por esse endpoint.

### PATCH /api/guilda/{id}/encerrar

Marca o aventureiro como inativo. Retorna 204 No Content.

### PATCH /api/guilda/{id}/recrutar

Marca o aventureiro como ativo novamente. Retorna 204 No Content.

### PUT /api/guilda/{id}/companheiro

Define ou substitui o companheiro do aventureiro.

Body esperado:

```json
{
  "nome": "HERAGON",
  "especie": "GOLEM",
  "lealdade": 75
}
```

Regras:

- nome do companheiro é obrigatorio.
- especie precisa ser TipoEspecieCompanheiro.
- lealdade deve ficar entre 0 e 100.
- o companheiro é associado ao aventureiro por relacionamento OneToOne com MapsId.

### DELETE /api/guilda/{id}/companheiro

Remove o companheiro do aventureiro. Como o relacionamento usa orphanRemoval, a entidade dependente pode ser removida junto na persistencia.

## Endpoints de missoes

Controller: PortalMissaoController
Service: ServicoContratoMissao
Entidades principais: ContratoMissao e VinculoParticipacaoMissao

### POST /api/missoes

Cria uma nova missao.

Body esperado:

```json
{
  "titulo": "Dragão na floresta",
  "nivelPerigo": "ALTO"
}
```

Regras:

- titulo e obrigatorio.
- titulo deve ter no maximo 150 caracteres.
- nivelPerigo e obrigatorio e precisa ser TipoNivelPerigo.
- toda missao criada inicia com status PLANEJADA.
- a organizacao vem do primeiro usuario existente no banco.

### POST /api/missoes/{id}/participantes

Adiciona um aventureiro a uma missao.

Body esperado:

```json
{
  "aventureiroId": 1,
  "papelMissao": "LIDER",
  "recompensaOuro": 200.00,
  "destaque": true
}
```

Regras:

- aventureiroId e obrigatorio.
- papelMissao e obrigatorio e precisa ser TipoPapelMissao.
- recompensaOuro nao pode ser negativa quando informada.
- destaque e obrigatorio.
- aventureiro precisa estar ativo.
- aventureiro e missao precisam pertencer a mesma organizacao.
- a missao precisa estar em status PLANEJADA ou EM_ANDAMENTO.
- o mesmo aventureiro nao pode ser adicionado duas vezes a mesma missao.

A chave da participacao e composta por missaoId e aventureiroId.

### GET /api/missoes

Lista missoes resumidas.

Parametros opcionais:

- status
- nivelPerigo
- dataInicio
- dataFim
- page
- size

As datas sao interpretadas como LocalDate em UTC. O fim e convertido para o inicio do dia seguinte, criando um intervalo semiaberto: createdAt >= inicio e createdAt < fim.

O retorno tambem inclui headers de paginacao:

- X-Total-Count
- X-Page
- X-Size
- X-Total-Pages

### GET /api/missoes/{id}

Retorna a missao com seus participantes. Cada participante inclui:

- id do aventureiro.
- nome.
- classe.
- nivel.
- papel na missao.
- recompensa em ouro.
- flag de destaque.

### GET /api/missoes/relatorios/ranking-participacao

Gera ranking de aventureiros por participacao.

Parametros opcionais:

- dataInicio
- dataFim
- status

Se datas forem usadas, as duas precisam ser informadas juntas. O ranking ordena por quantidade de participacoes e, em empate, pelo total de recompensas.

### GET /api/missoes/relatorios/missoes-metricas

Gera metricas por missao. Cada linha retorna:

- id da missao.
- titulo.
- status.
- nivel de perigo.
- quantidade de participantes.
- soma das recompensas.

Tambem aceita dataInicio e dataFim em conjunto.

## Endpoints de produtos no Elasticsearch

Controller: PortalBuscaProdutoController
Service: ServicoBuscaProdutoLoja
Documento: DocumentoProdutoLoja
Indice: guilda_loja

### Buscas textuais

- GET /produtos/busca/nome?termo=x: busca match no campo nome.
- GET /produtos/busca/descricao?termo=x: busca match no campo descricao.
- GET /produtos/busca/frase?termo=x: busca match_phrase em descricao.
- GET /produtos/busca/fuzzy?termo=x: busca fuzzy em nome.
- GET /produtos/busca/multicampos?termo=x: busca multi_match em nome e descricao.

### Buscas com filtro e preco

- GET /produtos/busca/com-filtro?termo=x&categoria=y: descricao deve bater e categoria filtra por termo exato.
- GET /produtos/busca/faixa-preco?min=10&max=200: filtra por range no campo preco.
- GET /produtos/busca/avancada?categoria=x&raridade=y&min=10&max=500: aplica filtros de categoria, raridade e preco.

## Endpoints de agregacoes de produtos

Controller: PortalAgregacaoProdutoController

- GET /produtos/agregacoes/por-categoria: terms aggregation por categoria.
- GET /produtos/agregacoes/por-raridade: terms aggregation por raridade.
- GET /produtos/agregacoes/preco-medio: average aggregation do campo preco.
- GET /produtos/agregacoes/faixas-preco: range aggregation em faixas: abaixo de 100, 100 a 300, 300 a 700 e acima de 700.

## Endpoint de painel tatico

Controller: PortalPainelTaticoController
Service: ServicoPainelTaticoMissao
Entidade: VisaoPainelTaticoMissao

GET /missoes/top15dias

Consulta a view operacoes.vw_painel_tatico_missao e retorna as 10 missoes com maior indice de prontidao atualizadas nos ultimos 15 dias.

O metodo e anotado com:

```java
@Cacheable(value = "topMissoes15Dias", key = "'rankingTopMissoes'")
```

Isso significa que, com cache habilitado, chamadas repetidas podem reutilizar o resultado em vez de consultar a view toda vez.

## Modelo de dominio

### NucleoOrganizacao

Mapeia audit.organizacoes. Representa a organizacao dona dos usuarios, perfis, aventureiros e missoes.

### ContaUsuario

Mapeia audit.usuarios. Guarda dados do usuario, status, senha hash, organizacao e papeis de acesso.

### PerfilAcesso, PermissaoOperacao e vinculos

PerfilAcesso mapeia audit.roles.
PermissaoOperacao mapeia audit.permissions.
VinculoUsuarioPerfil mapeia audit.user_roles.
VinculoPerfilPermissao mapeia audit.role_permissions.

Essas entidades sustentam a estrutura de usuarios, perfis e permissoes.

### IntegranteAventureiro

Mapeia aventura.aventureiros. Possui:

- organizacao.
- usuarioCadastro.
- nome.
- classe.
- nivel.
- ativo.
- createdAt e updatedAt.
- companheiro OneToOne.
- participacoes OneToMany.

Antes de persistir, prePersist preenche createdAt e updatedAt. Antes de atualizar, preUpdate renova updatedAt.

### AliadoCompanheiro

Mapeia aventura.companheiros. Usa @MapsId, entao seu ID e o mesmo ID do aventureiro. Isso reforca que um companheiro nao existe isoladamente.

### ContratoMissao

Mapeia aventura.missoes. Possui titulo, nivelPerigo, status, createdAt, dataInicio, dataFim e participacoes. O createdAt e preenchido automaticamente no prePersist.

### VinculoParticipacaoMissao

Mapeia aventura.participacoes_missao. Liga uma missao a um aventureiro. Usa chave composta ChaveParticipacaoMissao com missaoId e aventureiroId. Tambem guarda papelMissao, recompensaOuro, destaque e createdAt.

### VisaoPainelTaticoMissao

Mapeia a view operacoes.vw_painel_tatico_missao. E marcada com @Immutable porque a aplicacao apenas le os dados da view.

### DocumentoProdutoLoja

Nao e entidade JPA. Representa documentos retornados do indice Elasticsearch guilda_loja com nome, descricao, categoria, raridade e preco.

## Enums do dominio

TipoClasseAventureiro:

- GUERREIRO
- MAGO
- ARQUEIRO
- CLERIGO
- LADINO

TipoEspecieCompanheiro:

- LOBO
- CORUJA
- GOLEM
- DRAGAO_MINIATURA

TipoNivelPerigo:

- BAIXO
- MEDIO
- ALTO
- EXTREMO

TipoPapelMissao:

- LIDER
- COMBATENTE
- SUPORTE
- EXPLORADOR
- CURANDEIRO

TipoStatusMissao:

- PLANEJADA
- EM_ANDAMENTO
- CONCLUIDA
- CANCELADA

## Repositories

Os repositories estendem JpaRepository e usam dois tipos de consulta:

1. Consultas derivadas pelo nome do metodo, como findByAtivo, findByClasse e findByStatusAndNivelPerigo.
2. Queries JPQL com @Query para relatorios, ranking, fetch joins e carregamento de usuarios com roles e permissions.

RepositorioIntegranteAventureiro oferece filtros combinados por ativo, classe e nivel.
RepositorioContratoMissao oferece filtros por status, nivel de perigo e periodo.
RepositorioVinculoParticipacaoMissao calcula ranking, relatorios de participacao e busca participantes com dados do aventureiro.
RepositorioContaUsuario carrega usuario com organizacao, roles e permissions em uma consulta com fetch join.

## Tratamento de erros

TratadorGlobalExcecoes centraliza as respostas de erro:

- ExcecaoRecursoNaoEncontrado vira HTTP 404.
- ExcecaoRequisicaoInvalida vira HTTP 400.
- Qualquer outra Exception vira HTTP 500.

O formato de erro e:

```json
{
  "mensagem": "Solicitacao invalida",
  "detalhes": [
    "classe invalida"
  ]
}
```

## Validacoes importantes

As validacoes estao principalmente nos services:

- Campos obrigatorios sao conferidos antes de acessar banco.
- Valores de enum sao convertidos com valueOf e geram erro se nao existirem.
- Page e size sao limitados para evitar paginacao invalida.
- Aventureiro inativo nao entra em missao.
- Missao concluida ou cancelada nao aceita novos participantes.
- Datas de relatorios precisam vir em pares quando o endpoint exige intervalo completo.

## DTOs e contratos JSON

Os DTOs de pedido recebem dados externos:

- PedidoCriacaoAventureiro
- PedidoAtualizacaoAventureiro
- PedidoCompanheiro
- PedidoCriacaoMissao
- PedidoParticipacaoMissao

Os DTOs de resposta controlam o JSON retornado:

- RespostaResumoAventureiro
- RespostaDetalheAventureiro
- RespostaCompanheiro
- RespostaUltimaMissao
- RespostaResumoMissao
- RespostaDetalheMissao
- RespostaMissao
- RespostaParticipanteMissao
- RespostaRankingParticipacao
- RespostaRelatorioMissao
- RespostaProdutoLoja
- RespostaPrecoMedio
- RespostaFaixaPreco
- RespostaAgrupamentoQuantidade
- RespostaErroApi

Mesmo com classes renomeadas, os atributos internos dos DTOs mantem os nomes de propriedades esperados no JSON.

## Testes

Existem testes de servico e repository em src/test/java. O teste PainelTaticoServicoTest usa Mockito e nao depende de banco real. Os demais testes usam SpringBootTest ou DataJpaTest e dependem de PostgreSQL em localhost:5432.

Na validacao feita apos a refatoracao:

- mvn -q clean compile passou.
- mvn -q test-compile passou.
- mvn -q -Dtest=PainelTaticoServicoTest test passou.
- mvn test falhou porque o PostgreSQL local recusou conexao em localhost:5432.

Isso indica que a compilacao do codigo esta correta, mas a suite completa depende do ambiente de banco.

## Como executar

Requisitos:

- Java 17.
- Maven.
- PostgreSQL rodando em localhost:5432 com usuario postgres e senha postgres.
- Elasticsearch rodando em localhost:9200 para os endpoints de produtos.

Com o banco disponivel, execute:

```bash
mvn spring-boot:run
```

A API sobe em http://localhost:8080.

## Resumo final

A aplicacao e uma API Spring Boot organizada em camadas. Ela persiste dados relacionais no PostgreSQL com JPA, consulta produtos no Elasticsearch, expõe endpoints REST para cadastro e consulta, aplica validacoes nos services e padroniza erros com um handler global. A parte central do dominio envolve aventureiros, companheiros, missoes e participacoes, enquanto as entidades de auditoria e permissoes sustentam usuarios, organizacoes e acesso.
