# 💻 ByteStore - E-commerce Backend API

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=flat-square&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-3.6+-red?style=flat-square&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

**ByteStore** é uma API RESTful completa para gerenciamento de e-commerce, desenvolvida em **Java 17** com **Spring Boot
3.5.6**. O sistema oferece autenticação JWT, CRUD de produtos, gerenciamento de pedidos e relatórios administrativos.

---

## 📑 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Objetivo](#-objetivo)
- [Stack Tecnológica](#-stack-tecnológica)
- [Funcionalidades](#-funcionalidades)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração do Banco de Dados](#-configuração-do-banco-de-dados)
- [Usando o Postman](#-usando-o-postman)
- [Documentação da API](#-documentação-da-api)
- [Autenticação](#-autenticação)
- [Perfis de Usuário](#-perfis-de-usuário)
- [Dados Iniciais](#-dados-iniciais)
- [Autor](#-autor)

---

## 📖 Sobre o Projeto

O **ByteStore** é um sistema de e-commerce simplificado voltado para a venda de produtos de tecnologia e informática.
Desenvolvido como projeto de portfólio, demonstra habilidades práticas em:

- **Desenvolvimento Back-end** com Java e Spring Boot
- **Arquitetura em Camadas** (Clean Architecture)
- **Autenticação e Autorização** com JWT
- **APIs RESTful** seguindo boas práticas
- **Banco de Dados Relacional** com MySQL
- **Consultas SQL Otimizadas** para relatórios
- **Tratamento de Exceções** centralizado
- **Validações** de dados e regras de negócio

O projeto implementa um sistema completo de gestão de produtos e pedidos, com controle de estoque, pagamentos e
relatórios administrativos, utilizando as melhores práticas do ecossistema Spring.

---

## 🎯 Objetivo

Construir uma aplicação RESTful que demonstre habilidades em desenvolvimento back-end, implementando:

### Requisitos Principais

- ✅ **Autenticação segura** com JWT (JSON Web Tokens)
- ✅ **Dois perfis de usuário** (ADMIN e USER) com controle de acesso
- ✅ **CRUD completo de produtos** (acesso exclusivo do perfil ADMIN)
- ✅ **Gerenciamento de pedidos** com:
    - Criação de pedidos com múltiplos produtos
    - Controle de status (PENDENTE, PAGO, CANCELADO)
    - Cálculo automático do valor total
    - Atualização de estoque após pagamento
    - Cancelamento automático por falta de estoque
- ✅ **Consultas SQL otimizadas** para relatórios administrativos:
    - Top 5 usuários que mais compraram
    - Ticket médio de pedidos por usuário
    - Valor total faturado no mês

### Objetivos de Aprendizado

- Demonstrar conhecimento em **Spring Boot** e suas dependências
- Aplicar **arquitetura em camadas** de forma prática
- Implementar **segurança JWT** do zero
- Criar **APIs RESTful** bem estruturadas
- Otimizar **consultas SQL** para performance
- Aplicar **boas práticas** de desenvolvimento Java

---

## 🧩 Stack Tecnológica

### Tecnologias Principais

| Tecnologia      | Versão | Descrição                          |
|-----------------|--------|------------------------------------|
| **Java**        | 17+    | Linguagem de programação           |
| **Spring Boot** | 3.5.6  | Framework Java para aplicações web |
| **MySQL**       | 8.0+   | Banco de dados relacional          |
| **Maven**       | 3.6+   | Gerenciador de dependências        |

### Dependências Spring Boot

- **spring-boot-starter-web** - Framework web REST
- **spring-boot-starter-data-jpa** - Persistência de dados com JPA
- **spring-boot-starter-security** - Segurança e autenticação
- **spring-boot-starter-validation** - Validação de dados

### Dependências Adicionais

- **mysql-connector-j** - Driver JDBC para MySQL
- **jjwt-api** - Biblioteca JWT para Java
- **jjwt-impl** - Implementação JWT
- **jjwt-jackson** - Serialização Jackson para JWT
- **lombok** (Latest) - Redução de boilerplate
- **spring-boot-starter-test** - Testes
- **spring-security-test** - Testes de segurança

### Ferramentas

- **Maven** - Build e gerenciamento de dependências
- **MySQL Workbench** - Gerenciamento do banco de dados (opcional)
- **Postman** - Testes de API (opcional)

---

## ⚙️ Funcionalidades

### 🔐 Módulo de Autenticação e Autorização

- **Registro de usuários** com validação de dados
- **Login** com email e senha
- **Geração de token JWT** com expiração configurável (1 hora)
- **Validação de token** JWT
- **Criptografia de senhas** com BCrypt
- **Controle de acesso** por perfis (ADMIN e USER)
- **Filtro JWT** para autenticação automática

### 📦 Módulo de Gerenciamento de Produtos

- **Listagem de produtos** (acesso público)
- **Busca de produto por ID** (acesso público)
- **Criação de produtos** (apenas ADMIN)
- **Atualização de produtos** (apenas ADMIN)
- **Exclusão de produtos** (apenas ADMIN)
- **Validações**:
    - Nome único
    - Preço não negativo
    - Estoque não negativo
    - Campos obrigatórios

### 🛒 Módulo de Gerenciamento de Pedidos

- **Criação de pedidos** com múltiplos produtos
- **Listagem de pedidos** do usuário autenticado
- **Busca de pedido por ID**
- **Realização de pagamento** de pedidos
- **Controle de status**:
    - `PENDENTE` - Pedido criado, aguardando pagamento
    - `PAGO` - Pedido pago, estoque atualizado
    - `CANCELADO` - Pedido cancelado
- **Regras de negócio**:
    - Cálculo automático de totais
    - Atualização de estoque apenas após pagamento
    - Cancelamento automático por falta de estoque
    - Validação de propriedade (usuário só vê seus pedidos)

### 📊 Módulo de Relatórios Administrativos

- **Top 5 usuários que mais compraram** (apenas ADMIN)
    - Ordenado por quantidade de pedidos
    - Filtro por status PAGO
    - Retorna nome, email, total de pedidos e valor total
- **Ticket médio por usuário** (apenas ADMIN)
    - Média de valor por pedido
    - Filtro por status PAGO
    - Ordenado por ticket médio decrescente
- **Faturamento mensal** (apenas ADMIN)
    - Valor total faturado no mês
    - Parâmetros opcionais: ano e mês (padrão: mês atual)
    - Filtro por status PAGO

---

## 🏗️ Arquitetura

O projeto segue o modelo **Clean Layers (Arquitetura em Camadas)**, organizando o código em camadas bem definidas com
responsabilidades específicas:

```
┌─────────────────────────────────────┐
│         Controller Layer            │
│  (AuthController, ProductController,│
│   OrderController, ReportController)│
│  - Recebe requisições HTTP          │
│  - Valida entrada                   │
│  - Chama services                   │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│          Service Layer              │
│  (AuthService, ProductService,      │
│   OrderService, ReportService)      │
│  - Lógica de negócio                │
│  - Orquestração                     │
│  - Validações de negócio            │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│        Repository Layer             │
│  (UserRepository, ProductRepository,│
│   OrderRepository, OrderItemRepo)   │
│  - Acesso ao banco de dados         │
│  - Queries SQL                      │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         Database (MySQL)            │
│  (tb_users, tb_products, tb_orders, │
│   tb_order_items)                   │
└─────────────────────────────────────┘
```

### Componentes por Camada

| Camada         | Responsabilidade                                  | Componentes                           |
|----------------|---------------------------------------------------|---------------------------------------|
| **Controller** | Exposição de endpoints REST, validação de entrada | 4 Controllers                         |
| **Service**    | Regras de negócio, orquestração, validações       | 5 Services                            |
| **Repository** | Acesso ao banco de dados, queries                 | 4 Repositories                        |
| **DTO**        | Transporte de dados, padronização de respostas    | 15 DTOs                               |
| **Entity**     | Mapeamento JPA das tabelas                        | 6 Entities                            |
| **Security**   | Configuração JWT, controle de perfis              | JwtService, JwtFilter, SecurityConfig |
| **Exception**  | Tratamento global de exceções                     | GlobalExceptionHandler, 17 Exceptions |
| **Mapper**     | Conversão Entity ↔ DTO                            | 3 Mappers                             |

### Padrões de Design Utilizados

- **DTO (Data Transfer Object)** - Transferência de dados entre camadas
- **Repository Pattern** - Abstração de acesso a dados
- **Service Layer Pattern** - Separação de lógica de negócio
- **Exception Handler Pattern** - Tratamento centralizado de exceções
- **Mapper Pattern** - Conversão entre entidades e DTOs

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

### Obrigatórios

- **Java 17 ou superior**
    - Verificar instalação: `java -version`
    - Download: [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
      ou [OpenJDK](https://adoptium.net/)

- **Maven 3.6 ou superior**
    - Verificar instalação: `mvn -version`
    - Download: [Apache Maven](https://maven.apache.org/download.cgi)

- **MySQL 8.0 ou superior**
    - Verificar instalação: `mysql --version`
    - Download: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

- **Git**
    - Verificar instalação: `git --version`
    - Download: [Git](https://git-scm.com/downloads)

### Opcionais (Recomendados)

- **IDE** (IntelliJ IDEA, Eclipse, VS Code)
- **MySQL Workbench** - Gerenciamento visual do banco de dados
- **Postman** - Testes de API

---

## 🚀 Instalação

Siga os passos abaixo para instalar e executar o projeto:

### 1. Clonar o Repositório

```bash
git clone https://github.com/ManoelRabelo/byte-store-backend.git
cd byte-store-backend
```

### 2. Criar o Banco de Dados (Opcional)

O `schema.sql` já cria o banco de dados automaticamente quando a aplicação é executada. No entanto, se você encontrar
problemas de permissão, crie o banco manualmente antes:

```sql
CREATE DATABASE bytestore_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

**Nota**: Se o usuário MySQL tiver permissões adequadas, o Spring Boot criará e populará o banco automaticamente na
primeira execução através do `schema.sql`. Caso contrário, crie o banco manualmente primeiro.

### 3. Configurar Variáveis de Ambiente (Opcional)

Crie um arquivo `.env` na raiz do projeto (opcional, se preferir usar variáveis de ambiente):

```bash
DB_URL=jdbc:mysql://localhost:3306/bytestore_db?useTimezone=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=admin123
JWT_SECRET=hHghwETDATHC8QMpA4MQPu8LUrcUsXNyDeKH/qSwVpU9cFKmNO/lWsRO7iuyv/Hg
JWT_EXPIRATION=3600000
```

**Nota**: As configurações também podem ser feitas diretamente no arquivo `application.properties`.

### 4. Configurar application.properties

Edite o arquivo `bytestore/src/main/resources/application.properties` e ajuste as credenciais do banco de dados:

```properties
spring.application.name=ByteStore
# Server Configuration
server.port=8080
# Datasource Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bytestore_db?useTimezone=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=admin123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# Hibernate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
# Configuração para carregar dados iniciais (schema.sql)
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
# JWT Configuration
jwt.secret=hHghwETDATHC8QMpA4MQPu8LUrcUsXNyDeKH/qSwVpU9cFKmNO/lWsRO7iuyv/Hg
jwt.expiration=3600000
```

**Importante**:

- Altere `spring.datasource.username` e `spring.datasource.password` conforme suas credenciais do MySQL
- As propriedades `spring.jpa.defer-datasource-initialization=true` e `spring.sql.init.mode=always` já estão
  configuradas para executar o `schema.sql` automaticamente na inicialização da aplicação

### 5. Instalar Dependências

```bash
cd bytestore
mvn clean install
   ```

### 6. Executar a Aplicação

```bash
mvn spring-boot:run
```

Ou execute a classe `ByteStoreApplication.java` diretamente na sua IDE.

### 7. Verificar se Está Funcionando

A aplicação estará disponível em:

```
http://localhost:8080
```

Teste o endpoint de listagem de produtos (público):

```bash
curl http://localhost:8080/products
```

Se retornar uma lista (mesmo que vazia), a aplicação está funcionando!

---

## 🗄️ Configuração do Banco de Dados

### Configuração Automática (Recomendada)

O projeto está configurado para **criar automaticamente** o banco de dados e carregar os dados iniciais quando a
aplicação é iniciada.

#### Como Funciona

O arquivo `application.properties` já possui as seguintes configurações:

```properties
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
```

Essas propriedades fazem com que o Spring Boot execute automaticamente o arquivo `schema.sql` localizado em
`src/main/resources/schema.sql` toda vez que a aplicação inicia.

#### O que o schema.sql faz

O arquivo `schema.sql` contém:

1. **DROP DATABASE IF EXISTS** - Deleta o banco de dados se ele já existir
2. **CREATE DATABASE** - Cria o banco de dados `bytestore_db`
3. **CREATE TABLE** - Cria todas as tabelas (tb_users, tb_products, tb_orders, tb_order_items)
4. **INSERT** - Insere dados iniciais (4 usuários, 26 produtos, 6 pedidos)

**⚠️ Importante**: Toda vez que a aplicação é reiniciada, o banco de dados é **deletado e recriado** com os dados
iniciais.

#### Passo a Passo

1. **Configure as credenciais** no `application.properties` (veja seção [Instalação](#-instalação))

2. **Execute a aplicação** - O Spring Boot irá automaticamente:
    - Deletar o banco de dados `bytestore_db` (se existir)
    - Recriar o banco de dados `bytestore_db`
    - Criar todas as tabelas (tb_users, tb_products, tb_orders, tb_order_items)
    - Inserir dados iniciais (4 usuários, 26 produtos, 6 pedidos)

**Nota**: Se o usuário MySQL não tiver permissões para criar/deletar bancos de dados, crie o banco manualmente
primeiro (veja passo opcional na seção [Instalação](#-instalação)).

#### Verificar se Funcionou

Após iniciar a aplicação, você pode verificar se tudo foi criado corretamente:

```sql
USE bytestore_db;
SHOW TABLES;
SELECT COUNT(*) FROM tb_users;    -- Deve retornar 4
SELECT COUNT(*) FROM tb_products; -- Deve retornar 26
SELECT COUNT(*) FROM tb_orders;   -- Deve retornar 6
```

### Configuração Manual com Dump SQL (Alternativa)

Como alternativa à execução automática do `schema.sql`, você pode importar o arquivo `bytestore_db_dump.sql` diretamente
no MySQL. Este dump contém a estrutura completa do banco de dados e todos os dados iniciais.

#### Importar Dump via MySQL Workbench

1. **Desabilite a execução automática** no `application.properties`:

```properties
spring.sql.init.mode=never
```

2. **Abra o MySQL Workbench** e conecte-se ao servidor

3. **Importe o dump**:
    - Clique em `Server` → `Data Import`
    - Selecione `Import from Self-Contained File`
    - Escolha o arquivo `bytestore_db_dump.sql`
    - Selecione `bytestore_db` como banco de destino (ou crie o banco se não existir)
    - Clique em `Start Import`

### Estrutura do Banco de Dados

O banco de dados possui 4 tabelas principais:

- **tb_users** - Usuários do sistema (id, name, email, password, role, created_at)
- **tb_products** - Produtos cadastrados (id, name, description, price, category, stock_quantity, created_at,
  updated_at)
- **tb_orders** - Pedidos realizados (id, user_id, status, total_amount, created_at, paid_at)
- **tb_order_items** - Itens de cada pedido (id, order_id, product_id, quantity, unit_price, subtotal)

### Dados Iniciais

O `schema.sql` inclui automaticamente:

- **4 usuários** (2 ADMIN, 2 USER) com senhas criptografadas
- **26 produtos** em 5 categorias (Computadores, Periféricos, Componentes, Monitores, Áudio)
- **6 pedidos** de exemplo com diferentes status

Veja a seção [Dados Iniciais](#-dados-iniciais) para credenciais de acesso.

---

## 🚀 Usando o Postman

O projeto inclui uma **collection do Postman** completa com todos os endpoints da API, além de um **ambiente configurado
** com automação para gerenciar tokens de autenticação.

### Arquivos Incluídos

- **`ByteStore Paths.postman_collection.json`** - Collection com todos os endpoints da API
- **`ByteStore Local.postman_environment.json`** - Ambiente local com variáveis configuradas

### Importando no Postman

#### 1. Importar a Collection

1. Abra o **Postman**
2. Clique em **Import** (canto superior esquerdo)
3. Arraste o arquivo `ByteStore Paths.postman_collection.json` ou clique em **Upload Files**
4. Clique em **Import**

#### 2. Importar o Ambiente

1. No Postman, clique em **Import** novamente
2. Arraste o arquivo `ByteStore Local.postman_environment.json` ou clique em **Upload Files**
3. Clique em **Import**

#### 3. Selecionar o Ambiente

1. No canto superior direito do Postman, clique no dropdown de ambientes
2. Selecione **ByteStore Local**

### Configurando o Ambiente

Após importar o ambiente, configure a variável de ambiente:

1. Clique no ícone de **engrenagem** (⚙️) ao lado do dropdown de ambientes
2. Selecione **ByteStore Local**
3. Defina o valor da variável `baseUrl`:
   ```
   http://localhost:8080
   ```
   
### Automação de Token

A collection possui **automação configurada** que salva automaticamente o token JWT após registro ou login:

#### Como Funciona

1. **Execute a requisição de Register ou Login**:
    - Vá para a pasta `User Athentication`
    - Execute `Register` ou `Login`

2. **O token é salvo automaticamente**:
    - A collection possui scripts de teste que capturam o token da resposta
    - O token é automaticamente salvo na variável `accessToken` do ambiente
    - Todas as outras requisições usam automaticamente esse token

#### Script de Automação

As requisições `Register` e `Login` possuem o seguinte script de teste:

```javascript
if (pm.response.code >= 200 && pm.response.code < 300) {
    const jsonData = pm.response.json();
    pm.environment.set("accessToken", jsonData.accessToken);
} else {
    console.log("Falha na requisição, código:", pm.response.code);
}
```

**Nota**: O script verifica se a resposta foi bem-sucedida (código 200-299) e salva o token na variável de ambiente.

### Usando a Collection

#### 1. Autenticação

1. Abra a pasta **User Athentication**
2. Execute a requisição **Login** (ou **Register** para criar um novo usuário)
3. O token será automaticamente salvo no ambiente

#### 2. Usar Endpoints Protegidos

Após fazer login, todas as requisições que requerem autenticação usarão automaticamente o token salvo:

- **Product** - Endpoints de produtos (criar, atualizar, deletar)
- **Order** - Endpoints de pedidos (criar, listar, pagar)
- **Report** - Endpoints de relatórios (apenas ADMIN)

#### 3. Verificar Token Salvo

Para verificar se o token foi salvo:

1. Clique no ícone de **engrenagem** (⚙️) ao lado do dropdown de ambientes
2. Selecione **ByteStore Local**
3. Verifique se a variável `accessToken` possui um valor (será um token JWT longo)

### Estrutura da Collection

A collection está organizada em pastas:

- **User Athentication**
    - Register - Registrar novo usuário
    - Login - Fazer login e obter token

- **Product**
    - Product Create - Criar produto (ADMIN)
    - Product Update - Atualizar produto (ADMIN)
    - Product Delete - Deletar produto (ADMIN)
    - Product Get All - Listar todos os produtos (público)
    - Product Get By Id - Buscar produto por ID (público)

- **Order**
    - Order Create - Criar pedido (USER/ADMIN)
    - Order Get All - Listar pedidos do usuário (USER/ADMIN)
    - Order Get By Id - Buscar pedido por ID (USER/ADMIN)
    - Order Payment - Realizar pagamento (USER/ADMIN)

- **Report**
    - Top Users - Top 5 usuários que mais compraram (ADMIN)
    - Average Ticket - Ticket médio por usuário (ADMIN)
    - Monthly Revenue - Faturamento mensal (ADMIN)

### Dicas de Uso

1. **Trocar entre usuários**: Execute Login com diferentes credenciais para trocar o token
2. **Testar permissões**: Use um usuário USER para testar endpoints que não requerem ADMIN
3. **Visualizar respostas**: Todas as requisições mostram a resposta completa no Postman
4. **Debugging**: Use o console do Postman (View → Show Postman Console) para ver logs

### Variáveis do Ambiente

O ambiente `ByteStore Local` possui as seguintes variáveis:

| Variável      | Descrição                 | Valor Padrão                                     |
|---------------|---------------------------|--------------------------------------------------|
| `baseUrl`     | URL base da API           | `http://localhost:8080` (deve ser configurado)   |
| `accessToken` | Token JWT de autenticação | (preenchido automaticamente após login/register) |

---

## 📚 Documentação da API

### Base URL

```
http://localhost:8080
```

### Autenticação

A maioria dos endpoints requer autenticação via JWT. Consulte a seção [Autenticação](#-autenticação) para obter o token.

### Endpoints por Módulo

#### 🔐 Autenticação (`/auth`)

| Método | Endpoint                       | Descrição               | Autenticação |
|--------|--------------------------------|-------------------------|--------------|
| `POST` | `/auth/register`               | Registrar novo usuário  | ❌ Público    |
| `POST` | `/auth/login`                  | Login e obter token JWT | ❌ Público    |
| `GET`  | `/auth/validate?token={token}` | Validar token JWT       | ❌ Público    |

#### 📦 Produtos (`/products`)

| Método   | Endpoint         | Descrição                | Autenticação | Perfil |
|----------|------------------|--------------------------|--------------|--------|
| `GET`    | `/products`      | Listar todos os produtos | ❌ Público    | -      |
| `GET`    | `/products/{id}` | Buscar produto por ID    | ❌ Público    | -      |
| `POST`   | `/products`      | Criar produto            | ✅ Requerida  | ADMIN  |
| `PUT`    | `/products/{id}` | Atualizar produto        | ✅ Requerida  | ADMIN  |
| `DELETE` | `/products/{id}` | Deletar produto          | ✅ Requerida  | ADMIN  |

#### 🛒 Pedidos (`/orders`)

| Método | Endpoint               | Descrição                 | Autenticação | Perfil     |
|--------|------------------------|---------------------------|--------------|------------|
| `POST` | `/orders`              | Criar pedido              | ✅ Requerida  | USER/ADMIN |
| `GET`  | `/orders`              | Listar pedidos do usuário | ✅ Requerida  | USER/ADMIN |
| `GET`  | `/orders/{id}`         | Buscar pedido por ID      | ✅ Requerida  | USER/ADMIN |
| `POST` | `/orders/{id}/payment` | Realizar pagamento        | ✅ Requerida  | USER/ADMIN |

#### 📊 Relatórios (`/reports`)

| Método | Endpoint                                     | Descrição                         | Autenticação | Perfil |
|--------|----------------------------------------------|-----------------------------------|--------------|--------|
| `GET`  | `/reports/top-users`                         | Top 5 usuários que mais compraram | ✅ Requerida  | ADMIN  |
| `GET`  | `/reports/average-ticket`                    | Ticket médio por usuário          | ✅ Requerida  | ADMIN  |
| `GET`  | `/reports/monthly-revenue?year=2025&month=1` | Faturamento mensal                | ✅ Requerida  | ADMIN  |

### Exemplos de Requisições

#### Registrar Usuário

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "Senha123",
    "role": "USER"
  }'
```

#### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Senha123"
  }'
```

#### Listar Produtos (Público)

```bash
curl -X GET http://localhost:8080/products
```

#### Criar Produto (ADMIN)

```bash
curl -X POST http://localhost:8080/products \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mouse Logitech MX Master 3",
    "description": "Mouse sem fio Logitech MX Master 3",
    "price": 549.99,
    "category": "Periféricos",
    "stockQuantity": 50
  }'
```

#### Criar Pedido (USER/ADMIN)

```bash
curl -X POST http://localhost:8080/orders \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": "123e4567-e89b-12d3-a456-426614174000",
        "quantity": 2
      }
    ]
  }'
```

#### Realizar Pagamento

```bash
curl -X POST http://localhost:8080/orders/{id}/payment \
  -H "Authorization: Bearer {token}"
```

#### Top 5 Usuários (ADMIN)

```bash
curl -X GET http://localhost:8080/reports/top-users \
  -H "Authorization: Bearer {token_admin}"
```

### Códigos de Status HTTP

| Código | Descrição                                        |
|--------|--------------------------------------------------|
| `200`  | OK - Requisição bem-sucedida                     |
| `201`  | Created - Recurso criado com sucesso             |
| `204`  | No Content - Recurso deletado com sucesso        |
| `400`  | Bad Request - Erro de validação                  |
| `401`  | Unauthorized - Token inválido ou ausente         |
| `403`  | Forbidden - Acesso negado (sem permissão)        |
| `404`  | Not Found - Recurso não encontrado               |
| `409`  | Conflict - Conflito (ex: nome duplicado)         |
| `500`  | Internal Server Error - Erro interno do servidor |

### Tratamento de Erros

Todas as respostas de erro seguem o formato padronizado:

```json
{
  "timestamp": "2025-01-27T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensagem de erro descritiva",
  "path": "/endpoint",
  "fieldErrors": [
    {
      "field": "campo",
      "message": "Mensagem de erro do campo"
    }
  ]
}
```

---

## 🔐 Autenticação

O sistema utiliza **JWT (JSON Web Tokens)** para autenticação. Todas as requisições que requerem autenticação devem
incluir o token no header `Authorization`.

### Fluxo de Autenticação

1. **Registrar usuário** ou **fazer login**
2. **Receber token JWT** na resposta
3. **Incluir token** no header `Authorization` das requisições
4. **Token válido por 1 hora** (configurável)

### Obter Token

#### Opção 1: Registrar Novo Usuário

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "Senha123",
    "role": "USER"
  }'
```

**Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### Opção 2: Fazer Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Senha123"
  }'
```

**Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

### Usar Token nas Requisições

Inclua o token no header `Authorization` com o prefixo `Bearer`:

```bash
curl -X GET http://localhost:8080/orders \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Validar Token

```bash
curl -X GET "http://localhost:8080/auth/validate?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta:**

```
Token válido
```

ou

```
Token inválido ou expirado
```

### Configuração do JWT

O token JWT está configurado no `application.properties`:

```properties
jwt.secret=hHghwETDATHC8QMpA4MQPu8LUrcUsXNyDeKH/qSwVpU9cFKmNO/lWsRO7iuyv/Hg
jwt.expiration=3600000  # 1 hora em milissegundos
```

**Importante**: Em produção, use uma chave secreta forte e armazene-a em variáveis de ambiente.

---

## 👥 Perfis de Usuário

O sistema possui dois perfis de usuário com diferentes níveis de acesso:

### 🔴 ADMIN (Administrador)

**Permissões completas:**

- ✅ Gerenciar produtos (criar, atualizar, deletar, listar)
- ✅ Visualizar relatórios administrativos
- ✅ Criar e gerenciar pedidos
- ✅ Visualizar todos os pedidos

**Endpoints exclusivos:**

- `POST /products` - Criar produto
- `PUT /products/{id}` - Atualizar produto
- `DELETE /products/{id}` - Deletar produto
- `GET /reports/top-users` - Top 5 usuários
- `GET /reports/average-ticket` - Ticket médio
- `GET /reports/monthly-revenue` - Faturamento mensal

### 🟢 USER (Usuário)

**Permissões limitadas:**

- ✅ Visualizar produtos (listagem pública)
- ✅ Criar pedidos
- ✅ Visualizar próprios pedidos
- ✅ Realizar pagamento de pedidos
- ❌ Gerenciar produtos
- ❌ Visualizar relatórios
- ❌ Visualizar pedidos de outros usuários

**Endpoints permitidos:**

- `GET /products` - Listar produtos
- `GET /products/{id}` - Buscar produto
- `POST /orders` - Criar pedido
- `GET /orders` - Listar próprios pedidos
- `GET /orders/{id}` - Buscar próprio pedido
- `POST /orders/{id}/payment` - Realizar pagamento

*Usuários USER só podem acessar seus próprios pedidos.

---

## 🗃️ Dados Iniciais

O arquivo `schema.sql` inclui dados iniciais para testes. Utilize as credenciais abaixo para acessar o sistema:

### 👤 Usuários Pré-cadastrados

| Nome                  | Email                   | Senha           | Perfil | Descrição                 |
|-----------------------|-------------------------|-----------------|--------|---------------------------|
| **Roronoa Zoro**      | `zoro@bytestore.com`    | `Santoryu123`   | ADMIN  | Administrador do sistema  |
| **Monkey D Luffy**    | `luffy@bytestore.com`   | `GomuGomu123`   | USER   | Usuário comum             |
| **Tony Tony Chopper** | `chopper@bytestore.com` | `RumbleBall123` | USER   | Usuário comum             |
| **Nico Robin**        | `robin@bytestore.com`   | `HanaHana123`   | ADMIN  | Administradora do sistema |

### 📦 Produtos Pré-cadastrados

O sistema inclui **26 produtos** organizados em **5 categorias**:

- **Computadores** (4 produtos): Notebooks e desktops
- **Periféricos** (6 produtos): Mouses, teclados, webcams
- **Componentes** (8 produtos): Placas de vídeo, processadores, memórias, SSDs
- **Monitores** (4 produtos): Monitores diversos
- **Áudio** (4 produtos): Headsets e fones de ouvido

### 🛒 Pedidos de Exemplo

O sistema inclui **6 pedidos** de exemplo com diferentes status.

### 🧪 Como Testar com Dados Iniciais

1. **Execute a aplicação** - O Spring Boot criará automaticamente o banco de dados e carregará os dados iniciais do
   `schema.sql` na primeira execução (veja seção [Configuração do Banco de Dados](#-configuração-do-banco-de-dados))

2. **Faça login com um usuário ADMIN:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "zoro@bytestore.com",
    "password": "Santoryu123"
  }'
```

3. **Use o token retornado** para acessar endpoints administrativos:

```bash
curl -X GET http://localhost:8080/reports/top-users \
  -H "Authorization: Bearer {token}"
```

4. **Faça login com um usuário USER:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "luffy@bytestore.com",
    "password": "GomuGomu123"
  }'
```

5. **Use o token** para criar pedidos e visualizar produtos:

```bash
curl -X GET http://localhost:8080/products \
  -H "Authorization: Bearer {token}"
```

### 📝 Notas Importantes

- As senhas estão **criptografadas com BCrypt** no banco de dados
- Os valores de senha acima são as **senhas originais** antes da criptografia
- Use essas credenciais apenas para **testes locais**

---

## ✍️ Autor

**Manoel Rabelo**

💼 Desenvolvedor Back-End

📧 Projeto desenvolvido como desafio técnico demonstrando habilidades em:

- Java e Spring Boot
- Arquitetura em camadas
- APIs RESTful
- Autenticação JWT
- Banco de dados MySQL
- Consultas SQL otimizadas

---
