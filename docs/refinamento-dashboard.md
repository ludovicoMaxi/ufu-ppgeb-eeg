# Refinamento do dashboard

## Visão geral

A tela inicial (`src/main/webapp/dashboard/dashboard.jsx`) hoje é um placeholder. Este
refinamento define os indicadores, as fontes de dados e o contrato de agregação que
serão usados na primeira versão, com base no template AdminLTE v4 já presente no
projeto (`admin-lte ^4.3.1`).

Os indicadores são calculados **no backend** por um endpoint de resumo, em vez de o
frontend buscar listas completas e contar no cliente. Nenhuma entidade é exposta nas
respostas: os DTOs são dedicados (ver `AGENTS.md`).

O dashboard é somente leitura. Não há mutação de dados nem ações de escrita além de
links para as telas existentes.

## Fontes de dados

Entidades envolvidas (somente os campos usados pelo dashboard):

| Entidade          | Campos usados | Observação |
|-------------------|---------------|------------|
| `PATIENT`         | `ID`, `SEX`, `BIRTH_DATE`, `CIVIL_STATUS` | `SEX` e `CIVIL_STATUS` são enums `STRING` |
| `EXAM_REQUEST`    | `ID`, `PATIENT_ID`, `REQUEST_DATE`, `ACHIEVEMENT_DATE` | `REQUEST_DATE` é a data da solicitação |
| `EXAM`            | `ID`, `EXAM_REQUEST_ID` (OneToOne), `PATIENT_ID`, `ACHIEVEMENT_DATE` | **não há campo de status** |
| `QUESTIONNAIRE`   | `ID`, `TITLE` | |
| `QUESTIONNAIRE_ANSWER` | `ID`, `QUESTIONNAIRE_ID`, `OBJECT_TYPE`, `OBJECT_ID` | `OBJECT_TYPE = 2L` para exame |
| `ACTIVITY`        | `ID`, `EXAM_ID`, `START_TIME`, `DURATION`, `DESCRIPTION` | atividade registrada no exame |

### Regra de situação do exame

Como `EXAM` não possui status, a situação é derivada:

- **Solicitação pendente**: `EXAM_REQUEST` sem `EXAM` vinculado (nenhum `EXAM.EXAM_REQUEST_ID`
  apontando para ela).
- **Exame realizado**: `EXAM` vinculado a uma `EXAM_REQUEST`.
- **Exame avulso**: `EXAM` sem `EXAM_REQUEST_ID` (cadastrado sem solicitação prévia).

> Decisão em aberto: se a equipe precisar de estados intermediários (ex.: "em análise",
> "laudado"), será necessário um campo de status em `EXAM` ou `EXAM_REQUEST`. Esse
> campo **não** está previsto nesta versão.

## Indicadores

### Bloco 1 — Cartões (KPIs)

| Chave              | Descrição | Regra de cálculo | Destino ao clicar |
|--------------------|-----------|------------------|-------------------|
| `patientsTotal`    | Pacientes cadastrados | `COUNT(PATIENT)` | listagem de pacientes |
| `examsTotal`       | Exames cadastrados | `COUNT(EXAM)` | listagem de exames |
| `examsThisMonth`   | Exames realizados no mês corrente | `EXAM.ACHIEVEMENT_DATE` dentro do mês atual | listagem de exames filtrada |
| `requestsPending`  | Solicitações sem exame | `EXAM_REQUEST` sem `EXAM` vinculado | listagem de solicitações |
| `questionnairesAnswered` | Questionários respondidos (aplicações distintas) | `COUNT(DISTINCT QUESTIONNAIRE_ID, OBJECT_ID)` em `QUESTIONNAIRE_ANSWER` com `OBJECT_TYPE = 2` | — |

### Bloco 2 — Gráficos

| Chave | Tipo de gráfico | Eixo / agrupamento | Período |
|-------|-----------------|--------------------|---------|
| `examsByMonth` | Barra/linha | Mês de `EXAM.ACHIEVEMENT_DATE` | últimos 12 meses |
| `requestsByMonth` | Linha (sobreposta a `examsByMonth`) | Mês de `EXAM_REQUEST.REQUEST_DATE` | últimos 12 meses |
| `patientsBySex` | Rosca | `PATIENT.SEX` | total |
| `answersByQuestionnaire` | Barra horizontal | `QUESTIONNAIRE.TITLE` com contagem de aplicações | total |

Meses sem registros devem vir preenchidos com zero, para o gráfico ter eixo contínuo.

### Bloco 3 — Listas

| Chave | Conteúdo | Ordenação | Limite |
|-------|----------|-----------|--------|
| `recentExams` | Últimos exames: paciente, data de realização, origem (com ou sem solicitação) | `ACHIEVEMENT_DATE DESC` | 5 |
| `pendingRequests` | Solicitações pendentes: paciente, data da solicitação | `REQUEST_DATE ASC` (mais antigas primeiro) | 5 |
| `recentActivities` | Atividades recentes | `START_TIME DESC` | 5 |

A lista de pendentes é ordenada do mais antigo para o mais novo para evidenciar
atrasos.

## Contrato da API

Endpoint(s) propostos, com URLs definidas em `ApiPaths` (sem literais duplicados):

| Método | Caminho | Retorno |
|--------|---------|---------|
| `GET` | `ApiPaths.DASHBOARD_SUMMARY` | Bloco 1 (`DashboardSummaryResponse`) |
| `GET` | `ApiPaths.DASHBOARD_CHARTS` | Bloco 2 (`DashboardChartsResponse`) |
| `GET` | `ApiPaths.DASHBOARD_LISTS` | Bloco 3 (`DashboardListsResponse`) |

Regras do contrato:

- Respostas usam apenas DTOs de resposta; nenhum tipo do pacote `model`.
- Os nomes de campos seguem as chaves da tabela acima (`camelCase`).
- Datas em ISO-8601.
- Nenhum parâmetro obrigatório. Períodos são fixos nesta versão.
- O endpoint não aceita escrita; apenas `GET`.

## Regras de validação e negócio

- Um `EXAM` pode estar vinculado a no máximo uma `EXAM_REQUEST` (`EXAM.EXAM_REQUEST_ID`
  é OneToOne). Uma solicitação é "pendente" enquanto não tiver exame vinculado.
- `ACHIEVEMENT_DATE` nulo não entra em `examsThisMonth` nem em `examsByMonth`.
- `examsByMonth` e `requestsByMonth` usam o fuso do servidor; a conversão para mês
  deve ser feita sobre `ZonedDateTime` e documentada no teste.
- `questionnairesAnswered` conta aplicações distintas, não linhas de
  `QUESTIONNAIRE_ANSWER`: um mesmo questionário com várias questões conta uma vez por
  objeto.
- Se a contagem de um indicador falhar, o endpoint responde o erro padrão do
  `GlobalExceptionHandler`; o frontend exibe o cartão com estado de erro, sem derrubar
  a página inteira.

## Requisitos não funcionais

- Desempenho: as consultas de agregação devem usar índices existentes em `EXAM`,
  `EXAM_REQUEST` e `QUESTIONNAIRE_ANSWER`. Verificar com `EXPLAIN` antes de criar
  índices novos.
- Carga: o resumo deve responder em até 500 ms com o volume atual de produção.
- Segurança: o endpoint segue as mesmas regras de autorização das telas de exame.
- Cache: não há cache nesta versão; os dados são atualizados a cada carregamento.

## Estratégia de testes

Seguindo `docs/TESTING.md` e `AGENTS.md`:

- **Serviço de agregação**: teste unitário com Mockito e Instancio, cobrindo
  pendentes, exames avulsos, meses sem registro e `ACHIEVEMENT_DATE` nulo.
- **Controller**: `MockMvcBuilders.standaloneSetup` com `GlobalExceptionHandler`,
  verificando status, corpo JSON e os caminhos de `ApiPaths`.
- **Repositório**: `@DataJpaTest` com H2 para as consultas de agregação.
- **Frontend**: teste dos cartões com estados de carregamento, vazio e erro.

## Pontos em aberto

1. Confirmar a regra de "pendente" com a equipe (solicitação sem exame).
2. Definir se o dashboard deve filtrar por período ou por usuário.
3. Decidir se haverá campo de status de exame em uma versão futura.
4. Confirmar quais atividades entram em `recentActivities` (todas ou só do exame).

## SQL de referência

```sql
-- Solicitações pendentes (sem exame vinculado)
SELECT er.ID, er.PATIENT_ID, er.REQUEST_DATE
FROM EXAM_REQUEST er
LEFT JOIN EXAM e ON e.EXAM_REQUEST_ID = er.ID
WHERE e.ID IS NULL
ORDER BY er.REQUEST_DATE ASC
LIMIT 5;

-- Exames realizados por mês (últimos 12 meses)
SELECT FORMATDATETIME(e.ACHIEVEMENT_DATE, 'yyyy-MM') AS MES, COUNT(*) AS TOTAL
FROM EXAM e
WHERE e.ACHIEVEMENT_DATE >= DATEADD('MONTH', -12, CURRENT_DATE)
GROUP BY FORMATDATETIME(e.ACHIEVEMENT_DATE, 'yyyy-MM')
ORDER BY MES;
```

> As consultas acima são de referência e usam sintaxe H2. Para PostgreSQL/MySQL a
> função de formatação de data é outra; o repositório deve usar JPQL portável ou a
> sintaxe do banco de produção, conforme o driver em uso.
