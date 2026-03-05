# Desafio Técnico Sicredi — Sistema de Votação

API REST para gerenciamento de sessões de votação em assembleias cooperativistas.

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.3 |
| Spring Cloud OpenFeign | 2024.0.0 |
| PostgreSQL | 15+ |
| Apache Kafka | 3.x |
| Flyway | (gerenciado pelo Spring Boot) |
| springdoc-openapi | 2.8.5 |
| Lombok | (gerenciado pelo Spring Boot) |

---

## Arquitetura

O projeto segue a **Arquitetura Hexagonal (Ports & Adapters)**:

```
adapters/input/{domínio}/controllers/   → @RestController
adapters/input/{domínio}/protocols/     → Request/Response records
adapters/output/{domínio}/adapters/     → implementam OutputGateway
adapters/output/{domínio}/entities/     → JPA Entity
adapters/output/{domínio}/repository/   → JpaRepository + impl
application/core/{domínio}/domain/      → Domínio + Command
application/core/{domínio}/ports/input/ → InputGateway interfaces
application/core/{domínio}/ports/output/→ OutputGateway interfaces
application/core/{domínio}/usecases/    → UseCase (implementa InputGateway)
application/core/{domínio}/exceptions/  → Exceções de domínio
config/usecases/                        → @Bean configs dos UseCases
config/infra/                           → Kafka, Swagger, Scheduler
```

A camada `application/core` não possui nenhuma dependência de infraestrutura.

---

## Pré-requisitos

- Java 21
- Maven 3.9+ (ou usar o `./mvnw` incluso)
- Docker e Docker Compose

---

## Subindo a infraestrutura

O projeto depende de **PostgreSQL** e **Kafka**. O arquivo de composição está em [`composer/docker-compose.yml`](composer/docker-compose.yml).

```bash
docker-compose -f composer/docker-compose.yml up -d
```

---

## Rodando a aplicação

```bash
./mvnw spring-boot:run
```

Ou compilando e executando o JAR:

```bash
./mvnw clean package -DskipTests
java -jar target/votacao-0.0.1-SNAPSHOT.jar
```

A aplicação sobe na porta **8080** por padrão.

As migrations do Flyway são executadas automaticamente na inicialização.

---

## Variáveis de ambiente

Todas possuem valor padrão para ambiente local:

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5433/votacao` | URL do banco |
| `DB_USERNAME` | `votacao` | Usuário do banco |
| `DB_PASSWORD` | `votacao` | Senha do banco |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Endereço do Kafka |
| `KAFKA_TOPIC_VOTING_RESULT` | `voting-result` | Tópico de resultado |
| `SCHEDULER_SESSION_RESULT_DELAY_MS` | `30000` | Intervalo do scheduler (ms) |
| `CPF_VALIDATION_URL` | `https://user-info.herokuapp.com` | URL da API externa de CPF |

---

## Documentação da API

Com a aplicação rodando, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

---

## Endpoints

### Pautas

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/pautas` | Cadastra uma nova pauta |
| `GET` | `/api/v1/pautas` | Lista todas as pautas |
| `GET` | `/api/v1/pautas/{id}` | Busca pauta por ID |

### Sessões

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/pautas/{pautaId}/sessoes` | Abre uma sessão de votação |

> Body opcional. Se `duracaoMinutos` não for informado, a sessão dura **1 minuto** por padrão.

### Votos

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/pautas/{pautaId}/votes` | Registra o voto de um associado |

```json
{
  "cpf": "51402671029",
  "option": "SIM"
}
```

Opções válidas: `SIM` ou `NAO`.

### Resultado

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/pautas/{pautaId}/result` | Retorna o resultado da votação |

---

## Regras de negócio

- Cada associado pode votar **apenas uma vez** por pauta (controlado por constraint única no banco)
- Só é possível votar com uma **sessão aberta** para a pauta
- O CPF do associado é validado na API externa antes do voto ser aceito
- O resultado (`SIM`, `NAO` ou `EMPATE`) é publicado automaticamente no Kafka quando a sessão encerra

---

## Validação de CPF (Bônus 1)

A cada voto, o CPF é verificado na API externa `GET https://user-info.herokuapp.com/users/{cpf}`.

**Estratégia de fallback** (caso a API esteja indisponível):
1. CPF matematicamente inválido → rejeita com `422`
2. CPF matematicamente válido → aceita com `WARN` no log

---

## Mensageria Kafka (Bônus 2)

Um `@Scheduled` roda a cada 30 segundos verificando sessões encerradas com `resultado_publicado = false`.
Para cada sessão encontrada, publica no tópico `voting-result` o seguinte evento:

```json
{
  "pautaId": "uuid",
  "simCount": 10,
  "naoCount": 3,
  "total": 13,
  "winner": "SIM",
  "publishedAt": "2026-03-05T10:00:00"
}
```

Após a publicação, marca `resultado_publicado = true` na sessão — garantindo **exactly-once** de publicação.

---

## Banco de dados

As migrations estão em `src/main/resources/db/migration` e são aplicadas automaticamente pelo Flyway.

| Tabela | Descrição |
|---|---|
| `pauta` | Pautas cadastradas |
| `sessao` | Sessões de votação vinculadas a pautas |
| `voto` | Votos registrados (unique por `pauta_id + cpf_associado`) |

---

## Tratamento de erros

Todos os erros seguem o padrão **RFC 9457 (Problem Details)**:

```json
{
  "type": "https://sicredi.com.br/errors/pauta-nao-encontrada",
  "title": "Pauta não encontrada",
  "status": 404,
  "detail": "Pauta não encontrada: 123e4567-e89b-..."
}
```

| Situação | Status |
|---|---|
| Pauta não encontrada | `404` |
| Sessão não encontrada | `404` |
| Sessão já aberta para a pauta | `409` |
| Sessão encerrada | `422` |
| Voto duplicado | `409` |
| CPF inválido | `422` |
| Associado não elegível | `403` |
| Erro de validação de request | `400` |
