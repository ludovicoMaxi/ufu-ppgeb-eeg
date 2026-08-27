chat# Modelo de testes

Este documento descreve o padrão de testes adotado atualmente no projeto `ufu-ppgeb-eeg`. Ele serve como referência para criar novos testes e para interpretar os testes existentes.

## Objetivo

Os testes devem proteger regras de negócio, contratos HTTP e integrações relevantes sem tornar a suíte desnecessariamente lenta ou frágil. A estratégia atual separa os testes em três níveis:

1. Testes unitários dos serviços, rápidos e isolados.
2. Testes de integração da aplicação, executados com o contexto Spring e banco H2.
3. Teste mínimo de carregamento do contexto da aplicação.

## Estrutura dos arquivos

```text
src/
├── main/java/br/com/ufu/ppgeb/eeg/
│   ├── controller/
│   ├── service/
│   └── repository/
└── test/
    ├── java/br/com/ufu/ppgeb/eeg/
    │   ├── ApiIntegrationTest.java
    │   ├── UfuPpgebEegApplicationTests.java
    │   ├── repository/*RepositoryTest.java
    │   └── service/impl/*ServiceImplTest.java
    └── resources/
        ├── application-test.properties
        └── import.sql
```

Os testes ficam no mesmo pacote lógico das classes testadas. Para um novo serviço, crie o teste em `src/test/java` preservando a estrutura de pacotes e use o sufixo `ServiceImplTest`.

## Ferramentas

As dependências de teste são fornecidas principalmente por:

- JUnit Jupiter, por meio de `spring-boot-starter-test`.
- AssertJ para asserções legíveis.
- Mockito para mocks, stubs e verificação de interações.
- Instancio para gerar entidades de teste e substituir apenas os campos relevantes.
- Spring Boot Test e MockMvc para testes de integração.
- Spring Security Test para autenticação nas requisições protegidas.
- `spring-boot-starter-data-jpa-test` para testes de repositório com `@DataJpaTest` (no Spring Boot 4).
- JaCoCo para gerar o relatório de cobertura durante a fase de testes.

## Testes unitários de serviços

### Quando usar

Use teste unitário para validar a lógica de uma classe de serviço sem iniciar o Spring, acessar banco de dados ou chamar outros serviços reais. A classe testada deve ser o foco; repositórios e dependências externas devem ser simulados.

### Estrutura padrão

Os testes atuais usam `MockitoExtension`, `@Mock` e `@InjectMocks`:

```java
@ExtendWith(MockitoExtension.class)
class UnitServiceImplTest {

  @Mock
  private UnitRepository unitRepository;

  @InjectMocks
  private UnitServiceImpl unitService;

  @Test
  @DisplayName("Given two units in database when findAll then return all units")
  void givenTwoUnitsInDatabase_whenFindAll_thenReturnAllUnits() {
    Unit unit1 = Instancio.create(Unit.class);
    Unit unit2 = Instancio.create(Unit.class);
    List<Unit> units = List.of(unit1, unit2);

    when(unitRepository.findAll()).thenReturn(units);

    List<Unit> result = unitService.findAll();

    assertThat(result).hasSize(2);
    verify(unitRepository).findAll();
  }
}
```

Organize o teste em três etapas:

1. Preparação: crie os objetos e configure os stubs.
2. Execução: chame uma única operação principal do serviço.
3. Verificação: confirme o retorno, a exceção e as interações relevantes.

### Dados e mocks

- Use `Instancio.create(Tipo.class)` quando os valores dos demais campos não forem importantes.
- Use `Instancio.of(...).set(field(...), valor).create()` ou setters quando um campo específico fizer parte do cenário.
- Quando o `save` mockado deve retornar um objeto fixo criado no teste, configure o retorno com `thenReturn`:

  ```java
  when(repository.save(any(Entity.class)))
      .thenReturn(entity);
  ```

- Use constantes para IDs, nomes, mensagens e quantidades reutilizados no cenário.
- Não dependa de uma ordem implícita entre testes unitários.
- Se a preparação de um teste tiver mais de cinco linhas, extraia-a para um método `setupNomeDoTeste` ou para uma factory/helper de criação do cenário e dos mocks.
- Coloque os métodos `setup...`/`create...` (factories/helpers de cenário) logo abaixo do primeiro teste que os utiliza, em vez de agrupá-los todos no topo da classe; mantenha apenas os helpers compartilhados por vários grupos junto à sua principal área de uso.

### Cenários esperados

Para operações de serviço, avalie conforme a regra de negócio:

- Caminho válido, incluindo o resultado esperado.
- Lista vazia ou busca sem resultados.
- Argumentos nulos, vazios ou inválidos.
- Recurso inexistente, normalmente com `ResourceNotFoundException`.
- Duplicidade ou violação de regra de negócio.
- Atualização de um recurso existente, preservando campos imutáveis e relações obrigatórias.
- Exclusão de recurso existente e tentativa de excluir recurso inexistente.
- Operações sobre listas relacionadas, como épocas, atividades, medicamentos e equipamentos de um exame.

Para entradas inválidas, verifique também que a operação de persistência não foi executada:

```java
verify(repository, never()).save(any());
```

As asserções devem validar o comportamento, não detalhes internos sem relevância. Quando uma chamada ao repositório for parte do contrato do serviço, use `verify` para garantir o argumento e a quantidade de chamadas esperada.

## Testes de repositório

### Quando usar

Use o teste de repositório para validar o comportamento real da camada de persistência: queries derivadas, métodos do `JpaRepository` e implementações customizadas (como o `ContactRepositoryImpl` com Criteria). O repositório real é executado contra o banco H2 em memória do perfil `test`.

### Configuração

> **Spring Boot 4:** desde o Spring Boot 4, o `@DataJpaTest` foi movido para o módulo
> `spring-boot-starter-data-jpa-test` e para novos pacotes. Adicione a dependência (test)
> ao `pom.xml` e use os novos imports:
>
> ```java
> import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
> import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
> import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
> ```

```java
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ContactRepositoryTest {

  @Autowired
  private ContactRepository contactRepository;
  // ...
}
```

- `@DataJpaTest` limita o carregamento à camada JPA (entidades e repositórios), sem iniciar controllers ou serviços.
- `@AutoConfigureTestDatabase(replace = Replace.NONE)` preserva o datasource H2 do perfil `test`.
- Cada teste é transacional e sofre rollback ao final, então não é necessário limpar os dados entre testes.

### Auditoria

O `@DataJpaTest` não ativa o JPA auditing por padrão. Para validar que os campos de
auditoria (`createdAt`, `createdBy`, `updatedAt`, `updatedBy`) são populados pelo
`AuditingEntityListener` (e não preenchidos manualmente no `save`), importe a
configuração real `AuditingConfig` e popule o `SecurityContextHolder` com um usuário
autenticado antes do teste, para que o `auditorProvider` de produção resolva o auditor:

```java
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(AuditingConfig.class)
class ContactRepositoryTest {

  @BeforeEach
  void setUpAuthentication() {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(
        new UsernamePasswordAuthenticationToken(USERNAME, "123",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    SecurityContextHolder.setContext(context);
  }

  @AfterEach
  void tearDownAuthentication() {
    SecurityContextHolder.clearContext();
  }
  // ...
}
```

Assim, o listener de auditoria usa o `auditorProvider` de produção para preencher os
campos de auditoria como parte do comportamento verificado. Como o `AuditingConfig`
usa `modifyOnCreate = false`, na criação apenas os campos de criação são preenchidos —
os campos de alteração ficam nulos:

```java
Contact saved = contactRepository.save(contact);

assertThat(saved.getId()).isNotNull();
assertThat(saved.getCreatedAt()).isNotNull();
assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);

assertThat(saved.getUpdatedAt()).isNull();
assertThat(saved.getUpdatedBy()).isNull();
```

Para atualizações, valide que `updatedAt` e `updatedBy` são preenchidos e que os campos
de criação são preservados. Como o teste é transacional (rollback ao final), é preciso
forçar o `flush` do `TestEntityManager` (que dispara o `UPDATE` e o `@PreUpdate`) e
limpar o contexto (`clear`) para reler os valores persistidos:

```java
saved.setName(UPDATED_NAME);
contactRepository.save(saved);
testEntityManager.flush();
testEntityManager.clear();

Contact updated = contactRepository.findById(saved.getId()).orElseThrow();

assertThat(updated.getUpdatedAt()).isNotNull();
assertThat(updated.getUpdatedBy()).isEqualTo(USERNAME);
assertThat(updated.getCreatedAt().getTime()).isEqualTo(saved.getCreatedAt().getTime());
assertThat(updated.getCreatedBy()).isEqualTo(USERNAME);
```

> Compare timestamps com `getTime()`: ao reler do banco, o Hibernate devolve
> `java.sql.Timestamp`, e `Timestamp.equals(Date)` nunca é `true`.

O `auditorProvider` em si (leitura do `SecurityContextHolder`, casos autenticado/não
autenticado) é coberto isoladamente pelo teste unitário `AuditingConfigTest`.

### Estrutura padrão

Organize o teste em três etapas, seguindo as mesmas convenções dos testes de serviço:
`given<Contexto>_when<Ação>_then<Resultado>`, `@DisplayName`, constantes para valores e
helpers `create...` logo abaixo do primeiro teste que os utiliza.

Cubra em cada repositório:
- Persistência e consulta por id (`save` + `findById`).
- Listagem (`findAll`).
- Verificação de existência (`existsById`) para id existente e inexistente.
- Exclusão (`deleteById`).
- Queries de filtro (derivadas ou customizadas), incluindo a combinação de filtros e o cenário sem filtros/resultado vazio.

## Testes de integração da API

### Configuração

Os testes de integração usam o contexto real da aplicação:

```java
@SpringBootTest
@ActiveProfiles("test")
class ApiIntegrationTest {
  // ...
}
```

O perfil `test` está definido em `src/test/resources/application-test.properties` e configura:

- H2 em memória com `jdbc:h2:mem:eeg_test`.
- `spring.jpa.hibernate.ddl-auto=create`.
- Console H2 desabilitado.
- Usuários de teste `joaol`, `teste` e `user`, todos com a senha local `123`.

As entidades iniciais usadas pela API são carregadas por `src/test/resources/import.sql`. Entre as fixtures estão o paciente `1001`, o paciente `1002`, unidades, exame, medicamento, equipamento, época e atividade usados em `ApiIntegrationTest`.

### MockMvc e segurança

Configure o `MockMvc` com o contexto web e o suporte do Spring Security:

```java
mockMvc = MockMvcBuilders.webAppContextSetup(context)
    .apply(springSecurity())
    .build();
```

Em cada requisição protegida, use autenticação Basic explicitamente:

```java
mockMvc.perform(get("/api/unit").with(httpBasic("joaol", "123")))
    .andExpect(status().isOk());
```

Cubra tanto o acesso autenticado quanto o não autenticado. Para requisições JSON, informe `MediaType.APPLICATION_JSON` e valide o status e os campos relevantes da resposta com `jsonPath`.
Crie os payloads a partir de objetos Java e serialize-os com o `ObjectMapper`; evite JSON manual em text blocks.

### O que validar

Os testes de API devem verificar o contrato observável:

- Status HTTP para sucesso, autenticação inválida, entrada inválida e recurso inexistente.
- Tamanho e campos importantes de listas e objetos JSON.
- Criação, atualização e consulta de entidades.
- Auditoria, como `createdBy`, `createdAt` e `updatedBy`, quando aplicável.
- Relações aninhadas retornadas por um endpoint.
- Parâmetros obrigatórios de filtros.

Evite usar o teste de integração para repetir todos os cenários internos já cobertos pelos testes unitários. Adicione um caso de integração quando ele proteger uma parte do contrato HTTP, da configuração Spring, da segurança, da serialização ou da persistência real.

## Teste de contexto

`UfuPpgebEegApplicationTests` valida que o contexto Spring inicia com o perfil `test`:

```java
@SpringBootTest
@ActiveProfiles("test")
class UfuPpgebEegApplicationTests {

  @Test
  void contextLoads() {
  }
}
```

Mantenha esse teste simples. Falhas nele normalmente indicam problema de configuração, dependência, bean ou inicialização do banco.

## Convenções de nomenclatura

- Classe unitária: `<Classe>Test`, por exemplo `PatientServiceImplTest`.
- Classe de integração da API: `ApiIntegrationTest`.
- Método: `given<Contexto>_when<Ação>_then<Resultado>`.
- `@DisplayName`: frase legível no formato `Given ... when ... then ...`.
- Testes de API também devem seguir o formato `given..._when..._then...` e declarar `@DisplayName`.
- Constantes: nomes em maiúsculas para valores compartilhados no cenário.
- Asserções: prefira AssertJ (`assertThat`, `assertThatThrownBy`).

## Como executar

Execute toda a suíte com:

```bash
./mvnw test
```

Para executar uma classe específica:

```bash
./mvnw -Dtest=PatientServiceImplTest test
```

Para executar o build completo, incluindo validações configuradas no Maven:

```bash
./mvnw clean verify
```

O Checkstyle é executado na fase `validate`, e o JaCoCo gera o relatório durante a fase `test`. O build pode também executar a instalação das dependências e a compilação do frontend conforme as fases configuradas no `pom.xml`.

## Checklist para novos testes

1. Identifique a regra ou o contrato que precisa ser protegido.
2. Escolha teste unitário para lógica isolada, teste de repositório para a camada de persistência ou integração para comportamento do sistema/API.
3. Coloque o arquivo no pacote e no diretório correspondentes.
4. Dê ao teste um nome `given_when_then` e um `@DisplayName` descritivo.
5. Cubra o caminho válido e as falhas relevantes da regra.
6. Verifique o resultado e, quando necessário, as chamadas e as chamadas proibidas às dependências.
7. Use fixtures existentes somente quando o cenário for compatível; prefira valores novos para operações que alteram dados.
8. Execute o teste específico e depois `./mvnw test`.
9. Atualize `import.sql` ou `application-test.properties` apenas quando uma nova fixture ou configuração for realmente necessária.

## Exemplos no projeto

- [UnitServiceImplTest](../src/test/java/br/com/ufu/ppgeb/eeg/service/impl/UnitServiceImplTest.java): exemplo unitário mínimo.
- [PatientServiceImplTest](../src/test/java/br/com/ufu/ppgeb/eeg/service/impl/PatientServiceImplTest.java): validações, duplicidade, busca, atualização e exclusão.
- [ExamServiceImplTest](../src/test/java/br/com/ufu/ppgeb/eeg/service/impl/ExamServiceImplTest.java): relações e listas de medicamentos/equipamentos.
- [ContactRepositoryTest](../src/test/java/br/com/ufu/ppgeb/eeg/repository/ContactRepositoryTest.java): `@DataJpaTest` no Spring Boot 4, filtro custom e persistência real com H2.
- [ApiIntegrationTest](../src/test/java/br/com/ufu/ppgeb/eeg/ApiIntegrationTest.java): segurança, MockMvc, JSON e auditoria.
- [UfuPpgebEegApplicationTests](../src/test/java/br/com/ufu/ppgeb/eeg/UfuPpgebEegApplicationTests.java): carregamento do contexto.
