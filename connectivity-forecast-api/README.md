# Connectivity Forecast API

API em Java (Spring Boot) que entrega **previsões da qualidade de conexão de
internet** por horário, a partir de dados de exemplo. É a versão Java da API de
referência usada pelo aplicativo mobile da squad, desenvolvida na disciplina de
Análise e Projeto de Sistemas II.

## Sobre o projeto (em linguagem simples)

Imagine poder perguntar "como vai estar minha internet às 19h?" e receber uma
resposta fácil de entender. Esta API é a parte que responde a essa pergunta.

Ela recebe pedidos do aplicativo (por exemplo: "qual a previsão para este
horário e esta região?") e devolve uma resposta organizada com:

- uma **classificação** simples da conexão: boa, regular ou instável;
- o **tempo de resposta** e a **perda de dados** previstos;
- uma **recomendação** de uso (usar normalmente, reduzir o uso ou se preparar
  para ficar sem conexão).

## Como executar

**Requisitos:** Java 17 e Maven 3.9 ou superior.

Na pasta do projeto:

```bash
# roda os testes automatizados
mvn test

# sobe a API localmente
mvn spring-boot:run
```

Com a API no ar, acesse no navegador:

- **Documentação interativa (Swagger):** http://localhost:8080/swagger-ui.html
- **Contrato OpenAPI (JSON):** http://localhost:8080/v3/api-docs

## Documentação da API

A documentação é gerada automaticamente a partir do código (OpenAPI/Swagger) e
pode ser explorada na interface do Swagger, com os endpoints, parâmetros e
exemplos. As falhas de negócio seguem um formato de erro padrão, com um código
estável (por exemplo, `MODEL_NOT_FOUND`) e uma mensagem.

A API é versionada em `/api/v1` e oferece os recursos de: verificação de saúde
(health), catálogo de modelos, localizações (probes), previsões e verificação de
atividades.

## Como funciona por dentro

A API lê dois arquivos de dados de exemplo (fixtures):

- **catálogo de modelos** (`data/models.json`): 4 modelos ativos e 1 inativo;
- **previsões** (`data/predictions.csv`): 960 previsões (10 pontos de medição ×
  24 horários, para cada modelo ativo).

O código é separado em camadas: **controllers** (recebem as requisições),
**services** (regras de negócio), **repositories** (leitura dos dados) e
**models/DTOs** (formato dos dados).

## Testes e cobertura

O projeto inclui testes automatizados (unitários, de serviço e de contrato
HTTP). Para rodar os testes e medir a cobertura:

```bash
mvn verify
```

O relatório de cobertura (JaCoCo) é gerado em `target/site/jacoco/index.html`.

## Limitações

Para quem está lendo pela primeira vez, atenção ao que **não** é funcionalidade
pronta:

- os **dados são estáticos e fictícios** (arquivos de exemplo), não medições
  reais;
- a API **não consulta o RIPE Atlas**, não treina nem executa modelos de
  previsão e **não usa banco de dados**;
- a localização de referência vem de um ponto de medição público e **não** é a
  localização exata do usuário;
- a classificação de qualidade e as recomendações são **regras experimentais**,
  não padrões científicos;
- não há autenticação e não é necessário deploy — a API roda localmente.

## Licença e créditos

Reimplementação em Java da
[API de referência](https://github.com/LiniiS/connectivity-forecast-api). A
licença MIT da API de origem está preservada no arquivo `THIRD_PARTY_NOTICES.md`.
