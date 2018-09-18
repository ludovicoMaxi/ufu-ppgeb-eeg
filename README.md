# ufu-ppgeb-eeg
> Cadastro de EEG coletados no Programa de Pós-graduação da UFU


## Instalação:
Esta aplicação utiliza springboot e para executar bastar executar o comando:
    mvn spring-boot:run

Obs: É necessário ter o maven instalado e java8.

O banco de dados utilizado é na memoria java (MySql), para evitar complicações de instalação do banco de dados.


## Exemplo de uso
A pagina inicial: http://localhost:8090
Será solicitado uma autenticação simples de usuário e nome.
A lista de usuários cadastra é:
joaol
teste
user


## Configuração para Desenvolvimento
O gerenciador de dependencia é o maven, para baixar todas as dependencias basta executar:
 mvn clean install

Para rodas os testes unitários execute:
 mvn test


## Histórico de lançamentos

* 0.0.1-SNAPSHOT
    * Versão inicial


## Meta
 João Ludovico Maximiano Barbosa - joaolmbarbosa@gmail.com
 Rafael Caetan da Silva - engcaetano@outlook.com

