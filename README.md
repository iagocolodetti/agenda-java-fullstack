# agenda-java-fullstack

Um exemplo simples de aplicação fullstack utilizando a linguagem Java.

* Projeto dividido em três partes:
	* [AgendaREST (backend)](#AgendaREST-backend)
	* [AgendaWEB (frontend)](#AgendaWEB-frontend)
	* [AgendaMobile (frontend)](#AgendaMobile-frontend)

<br>

## [AgendaREST (backend)](/AgendaREST)

- Desenvolvido usando a IDE: Apache NetBeans IDE 17
- Java 17 (JDK 17.0.6)
- Spring Boot 3.0.4

Spring Boot RESTful API desenvolvida utilizando: JPA com Hibernate, Lombok, Spring Security com JWT, documentação com Swagger (OpenAPI 3.0.3) e banco de dados MySQL.

As configurações de conexão com o banco de dados estão no arquivo: [AgendaREST/src/main/resources/application.properties](/AgendaREST/src/main/resources/application.properties)

*OBS: O Hibernate criará automaticamente tanto o banco de dados quanto suas tabelas, porém, para visualização ou até mesmo a criação manual desse banco de dados, utilizar o script: **[agenda.sql](agenda.sql)**.*

<br>

## [AgendaWEB (frontend)](/AgendaWEB)

- Desenvolvido usando a IDE: Apache NetBeans IDE 17
- Java 17 (JDK 17.0.6)

Cliente WEB para consumir a API, desenvolvido da forma mais simples possível com o objetivo de maximizar o uso de Java. Desenvolvido utilizando: JSP, Servlet e AngularJS (apenas onde foi necessário).

<br>

## [AgendaMobile (frontend)](/AgendaMobile)

- Desenvolvido usando a IDE: Android Studio Electric Eel | 2022.1.1 Patch 2
- Java 11 (JDK 11.0.15)
- Gradle 7.4.2

Cliente Mobile para consumir a API.