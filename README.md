# 💻 ByteStore

**ByteStore** é um sistema de **e-commerce simplificado** desenvolvido em **Java com Spring Boot**, voltado para a venda de produtos de **tecnologia e informática**.  
O objetivo do projeto é demonstrar habilidades práticas em desenvolvimento back-end, arquitetura em camadas e boas práticas com o ecossistema Spring.

---

## 🎯 Objetivo

Construir uma aplicação RESTful que permita:

- Autenticação segura com **JWT**.
- **CRUD completo** de produtos (acesso exclusivo do perfil ADMIN).
- **Gerenciamento de pedidos**, com controle de status, cálculo automático do valor total e atualização de estoque.
- Execução de **consultas SQL otimizadas** para relatórios administrativos.

---

## 👥 Perfis de Usuário

| Perfil | Permissões |
|--------|-------------|
| **ADMIN** | Gerenciar produtos (criar, atualizar, deletar, listar). |
| **USER** | Criar pedidos, visualizar produtos e acompanhar seus status. |

---

## ⚙️ Principais Funcionalidades

- **Autenticação JWT** (login e registro de usuários).
- **Gerenciamento de produtos** com categorias, preços e estoque.
- **Criação de pedidos** com múltiplos produtos.
- Atualização automática de estoque após o pagamento.
- Cancelamento automático de pedidos caso o estoque seja insuficiente.
- **Relatórios SQL otimizados**:
  - Top 5 usuários que mais compraram.
  - Ticket médio de pedidos por usuário.
  - Valor total faturado no mês.

---

## 🧩 Stack Tecnológica

- **Java 17+**
- **Spring Boot 3+**
  - Spring Web
  - Spring Data JPA
  - Spring Security (JWT)
- **MySQL**
- **Maven**
- **Docker (opcional)**

---

## 🏗️ Arquitetura

O projeto segue o modelo **Clean Layers (Modelo A)**, organizado em camadas:

```

Controller → Service → Repository

````

| Camada | Responsabilidade |
|--------|------------------|
| **Controller** | Exposição de endpoints REST |
| **Service** | Regras de negócio e orquestração |
| **Repository** | Acesso ao banco via Spring Data JPA |
| **DTO** | Transporte de dados e padronização de respostas |
| **Entity** | Mapeamento JPA das tabelas |
| **Security** | Configuração JWT e controle de perfis |
| **Exception** | Tratamento global de exceções |

---

## 🚀 Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/byte-store.git
    ````

2. Acesse o diretório:

   ```bash
   cd byte-store
   ```
3. Configure o banco de dados no arquivo `application.yml` ou `.properties`.
4. Execute o projeto:

   ```bash
   mvn spring-boot:run
   ```
5. Acesse a aplicação em:

   ```
   http://localhost:8080
   ```

---

## 📦 Entrega e Documentação

* O repositório incluirá:

    * Código-fonte completo.
    * Dump do banco de dados MySQL.
    * Instruções detalhadas no README.
* O foco é em **clareza, boas práticas e funcionalidade**.

---

## ✍️ Autor

Desenvolvido por Manoel Rabelo — Desafio Técnico **Desenvolvedor Back-End Pleno**
💼 Projeto de portfólio demonstrando habilidades em **Java + Spring Boot + MySQL**.

---