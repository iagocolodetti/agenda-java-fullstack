# agenda-java-fullstack

Um exemplo simples de aplicação fullstack utilizando a linguagem Java.

* Projeto dividido em três partes:
	* [AgendaREST (backend)](#AgendaREST-backend)
	* [AgendaWEB (frontend)](#AgendaWEB-frontend)
	* [AgendaMobile (frontend)](#AgendaMobile-frontend)

<br>

## [AgendaREST (backend)](/AgendaREST)

- Desenvolvido no NetBeans 8.2 com o plugin [NB SpringBoot](http://plugins.netbeans.org/plugin/67888/nb-springboot)

Spring Boot RESTful API desenvolvida utilizando: JPA com Hibernate, Lombok, Spring Security com JWT, documentação com Swagger (OpenAPI 3.0.2) e banco de dados MySQL.

As configurações de conexão com o banco de dados estão no arquivo: [AgendaREST/src/main/resources/application.properties](/AgendaREST/src/main/resources/application.properties)

*OBS: Utilizar o banco de dados* ***agenda.sql***

<br>

## [AgendaWEB (frontend)](/AgendaWEB)

- Desenvolvido no NetBeans 8.2

Cliente WEB para consumir a API, desenvolvido da forma mais simples possível com o objetivo de maximizar o uso de Java. Desenvolvido utilizando: JSP, Servlet e AngularJS (apenas onde foi necessário).

<br>

## [AgendaMobile (frontend)](/AgendaMobile)

- Desenvolvido no Android Studio 4.1.1

Cliente Mobile para consumir a API.