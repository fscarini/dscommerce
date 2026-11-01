# DSCommerce

API REST de e-commerce desenvolvida em **Java 21** e **Spring Boot 4**, com autenticação e autorização via **OAuth2 + JWT**. O projeto cobre o ciclo básico de uma loja virtual: catálogo de produtos e categorias, cadastro de pedidos e controle de acesso por perfil de usuário.

Projeto construído durante os estudos de Spring Boot, aplicando arquitetura em camadas, tratamento de exceções, validação de dados e Spring Security.

## Tecnologias

- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- Spring Authorization Server (OAuth2) e Resource Server (JWT)
- Bean Validation (Jakarta Validation)
- H2 Database (banco em memória)
- Maven

## Arquitetura

O projeto segue a arquitetura em camadas:

```
controllers  →  services  →  repositories  →  entities
     ↑              ↑
    dtos       exceptions
```

| Pacote | Responsabilidade |
|---|---|
| `controllers` | Endpoints REST e tratamento global de exceções (`ControllerExceptionHandler`) |
| `services` | Regras de negócio e transações |
| `repositories` | Acesso a dados com Spring Data JPA |
| `entities` | Mapeamento objeto-relacional (JPA) |
| `dtos` | Objetos de transferência de dados entre API e cliente |
| `projections` | Consultas leves ao banco (ex.: usuário e perfis para login) |
| `config` | Configuração do Authorization Server, Resource Server e CORS |

## Modelo de domínio

- **User** — cliente da loja, com um ou mais perfis (**Role**)
- **Product** — produto do catálogo, associado a uma ou mais **Category**
- **Order** — pedido feito por um usuário, com status (`WAITING_PAYMENT`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELED`)
- **OrderItem** — item do pedido (produto, quantidade e preço), com chave composta (`OrderItemPK`)
- **Payment** — pagamento associado a um pedido

## Endpoints

### Autenticação

| Método | Rota | Descrição |
|---|---|---|
| POST | `/oauth2/token` | Gera o token JWT (grant type `password`) |

### Produtos

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/products?name=&page=&size=&sort=` | Público | Lista paginada, com busca por nome |
| GET | `/products/{id}` | Público | Detalhes do produto com categorias |
| POST | `/products` | ADMIN | Cadastra produto |
| PUT | `/products/{id}` | ADMIN | Atualiza produto |
| DELETE | `/products/{id}` | ADMIN | Remove produto |

### Categorias

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/categories` | CLIENT, ADMIN | Lista todas as categorias |

### Pedidos

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/orders/{id}` | CLIENT, ADMIN | Busca pedido por id (o cliente só acessa os próprios pedidos) |
| POST | `/orders` | CLIENT, ADMIN | Cria um novo pedido |

### Usuários

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/users/me` | CLIENT, ADMIN | Dados do usuário logado |

## Segurança

A aplicação atua ao mesmo tempo como **Authorization Server** (emite os tokens) e **Resource Server** (valida os tokens nas rotas protegidas).

- Login com grant type customizado `password` (usuário e senha + credenciais do client)
- Token JWT contendo os perfis do usuário (`authorities`)
- Controle de acesso por rota com `@PreAuthorize`
- Regra "self or admin": um cliente só consulta os próprios pedidos; o admin consulta qualquer um
- Senhas armazenadas com BCrypt
- CORS configurável por variável de ambiente

## Tratamento de erros

As exceções são tratadas de forma centralizada e retornam respostas padronizadas:

| Status | Situação |
|---|---|
| 400 | Violação de integridade no banco (`DatabaseException`) |
| 403 | Acesso negado a recurso de outro usuário (`ForbiddenException`) |
| 404 | Recurso não encontrado (`ResourceNotFoundException`) |
| 422 | Erro de validação, com a lista de campos inválidos |

Exemplo de erro de validação:

```json
{
  "timestamp": "2026-11-01T17:00:00Z",
  "status": 422,
  "error": "Dados inválidos",
  "path": "/products",
  "errors": [
    { "fieldName": "price", "message": "O preço deve ser positivo" }
  ]
}
```

## Como executar

### Pré-requisitos

- Java 21+
- Maven (ou o wrapper `mvnw` incluso no projeto)

### Passos

```bash
# Clonar o repositório
git clone https://github.com/fscarini/dscommerce.git
cd dscommerce

# Executar (Linux/macOS)
./mvnw spring-boot:run

# Executar (Windows)
mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080`.

O perfil padrão é `test`, que usa o banco H2 em memória já populado pelo `import.sql`. O console do H2 fica em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, usuário `sa`, sem senha).

### Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `APP_PROFILE` | `test` | Perfil ativo do Spring |
| `CLIENT_ID` | `myclientid` | Client id do OAuth2 |
| `CLIENT_SECRET` | `myclientsecret` | Client secret do OAuth2 |
| `JWT_DURATION` | `86400` | Validade do token, em segundos |
| `CORS_ORIGINS` | `http://localhost:3000,http://localhost:5173` | Origens permitidas |

## Testando a API

### Usuários de teste

| Usuário | Senha | Perfis |
|---|---|---|
| `maria@gmail.com` | `123456` | CLIENT |
| `alex@gmail.com` | `123456` | CLIENT, ADMIN |

### 1. Obter o token

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -u myclientid:myclientsecret \
  -d "grant_type=password" \
  -d "username=alex@gmail.com" \
  -d "password=123456"
```

### 2. Usar o token

```bash
curl http://localhost:8080/users/me \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### Exemplo: criar um pedido

```bash
curl -X POST http://localhost:8080/orders \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      { "productId": 1, "quantity": 2 },
      { "productId": 5, "quantity": 1 }
    ]
  }'
```

## Autor

**Fernando Silva Carini**

[![GitHub](https://img.shields.io/badge/GitHub-fscarini-181717?logo=github)](https://github.com/fscarini)
