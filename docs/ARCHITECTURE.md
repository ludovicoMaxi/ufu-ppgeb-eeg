# Arquitetura e convenções

Este documento descreve a arquitetura em camadas do `ufu-ppgeb-eeg` e as convenções de
organização do código, com ênfase em **evitar duplicação** e manter a consistência entre
produção e testes.

## Contexto

Aplicação Spring Boot (Java 25, gerenciada pelo Maven) para cadastro de exames de EEG da
UFU. Backend em Java, frontend em React/Webpack em `src/main/webapp`.

## Camadas (three-tier)

O código segue uma arquitetura em três camadas. As dependências devem fluir de cima para
baixo: `controller -> service -> repository`. Um controller não deve acessar repositório
diretamente, e um serviço não deve expor objetos de entidade para a camada web quando houver
um DTO.

```text
src/main/java/br/com/ufu/ppgeb/eeg/
├── config/      Configuração Spring (SecurityConfig, AuditingConfig).
├── constant/    Constantes compartilhadas (paths de URL, formatos de data).
├── controller/  Pontos de entrada HTTP/REST (camada web).
├── dto/         Objetos de transferência de dados de requisição/resposta.
├── exception/   Exceções de domínio e o GlobalExceptionHandler.
├── mapper/      Conversão entre DTO e entidade.
├── model/       Entidades JPA.
├── repository/  Persistência (interfaces Spring Data).
├── service/     Regras de negócio (interface + impl em service/impl).
├── utils/       Utilitários auxiliares.
└── view/        Objetos de resposta de listagens (ex.: ActivityList, EpochList).
```

Convenções de camada:

- **Controller**: recebe a requisição, valida com `@Valid`, delega ao serviço e retorna
  `ResponseEntity`. Controllers e ações são mapeados por paths centralizados em `ApiPaths`
  (ver abaixo).
- **Service**: implementa a regra de negócio, orquestra repositórios e converte DTO/entidade
  (via `mapper`). Interfaces em `service`, implementações em `service.impl`.
- **Repository**: apenas persistência (queries derivadas ou customizadas com Criteria).

## Constantes centralizadas (`constant`)

Os paths de URL da aplicação ficam centralizados em `br.com.ufu.ppgeb.eeg.constant.ApiPaths`
(classe `@UtilityClass` do Lombok). **Nunca** declare o mesmo path em mais de um lugar, e
**não** duplique literais de URL em testes.

- `ApiPaths.API` é o prefixo `/api`.
- Paths completos derivam do prefixo: `API + "/patient"` etc.
- Subpaths reutilizados são constantes próprias: `MEDICAMENT_SUBPATH = "/medicament"`,
  `EQUIPMENT_SUBPATH = "/equipment"`, com o path completo montado a partir deles.
- `PATH_SEPARATOR = "/"` é usado para montar rotas com parâmetro (ex.: `/api/patient/{id}`).
- `HOME` reaproveita `PATH_SEPARATOR`.

### Uso em produção

Nos controllers, referencie as constantes via import estático:

```java
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATIENT;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;

@PostMapping(PATIENT)
public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
  // ...
}
```

### Uso em testes

Nos testes de controller, use os mesmos imports estáticos de `ApiPaths` (ex.: `ACTIVITY`,
`EXAM`, `PATIENT`, `PATH_SEPARATOR`, `MEDICAMENT_SUBPATH`) em vez de repetir os literais.
Assim, se um path mudar em produção, o teste acompanha automaticamente:

```java
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.MEDICAMENT_SUBPATH;

mockMvc.perform(put(EXAM + PATH_SEPARATOR + EXAM_ID))
    .andExpect(status().isOk());
```

Formatos de data centralizados ficam em `constant/DateFormats`.

> **Regra de ouro:** um literal de URL ou de path não deve ser duplicado. Se aparecer em mais
> de um lugar, mova-o para `ApiPaths` (ou para uma constante local quando for um valor de
> cenário específico de um único teste).

## Mensagens e regras não duplicadas

- Mensagens de validação/mensagens de negócio reutilizadas vivem próximas à entidade/DTO que
  as originam (ex.: em `model.Exam` para a mensagem de filtro). Não repita a mesma mensagem em
  produção e em testes — os testes referenciam a mesma fonte quando possível.

## Auditoria

Os campos de auditoria (`createdAt`, `createdBy`, `updatedAt`, `updatedBy`) são preenchidos
pelo `AuditingConfig` (`@EnableJpaAuditing` com o bean `auditorProvider`, que lê o
`SecurityContextHolder`, e `modifyOnCreate = false`), **não** manualmente no `save`. Não
preencha esses campos à mão. Isso vale tanto para código de produção quanto para
fixtures/testes (ver `docs/TESTING.md`).

## Frontend

O frontend fica em `src/main/webapp` (React/Webpack). Este arquivo documenta apenas o
backend; para convenções do frontend, consulte o código em `src/main/webapp`.

## Testes

A estratégia completa de testes (isolados de controller, unitários de serviço, de repositório
e de integração) está em [`docs/TESTING.md`](TESTING.md). Leia-o antes de criar ou alterar
testes. As convenções de nulidade (`isNull`/`nonNull` de `java.util.Objects`) e de coleções
(`isNotEmpty`/`isEmpty` de `org.apache.commons.collections4.CollectionUtils`) estão resumidas
em [`AGENTS.md`](../AGENTS.md).
