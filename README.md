# ufu-ppgeb-eeg
> Cadastro de EEG coletados no Programa de Pós-graduação da UFU


## Instalação:
Esta aplicação utiliza Spring Boot 4.1.0 e Java 25. Para executar:

    mvn spring-boot:run

Obs: É necessário ter o Maven instalado e Java 25.

O banco de dados utilizado é o H2 em memória (perfil `dev`), para evitar complicações de instalação do banco de dados.


## Exemplo de uso
A pagina inicial: http://localhost:8080
Será solicitada uma autenticação básica (HTTP Basic) de usuário e senha.
A lista de usuários cadastrados é (todos com senha `123`):
- joaol
- teste
- user


## Configuração para Desenvolvimento
O gerenciador de dependência é o Maven. Para baixar todas as dependências e compilar o frontend (Node/Webpack):

    mvn clean install

Para rodar os testes unitários execute:

    mvn test


## Empacotamento
O build gera um jar executável com o frontend embutido:

    mvn clean package
    java -jar target/ufu-ppgeb-eeg-0.0.1-SNAPSHOT.jar


## Histórico de lançamentos

* 0.0.1-SNAPSHOT
    * Versão inicial (Spring Boot 1.5.6 / Java 8)
    * Migração para Spring Boot 4.1.0 / Java 25 (Jakarta EE 11, Hibernate 7, Spring Security 7)


## Meta
 João Ludovico Maximiano Barbosa - joaolmbarbosa@gmail.com
 Rafael Caetan da Silva - engcaetano@outlook.com
