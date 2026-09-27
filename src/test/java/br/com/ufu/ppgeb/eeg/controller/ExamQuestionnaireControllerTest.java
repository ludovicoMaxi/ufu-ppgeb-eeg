package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM_ID_PATTERN;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM_QUESTIONNAIRES;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM_QUESTIONNAIRE_ANSWERS;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.QUESTIONNAIRE_ID_PATTERN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswerOption;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireAnswerService;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ExamQuestionnaireControllerTest {

  private static final Long EXAM_ID = 1001L;
  private static final Long QUESTIONNAIRE_ID = 2001L;
  private static final Long QUESTION_ID = 3003L;
  private static final Long ANSWER_ID = 4004L;
  private static final Long ANSWER_OPTION_ID = 1001L;
  private static final String ANSWER_OPTION_TEXT = "Lúcido";
  private static final String FREE_TEXT = "Paciente adormeceu ao final do registro.";
  private static final String MESSAGE = "questionId must not be null";
  private static final String JSON_PATH_LENGTH = "$.length()";
  private static final String JSON_PATH_QUESTION_ID = "$[0].questionId";
  private static final String JSON_PATH_FREE_TEXT = "$[0].freeText";
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

  @Mock
  private QuestionnaireService questionnaireService;

  @Mock
  private QuestionnaireAnswerService questionnaireAnswerService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
    mockMvc = MockMvcBuilders.standaloneSetup(
        new ExamQuestionnaireController(questionnaireService, questionnaireAnswerService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setValidator(validator)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given exam ID when listing exam questionnaires then return questionnaires")
  void givenExamId_whenListingExamQuestionnaires_thenReturnQuestionnaires() throws Exception {
    Questionnaire questionnaire = Questionnaire.builder()
        .id(QUESTIONNAIRE_ID)
        .title("Anamnese do exame")
        .build();
    when(questionnaireAnswerService.findAppliedQuestionnaires(
        eq(ObjectType.EXAM.getId()), eq(EXAM_ID), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(questionnaire), PAGE_REQUEST, 1));

    mockMvc.perform(get(examQuestionnairesUrl()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));

    verify(questionnaireAnswerService).findAppliedQuestionnaires(
        eq(ObjectType.EXAM.getId()), eq(EXAM_ID), any(Pageable.class));
  }

  @Test
  @DisplayName("Given exam and questionnaire IDs when finding answers then return answers")
  void givenExamAndQuestionnaireIds_whenFindingAnswers_thenReturnAnswers() throws Exception {
    QuestionnaireAnswer answer = createAnswer();
    when(questionnaireAnswerService.findAppliedAnswers(eq(QUESTIONNAIRE_ID),
        eq(ObjectType.EXAM.getId()), eq(EXAM_ID)))
        .thenReturn(List.of(answer));

    mockMvc.perform(get(examQuestionnaireAnswersUrl()))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_QUESTION_ID).value(QUESTION_ID))
        .andExpect(jsonPath("$[0].objectType").value(ObjectType.EXAM.getId()))
        .andExpect(jsonPath("$[0].objectId").value(EXAM_ID))
        .andExpect(jsonPath(JSON_PATH_FREE_TEXT).value(FREE_TEXT))
        .andExpect(jsonPath("$[0].answerOptions[0].id").value(ANSWER_OPTION_ID))
        .andExpect(jsonPath("$[0].answerOptions[0].text").value(ANSWER_OPTION_TEXT));

    verify(questionnaireAnswerService).findAppliedAnswers(eq(QUESTIONNAIRE_ID),
        eq(ObjectType.EXAM.getId()), eq(EXAM_ID));
  }

  @Test
  @DisplayName("Given answers when updating exam questionnaire answers then return updated answers")
  void givenAnswers_whenUpdatingExamQuestionnaireAnswers_thenReturnUpdatedAnswers()
      throws Exception {
    QuestionnaireAnswer saved = createAnswer();
    when(questionnaireAnswerService.updateList(eq(QUESTIONNAIRE_ID),
        eq(ObjectType.EXAM.getId()), eq(EXAM_ID), anyList()))
        .thenReturn(List.of(saved));

    QuestionnaireAnswerRequest request = new QuestionnaireAnswerRequest(
        null, QUESTION_ID, FREE_TEXT, null);
    String body = objectMapper.writeValueAsString(List.of(request));

    mockMvc.perform(put(examQuestionnaireAnswersUrl())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_QUESTION_ID).value(QUESTION_ID))
        .andExpect(jsonPath(JSON_PATH_FREE_TEXT).value(FREE_TEXT));

    verify(questionnaireAnswerService).updateList(eq(QUESTIONNAIRE_ID),
        eq(ObjectType.EXAM.getId()), eq(EXAM_ID), anyList());
  }

  @Test
  @DisplayName("Given answer with null question when updating exam questionnaire answers then return bad request")
  void givenAnswerWithNullQuestion_whenUpdatingExamQuestionnaireAnswers_thenReturnBadRequest()
      throws Exception {
    QuestionnaireAnswerRequest request = new QuestionnaireAnswerRequest(
        null, null, null, null);
    String body = objectMapper.writeValueAsString(List.of(request));

    mockMvc.perform(put(examQuestionnaireAnswersUrl())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(MESSAGE));
  }

  private String examQuestionnairesUrl() {
    return EXAM_QUESTIONNAIRES.replace(EXAM_ID_PATTERN, String.valueOf(EXAM_ID));
  }

  private String examQuestionnaireAnswersUrl() {
    return EXAM_QUESTIONNAIRE_ANSWERS
        .replace(EXAM_ID_PATTERN, String.valueOf(EXAM_ID))
        .replace(QUESTIONNAIRE_ID_PATTERN, String.valueOf(QUESTIONNAIRE_ID));
  }

  private QuestionnaireAnswer createAnswer() {
    QuestionnaireAnswer answer = QuestionnaireAnswer.builder()
        .id(ANSWER_ID)
        .questionnaire(Questionnaire.builder().id(QUESTIONNAIRE_ID).build())
        .question(Question.builder().id(QUESTION_ID).build())
        .objectType(ObjectType.EXAM.getId())
        .objectId(EXAM_ID)
        .freeText(FREE_TEXT)
        .build();
    QuestionnaireAnswerOption option = QuestionnaireAnswerOption.builder()
        .questionnaireAnswer(answer)
        .answerOption(AnswerOption.builder().id(ANSWER_OPTION_ID).text(ANSWER_OPTION_TEXT).build())
        .build();
    answer.setAnswerOptions(List.of(option));
    return answer;
  }
}