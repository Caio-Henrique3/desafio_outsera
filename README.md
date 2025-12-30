# Desafio Técnico - Golden Raspberry Awards

## Requisitos atendidos (checklist)

- [x] Importação do CSV na inicialização da aplicação
- [x] Banco em memória com H2 (sem dependência externa)
- [x] Endpoint REST para intervalos min/max de prêmios
- [x] Swagger UI disponível
- [x] Testes de integração com datasets controlados (independentes do CSV externo)
- [x] Instruções para rodar o projeto

## Decisões técnicas

- JPA + H2 em memória para simplificar execução local e alinhar com os requisitos do desafio.
- Normalização de producers e studios (tabelas separadas) para evitar duplicidade e facilitar agregações.
- Importação do CSV no bootstrap com falha explícita em caso de arquivo ausente ou parsing inválido, garantindo consistência dos dados.
- Swagger via Springdoc para documentação leve e rápida validação da API.

## Como rodar

```bash
mvn spring-boot:run
```

## Como testar

```bash
mvn clean test
(perfil `test` é ativado automaticamente)
```

Os testes de integração usam CSVs pequenos em `src/test/resources/csv`:

- `movielist-no-tie.csv` (cenário sem empate)
- `movielist-tie.csv` (cenário com empate em min/max)

## Endpoint

- `GET /producers/awards-intervals` → 200 OK

Exemplo de retorno:

```json
{
  "min": [
    {
      "producer": "Producer 1",
      "interval": 1,
      "previousWin": 2008,
      "followingWin": 2009
    }
  ],
  "max": [
    {
      "producer": "Producer 2",
      "interval": 99,
      "previousWin": 1900,
      "followingWin": 1999
    }
  ]
}
```

## Swagger / H2 Console

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:desafio`
  - User: `sa`
  - Password: (vazio)

## Versões

- Java: 21
- Spring Boot: 3.5.9

## Observações

- CSV: caminho configurável via `app.csv.path` (padrão: `src/main/resources/csv/movielist.csv`)
- Importação falha no startup caso o CSV esteja ausente ou inválido.
- Nos testes, o CSV é trocado via `@TestPropertySource` para cada cenário.
- Importação não roda se a base já tiver dados (ver `CsvMovieLoader`).
- Perfis:
  - `application.yml` (padrão)
  - `application-test.yml` (ddl-auto: create-drop)
