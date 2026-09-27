package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.QUESTIONNAIRE;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.function.Supplier;

import br.com.ufu.ppgeb.eeg.dto.QuestionnaireRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireService;
import org.instancio.Instancio;
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
class QuestionnaireControllerTest {

  private static final Long ID = 1001L;
  private static final String TITLE = "Anamnese do exame";
  private static final String JSON_PATH_TITLE = "$.title";
  private static final String MESSAGE = "title must not be blank";
  private static final int OPERATION_ARGUMENT_INDEX = 3;
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

  @Mock
  private QuestionnaireService questionnaireService;

  @Mock
  private IdempotencyService idempotencyService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
    mockMvc = MockMvcBuilders.standaloneSetup(
        new QuestionnaireController(questionnaireService, idempotencyService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setValidator(validator)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given existing questionnaire ID when finding questionnaire then return questionnaire")
  void givenExistingQuestionnaireId_whenFindingQuestionnaire_thenReturnQuestionnaire()
      throws Exception {
    Questionnaire questionnaire = createQuestionnaire();
    when(questionnaireService.findById(ID)).thenReturn(questionnaire);

    mockMvc.perform(get(QUESTIONNAIRE + PATH_SEPARATOR + ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_TITLE).value(TITLE));

    verify(questionnaireService).findById(ID);
  }

  @Test
  @DisplayName("Given unknown questionnaire ID when finding questionnaire then return not found")
  void givenUnknownQuestionnaireId_whenFindingQuestionnaire_thenReturnNotFound() throws Exception {
    when(questionnaireService.findById(ID))
        .thenThrow(new ResourceNotFoundException("Questionário", ID));

    mockMvc.perform(get(QUESTIONNAIRE + PATH_SEPARATOR + ID))
        .andExpect(status().isNotFound());

    verify(questionnaireService).findById(ID);
  }

  @Test
  @DisplayName("Given title when listing questionnaires then return matching questionnaires")
  void givenTitle_whenListingQuestionnaires_thenReturnQuestionnaires() throws Exception {
    Questionnaire questionnaire = createQuestionnaire();
    when(questionnaireService.findByFilter(isNull(), eq(TITLE), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(questionnaire), PAGE_REQUEST, 1));

    mockMvc.perform(get(QUESTIONNAIRE).param("title", TITLE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].title").value(TITLE));

    verify(questionnaireService).findByFilter(isNull(), eq(TITLE), any(Pageable.class));
  }

  @Test
  @DisplayName("Given valid questionnaire when creating questionnaire then return created with location")
  void givenValidQuestionnaire_whenCreatingQuestionnaire_thenReturnCreated() throws Exception {
    Questionnaire saved = createQuestionnaire();
    when(questionnaireService.save(any(Questionnaire.class))).thenReturn(saved);

    doAnswer(invocation -> {
      Supplier<?> operation = invocation.getArgument(OPERATION_ARGUMENT_INDEX);
      return operation.get();
    }).when(idempotencyService).execute(any(), any(), any(), any());

    String body = objectMapper.writeValueAsString(Instancio.create(QuestionnaireRequest.class));

    mockMvc.perform(post(QUESTIONNAIRE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath(JSON_PATH_TITLE).value(TITLE))
        .andExpect(header().string("Location",
            QUESTIONNAIRE + PATH_SEPARATOR + ID));

    verify(questionnaireService).save(any(Questionnaire.class));
  }

  @Test
  @DisplayName("Given questionnaire with blank title when creating questionnaire then return bad request")
  void givenQuestionnaireWithBlankTitle_whenCreatingQuestionnaire_thenReturnBadRequest()
      throws Exception {
    QuestionnaireRequest request = Instancio.of(QuestionnaireRequest.class)
        .set(field(QuestionnaireRequest::title), null)
        .create();

    String body = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(QUESTIONNAIRE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(MESSAGE));
  }

  @Test
  @DisplayName("Given existing questionnaire when updating questionnaire then return updated questionnaire")
  void givenExistingQuestionnaire_whenUpdatingQuestionnaire_thenReturnUpdatedQuestionnaire()
      throws Exception {
    Questionnaire updated = createQuestionnaire();
    when(questionnaireService.update(any(Questionnaire.class))).thenReturn(updated);

    String body = objectMapper.writeValueAsString(Instancio.create(QuestionnaireRequest.class));

    mockMvc.perform(put(QUESTIONNAIRE + PATH_SEPARATOR + ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_TITLE).value(TITLE));

    verify(questionnaireService).update(any(Questionnaire.class));
  }

  private Questionnaire createQuestionnaire() {
    return Instancio.of(Questionnaire.class)
        .set(field(Questionnaire::getId), ID)
        .set(field(Questionnaire::getTitle), TITLE)
        .set(field(Questionnaire::getQuestionnaireQuestions), List.of())
        .create();
  }
}