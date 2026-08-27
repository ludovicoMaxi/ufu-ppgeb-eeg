# Instruções para agentes

## Contexto do projeto

- Aplicação Spring Boot para cadastro de exames de EEG da UFU.
- Backend em Java 25, gerenciado pelo Maven.
- Frontend em React/Webpack dentro de `src/main/webapp`.

## Testes

- Leia [`docs/TESTING.md`](docs/TESTING.md) antes de criar ou alterar testes.
- Execute `./mvnw test` após mudanças no código Java ou nos testes. `mvn test` também é aceito quando o Maven estiver instalado.
- Testes unitários de serviços devem permanecer isolados, usando Mockito e Instancio.
- Testes de repositório devem usar `@DataJpaTest` (pacote do Spring Boot 4: `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`), perfil `test`, H2 em memória e `@AutoConfigureTestDatabase(replace = Replace.NONE)`. Use `@Import(AuditingConfig.class)` e popule o `SecurityContextHolder` com um usuário autenticado para habilitar o JPA auditing; não preencha `createdBy`/`createdAt` manualmente no `save`.
- Testes de integração devem usar o perfil `test`, H2 em memória, MockMvc e as fixtures de `src/test/resources/import.sql` quando necessário.
- Preserve as verificações de segurança, status HTTP, corpo JSON, auditoria e interações com repositórios já cobertas pelos testes.
- Testes de API devem usar nomes no formato `given..._when..._then...` e declarar `@DisplayName` descrevendo o comportamento.
- Para payloads JSON nos testes de integração, crie os objetos Java e serialize-os com o `ObjectMapper`; não monte JSON manualmente em text blocks.
- Não use credenciais do perfil de teste em produção nem adicione segredos às fixtures.

## Convenções gerais

- Para verificações de nulidade, prefira os imports estáticos `isNull(...)` e `nonNull(...)` de `java.util.Objects` em vez dos operadores `== null` e `!= null`. Exemplo:

  ```java
  if (nonNull(patientId)) {
    exam.getPatient().setId(patientId);
  }
  ```

- Para validações de coleções e listas, prefira os imports estáticos `isNotEmpty(...)` e `isEmpty(...)` de `org.apache.commons.collections4.CollectionUtils` em vez de checar `size() == 0` ou `size() > 0` manualmente. Exemplo:

  ```java
  if (isNotEmpty(exam.getExamMedicaments())) {
    ...
  }
  ```

- Vale tanto para o código de produção quanto para os testes.

## Convenções rápidas

- Mantenha `src/test/java` espelhando os pacotes de `src/main/java`.
- Nomeie os testes no formato `given..._when..._then...` e use `@DisplayName` para explicar o comportamento.
- Organize cada teste em preparação, execução e verificação.
- Se a preparação de um teste tiver mais de cinco linhas, extraia-a para um método `setupNomeDoTeste` ou para factories/helpers de criação do cenário e dos mocks.
- Quando o `save` mockado deve retornar um objeto fixo criado no teste, use `when(repository.save(any(...))).thenReturn(objeto)`.
- Coloque os métodos `setup...`/`create...` (factories/helpers de cenário) logo abaixo do primeiro teste que os utiliza, em vez de agrupá-los todos no topo da classe; mantenha apenas os helpers compartilhados por vários grupos junto à sua principal área de uso.
- Quando a quantidade de chamadas fizer parte da verificação, use `verify(mock, times(quantidade))`; use `never()` para chamadas que não devem ocorrer.
- Em testes de criação, não valide ID gerado automaticamente quando esse não for o comportamento em análise; valide o resultado de negócio e as interações relevantes.
- Verifique tanto o resultado observável quanto as interações importantes com dependências, incluindo chamadas que não devem ocorrer em entradas inválidas.
- Prefira corrigir ou ampliar os testes existentes a removê-los ou enfraquecê-los.

Para o modelo completo, exemplos e critérios de cobertura, consulte [`docs/TESTING.md`](docs/TESTING.md).
