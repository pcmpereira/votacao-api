# 🗳️ API de Votação - Cooperativa

API REST para gerenciamento de pautas e sessões de votação em assembleias, permitindo cadastro de pautas, abertura de sessões, registro de votos e apuração de resultados.

---

## 🚀 Tecnologias utilizadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- MariaDB
- Docker / Docker Compose
- Maven
- Swagger (OpenAPI)

---

## 🧱 Arquitetura

A aplicação segue uma arquitetura em camadas:

- **Controller** → exposição dos endpoints REST  
- **Service** → regras de negócio  
- **Repository** → acesso a dados  
- **Entity (Domain)** → modelagem do domínio  

### Princípios aplicados

- Separação de responsabilidades (SRP)
- Clean Code
- Validação de regras de negócio na camada de serviço
- Simplicidade (sem overengineering)

---

## ⚙️ Como executar o projeto

### 🔹 1. Subir o banco de dados

```bash
docker compose up -d
```

---

### 🔹 2. Rodar a aplicação

```bash
mvn spring-boot:run
```

---

### 🔹 3. Acessar documentação (Swagger)

http://localhost:8080/swagger-ui/index.html

---

## 🗄️ Configuração do banco

- Host: localhost  
- Porta: 3306  
- Database: votacao_db  
- Usuário: root  
- Senha: root  

---

## 📌 Funcionalidades

- ✔ Cadastrar pauta  
- ✔ Abrir sessão de votação  
- ✔ Receber votos (Sim/Não)  
- ✔ Garantir 1 voto por associado por pauta  
- ✔ Apurar resultado  
- ✔ Persistência em banco  

---

## 🔗 Endpoints

### 🧾 Criar pauta

POST /api/pautas

**Body:**
```json
{
  "titulo": "Aprovar orçamento 2026",
  "descricao": "Discussão financeira anual"
}
```

---

### ⏱ Abrir sessão

POST /api/pautas/{id}/sessao

Com duração customizada:

POST /api/pautas/{id}/sessao?duracaoMinutos=5

---

### 🗳 Registrar voto

POST /api/pautas/{id}/votos

**Body:**
```json
{
  "associadoId": 1001,
  "voto": "SIM"
}
```

Valores possíveis:
- `SIM`
- `NAO`

---

### 📊 Consultar resultado

GET /api/pautas/{id}/resultado

**Resposta:**
```json
{
  "pautaId": 1,
  "sim": 2,
  "nao": 1,
  "resultado": "SIM venceu"
}
```

---

## 🧪 Regras de negócio

- Um associado pode votar apenas uma vez por pauta  
- Votos só são aceitos com sessão aberta  
- Sessão expira automaticamente  
- Apenas votos "SIM" ou "NAO" são válidos  
- Resultado por maioria simples  

---

## ⚠️ Tratamento de erros

Exemplo de erro:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Sessão de votação encerrada"
}
```

Casos tratados:

- Pauta não encontrada (404)  
- Sessão inexistente/expirada (400)  
- Voto duplicado (409)  
- Erro interno (500)  

---

## 🧪 Testes

Inclui testes unitários básicos para validação das regras principais.

---

## 📊 Persistência

Os dados são armazenados em MariaDB e permanecem após reinício da aplicação.

---

## 📌 Considerações técnicas

- Estrutura simples e objetiva  
- Código organizado e legível  
- Preparado para evolução futura  
- Foco em clareza e manutenção  

---

## 🔮 Melhorias futuras

- Integração com validação de CPF  
- Cache para performance  
- Versionamento de API  
- Testes de carga  
- Controle de concorrência  

---

## 👨‍💻 Autor

Projeto desenvolvido como parte de avaliação técnica para vaga de desenvolvedor backend.
Paulo Pereira
