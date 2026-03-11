# ForumHub API - Challenge Alura

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=openjdk" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.0-6db33f?style=for-the-badge&logo=spring" alt="Spring Boot 3.3.0">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql" alt="MySQL">
</p>

## Sobre o Projeto
O **ForumHub** é uma API REST desenvolvida como desafio final do programa de backend da Alura. O foco do projeto é a criação de um sistema de fórum funcional, aplicando conhecimentos de persistência de dados e segurança.

# Desenvolvedora
Kethelen De Azevedo

Estudante de Ciência da Computação.

## Desenvolvido durante a jornada na Alura. Desenvolvimento Back-end

## 🛠️ Funcionalidades
* **Autenticação Stateless:** Login seguro via **Spring Security** com geração de **Tokens JWT**.
* **CRUD Completo:** Gerenciamento de tópicos (criar, listar, atualizar e deletar).
* **Banco de Dados Relacional:** Uso de **MySQL** para armazenamento e **Flyway** para migrações.
* **Segurança:** Senhas criptografadas e rotas protegidas.

## Tecnologias Utilizadas
* **Java 17**
* **Spring Boot 3.3.0**
* **Spring Data JPA & Hibernate**
* **Flyway** (Migrations)
* **MySQL**
* **Maven**

## Configuração do Ambiente
Para rodar o projeto localmente, você precisará configurar as seguintes variáveis no seu `application.properties` ou arquivo `.env`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forumhub_api
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
api.security.token.secret=SEU_TOKEN_AQUI
