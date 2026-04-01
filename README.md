# API de Votação - Spring Boot + MariaDB

Projeto para o teste técnico de votação: abertura de sessão, recebimento de votos, apuração de resultado e persistência em banco de dados.


## Stack

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- MariaDB
- Spring Validation
- Lombok
- Swagger / OpenAPI
- JUnit 5 + Mockito

## Funcionalidades implementadas

- Cadastrar nova pauta
- Abrir sessão de votação por pauta
- Duração default de 1 minuto quando não informada
- Receber votos `SIM` ou `NAO`
- Restringir 1 voto por associado em cada pauta
- Apurar resultado da votação
- Persistir dados em MariaDB
- Documentação Swagger
- Tratamento global de exceções

## Estrutura do projeto

```text
src/main/java/com/example/votacao
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
└── service
```

## Como subir o MariaDB

Na raiz do projeto:

```bash
docker compose up -d
```

Isso sobe um MariaDB com:

- database: `votacao_db`
- user: `root`
- password: `root`
- port: `3306`

## Como executar a aplicação

### 1. Suba o banco

```bash
docker compose up -d
```

### 2. Rode a aplicação

```bash
mvn spring-boot:run
```

Ou gere o jar:

```bash
mvn clean package
java -jar target/votacao-api-1.0.0.jar
```

## Configuração padrão

Arquivo: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/votacao_db?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

## Swagger

Após subir a aplicação:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Endpoints

### Criar pauta

```http
POST /api/pautas
Content-Type: application/json
```

Body:

```json
{
  "titulo": "Reajuste anual",
  "descricao": "Deliberação sobre reajuste anual"
}
```

### Buscar pauta

```http
GET /api/pautas/{pautaId}
```

### Abrir sessão

```http
POST /api/pautas/{pautaId}/sessao
Content-Type: application/json
```

Body com duração explícita:

```json
{
  "duracaoMinutos": 5
}
```

Body vazio ou ausente = duração padrão de 1 minuto.

### Consultar sessão

```http
GET /api/pautas/{pautaId}/sessao
```

### Registrar voto

```http
POST /api/pautas/{pautaId}/votos
Content-Type: application/json
```

```json
{
  "associadoId": 123,
  "voto": "SIM"
}
```

Valores aceitos para `voto`:

- `SIM`
- `NAO`

### Obter resultado

```http
GET /api/pautas/{pautaId}/resultado
```

Resposta exemplo:

```json
{
  "pautaId": 1,
  "tituloPauta": "Reajuste anual",
  "votosSim": 10,
  "votosNao": 5,
  "totalVotos": 15,
  "resultado": "SIM venceu"
}
```

## Regras de negócio aplicadas

- Uma pauta precisa existir antes da abertura da sessão
- Só pode existir uma sessão por pauta
- Se a duração não for informada, a sessão dura 1 minuto
- O associado pode votar apenas uma vez por pauta
- Só é possível votar com sessão aberta

## Respostas de erro

### 404 - recurso não encontrado

```json
{
  "timestamp": "2026-04-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pauta não encontrada para o id 99",
  "path": "/api/pautas/99",
  "fields": null
}
```

### 400 - regra de negócio

```json
{
  "timestamp": "2026-04-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "O associado já votou nesta pauta",
  "path": "/api/pautas/1/votos",
  "fields": null
}
```

## Observações de arquitetura

A solução foi mantida simples e objetiva para aderir ao critério do teste de evitar overengineering. O projeto separa controller, service, repository, DTOs e tratamento global de exceções, com persistência em banco relacional para garantir sobrevivência a restart da aplicação.
