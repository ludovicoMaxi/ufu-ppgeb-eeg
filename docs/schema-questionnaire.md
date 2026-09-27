# Esquema do questionário

## Visão geral

O questionário é composto por um bloco de **catálogo** (questões, alternativas e
questionários reutilizáveis) e um bloco de **aplicações** (respostas dadas para uma
instância de objeto, hoje um exame). O bloco de aplicação não possui uma entidade
"aplicação": a aplicação é implícita pelo conjunto de respostas de um mesmo
`(questionnaire_id, object_type, object_id)`.

Todas as tabelas possuem auditoria (`created_at`, `created_by`, `updated_at`,
`updated_by`) preenchida pelo `AuditingEntityListener` e sequences próprias
(`*_SQ`) com `allocationSize = 1`.

## Bloco 1 — Catálogo

### QUESTION

Questão reutilizável do catálogo.

| Campo         | Tipo          | Regra |
|---------------|---------------|-------|
| `ID`          | Long (PK)     | sequence `QUESTION_SQ` |
| `TEXT`        | VARCHAR(512)  | not null |
| `DESCRIPTION` | VARCHAR(1024) | opcional |
| `QUESTION_TYPE` | VARCHAR(32)  | not null; enum `SINGLE_SELECT` / `MULTI_SELECT` / `FREE_TEXT` |
| `ACTIVE`      | BOOLEAN       | opcional |
| auditoria     | —             | padrão |

### ANSWER_OPTION

Alternativa de resposta do catálogo, **reutilizável entre questões** (não possui
`question_id` nem `sort_order` próprios).

| Campo    | Tipo          | Regra |
|----------|---------------|-------|
| `ID`     | Long (PK)     | sequence `ANSWER_OPTION_SQ` |
| `TEXT`   | VARCHAR(512)  | not null |
| `ACTIVE` | BOOLEAN       | opcional |
| auditoria | —            | padrão |

### QUESTION_ANSWER_OPTION

Vínculo questão ↔ alternativa do catálogo (n-árario, com ordem por questão).

| Campo            | Tipo  | Regra |
|------------------|-------|-------|
| `ID`             | Long (PK) | sequence `QUESTION_ANSWER_OPTION_SQ` |
| `QUESTION_ID`    | Long (FK → QUESTION) | not null |
| `ANSWER_OPTION_ID` | Long (FK → ANSWER_OPTION) | not null |
| `SORT_ORDER`     | Long  | opcional; ordem da alternativa dentro da questão |
| auditoria        | —     | padrão |

- `UNIQUE (QUESTION_ID, ANSWER_OPTION_ID)`.

### QUESTIONNAIRE

Questionário de aplicação a um objeto (exame).

| Campo          | Tipo          | Regra |
|----------------|---------------|-------|
| `ID`           | Long (PK)     | sequence `QUESTIONNAIRE_SQ` |
| `TITLE`        | VARCHAR(256)  | not null |
| `DESCRIPTION`  | VARCHAR(1024) | opcional |
| auditoria      | —             | padrão |

### QUESTIONNAIRE_QUESTION

Vínculo questionário ↔ questão (com ordem da questão dentro do questionário).

| Campo             | Tipo  | Regra |
|-------------------|-------|-------|
| `ID`              | Long (PK) | sequence `QUESTIONNAIRE_QUESTION_SQ` |
| `QUESTIONNAIRE_ID` | Long (FK → QUESTIONNAIRE) | not null |
| `QUESTION_ID`     | Long (FK → QUESTION) | not null |
| `SORT_ORDER`      | Long  | opcional; ordem da questão no questionário |
| auditoria         | —     | padrão |

- `UNIQUE (QUESTIONNAIRE_ID, QUESTION_ID)`.

## Bloco 2 — Respostas (aplicação)

### QUESTIONNAIRE_ANSWER

Resposta dada a uma questão de um questionário, para um objeto (exame). Os campos
`object_type` e `object_id` são **desnormalizados** (sem FK), seguindo o padrão
`CONTACT`. Não há tabela de "aplicação": a aplicação é o conjunto de respostas de um
mesmo `(questionnaire_id, object_type, object_id)`.

| Campo             | Tipo  | Regra |
|-------------------|-------|-------|
| `ID`              | Long (PK) | sequence `QUESTIONNAIRE_ANSWER_SQ` |
| `QUESTIONNAIRE_ID` | Long (FK → QUESTIONNAIRE) | not null |
| `QUESTION_ID`     | Long (FK → QUESTION) | not null |
| `OBJECT_TYPE`     | Long  | not null; `ObjectType.EXAM = 2L` |
| `OBJECT_ID`       | Long  | not null; id do exame |
| `FREE_TEXT`       | VARCHAR(2048) | opcional; obrigatório para `FREE_TEXT` |
| auditoria         | —     | padrão |

- `UNIQUE (QUESTIONNAIRE_ID, QUESTION_ID, OBJECT_TYPE, OBJECT_ID)`.
- Índice para as consultas por aplicação:
  `(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID)`.

### QUESTIONNAIRE_ANSWER_OPTION

Alternativas marcadas em uma resposta. **Multi-seleção normalizada**: uma linha por
alternativa marcada (um `SINGLE_SELECT` tem exatamente uma linha; um `MULTI_SELECT`
tem uma ou mais).

| Campo                | Tipo  | Regra |
|----------------------|-------|-------|
| `ID`                 | Long (PK) | sequence `QUESTIONNAIRE_ANSWER_OPTION_SQ` |
| `QUESTIONNAIRE_ANSWER_ID` | Long (FK → QUESTIONNAIRE_ANSWER) | not null |
| `ANSWER_OPTION_ID`   | Long (FK → ANSWER_OPTION) | not null |
| auditoria            | —     | padrão |

- `UNIQUE (QUESTIONNAIRE_ANSWER_ID, ANSWER_OPTION_ID)`.

## Regras de validação

- Referência **viva**: respostas apontam para `QUESTION`/`ANSWER_OPTION` atuais,
  sem congelar texto (sem snapshot).
- `SINGLE_SELECT`: exatamente uma alternativa selecionada; `FREE_TEXT` vazio.
- `MULTI_SELECT`: uma ou mais alternativas selecionadas; `FREE_TEXT` vazio.
- `FREE_TEXT`: texto obrigatório; nenhuma alternativa selecionada.
- As alternativas selecionadas devem estar vinculadas à questão
  (`QUESTION_ANSWER_OPTION`).
- A questão respondida deve pertencer ao questionário (`QUESTIONNAIRE_QUESTION`).
- Questão de seleção (`SINGLE_SELECT`/`MULTI_SELECT`) deve ter ao menos uma
  alternativa vinculada; `FREE_TEXT` não deve ter alternativas.
- Um questionário com questões registradas não pode ser removido se houver
  respostas; idem para questão usada em questionário.

## Sonar/SQL de referência

```sql
CREATE SEQUENCE QUESTION_SQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE ANSWER_OPTION_SQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE QUESTION_ANSWER_OPTION_SQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE QUESTIONNAIRE_SQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE QUESTIONNAIRE_QUESTION_SQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE QUESTIONNAIRE_ANSWER_SQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE QUESTIONNAIRE_ANSWER_OPTION_SQ START WITH 1 INCREMENT BY 1;
```