package br.com.ufu.ppgeb.eeg;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM_ID_PATTERN;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM_QUESTIONNAIRES;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM_QUESTIONNAIRE_ANSWERS;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.QUESTIONNAIRE_ID_PATTERN;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ExamQuestionnaireIntegrationTest {

  private static final String USERNAME = "joaol";
  private static final String PASSWORD = "123";
  private static final Long EXAM_ID = 1001L;
  private static final Long QUESTIONNAIRE_ID = 1001L;
  private static final Long QUESTION_SINGLE_SELECT = 1001L;
  private static final Long QUESTION_COLLABORATED = 1002L;
  private static final Long QUESTION_FREE_TEXT = 1003L;
  private static final Long ANSWER_OPTION_LUCIDO = 1001L;
  private static final Long ANSWER_OPTION_SIM = 1004L;
  private static final int ANSWERS_COUNT = 3;
  private static final int EXAM_OBJECT_TYPE = 2;
  private static final String FREE_TEXT = "Paciente adormeceu ao final do registro.";
  private static final String JSON_PATH_LENGTH = "$.length()";

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).build();
  }

  @Test
  @DisplayName("Given exam ID when listing exam questionnaires then return questionnaires")
  void givenExamId_whenListingExamQuestionnaires_thenReturnQuestionnaires() throws Exception {
    // First, create answers for the exam so questionnaires appear as applied
    String body = objectMapper.writeValueAsString(createAnswerRequests());
    mockMvc.perform(put(examQuestionnaireAnswersUrl())
            .with(httpBasic(USERNAME, PASSWORD))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk());

    // Now list questionnaires applied to the exam
    mockMvc.perform(get(examQuestionnairesUrl())
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(QUESTIONNAIRE_ID.intValue()))
        .andExpect(jsonPath("$.content[0].title").value("Anamnese do exame"));
  }

  @Test
  @DisplayName("Given answers when updating exam questionnaire answers then save and return them")
  void givenAnswers_whenUpdatingExamQuestionnaireAnswers_thenSaveAndReturnThem()
      throws Exception {
    String body = objectMapper.writeValueAsString(createAnswerRequests());

    mockMvc.perform(put(examQuestionnaireAnswersUrl())
            .with(httpBasic(USERNAME, PASSWORD))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(ANSWERS_COUNT));
  }

  @Test
  @DisplayName("Given saved answers when finding exam questionnaire answers then return them")
  void givenSavedAnswers_whenFindingExamQuestionnaireAnswers_thenReturnThem() throws Exception {
    String body = objectMapper.writeValueAsString(createAnswerRequests());
    mockMvc.perform(put(examQuestionnaireAnswersUrl())
            .with(httpBasic(USERNAME, PASSWORD))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk());

    mockMvc.perform(get(examQuestionnaireAnswersUrl())
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(ANSWERS_COUNT))
        .andExpect(jsonPath("$.[?(@.questionId == 1001)].objectType").value(EXAM_OBJECT_TYPE))
        .andExpect(jsonPath("$.[?(@.questionId == 1001)].objectId").value(EXAM_ID.intValue()));
  }

  private List<QuestionnaireAnswerRequest> createAnswerRequests() {
    return List.of(
        new QuestionnaireAnswerRequest(null, QUESTION_SINGLE_SELECT, null,
            List.of(ANSWER_OPTION_LUCIDO)),
        new QuestionnaireAnswerRequest(null, QUESTION_COLLABORATED, null,
            List.of(ANSWER_OPTION_SIM)),
        new QuestionnaireAnswerRequest(null, QUESTION_FREE_TEXT, FREE_TEXT, List.of()));
  }

  private String examQuestionnairesUrl() {
    return EXAM_QUESTIONNAIRES.replace(EXAM_ID_PATTERN, String.valueOf(EXAM_ID));
  }

  private String examQuestionnaireAnswersUrl() {
    return EXAM_QUESTIONNAIRE_ANSWERS
        .replace(EXAM_ID_PATTERN, String.valueOf(EXAM_ID))
        .replace(QUESTIONNAIRE_ID_PATTERN, String.valueOf(QUESTIONNAIRE_ID));
  }
}