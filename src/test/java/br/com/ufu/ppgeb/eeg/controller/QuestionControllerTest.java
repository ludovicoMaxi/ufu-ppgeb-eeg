package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.QUESTION;
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

import br.com.ufu.ppgeb.eeg.dto.QuestionRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
import br.com.ufu.ppgeb.eeg.service.QuestionService;
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
class QuestionControllerTest {

  private static final Long ID = 1001L;
  private static final String TEXT = "Paciente está lúcido?";
  private static final QuestionType QUESTION_TYPE = QuestionType.SINGLE_SELECT;
  private static final String JSON_PATH_TEXT = "$.text";
  private static final String JSON_PATH_QUESTION_TYPE = "$.questionType";
  private static final String MESSAGE = "text must not be blank";
  private static final int OPERATION_ARGUMENT_INDEX = 3;
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

  @Mock
  private QuestionService questionService;

  @Mock
  private IdempotencyService idempotencyService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
    mockMvc = MockMvcBuilders.standaloneSetup(
        new QuestionController(questionService, idempotencyService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setValidator(validator)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given existing question ID when finding question then return question")
  void givenExistingQuestionId_whenFindingQuestion_thenReturnQuestion() throws Exception {
    Question question = createQuestion();
    when(questionService.findById(ID)).thenReturn(question);

    mockMvc.perform(get(QUESTION + PATH_SEPARATOR + ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_TEXT).value(TEXT));

    verify(questionService).findById(ID);
  }

  @Test
  @DisplayName("Given unknown question ID when finding question then return not found")
  void givenUnknownQuestionId_whenFindingQuestion_thenReturnNotFound() throws Exception {
    when(questionService.findById(ID))
        .thenThrow(new ResourceNotFoundException("Questão", ID));

    mockMvc.perform(get(QUESTION + PATH_SEPARATOR + ID))
        .andExpect(status().isNotFound());

    verify(questionService).findById(ID);
  }

  @Test
  @DisplayName("Given question type when listing questions then return matching questions")
  void givenQuestionType_whenListingQuestions_thenReturnQuestions() throws Exception {
    Question question = createQuestion();
    when(questionService.findByFilter(isNull(), isNull(), eq(QUESTION_TYPE), isNull(),
        any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(question), PAGE_REQUEST, 1));

    mockMvc.perform(get(QUESTION).param("questionType", QUESTION_TYPE.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].questionType").value(QUESTION_TYPE.name()));

    verify(questionService).findByFilter(isNull(), isNull(), eq(QUESTION_TYPE), isNull(),
        any(Pageable.class));
  }

  @Test
  @DisplayName("Given valid question when creating question then return created with location")
  void givenValidQuestion_whenCreatingQuestion_thenReturnCreated() throws Exception {
    Question saved = createQuestion();
    when(questionService.save(any(Question.class))).thenReturn(saved);

    doAnswer(invocation -> {
      Supplier<?> operation = invocation.getArgument(OPERATION_ARGUMENT_INDEX);
      return operation.get();
    }).when(idempotencyService).execute(any(), any(), any(), any());

    String body = objectMapper.writeValueAsString(Instancio.create(QuestionRequest.class));

    mockMvc.perform(post(QUESTION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath(JSON_PATH_TEXT).value(TEXT))
        .andExpect(header().string("Location",
            QUESTION + PATH_SEPARATOR + ID));

    verify(questionService).save(any(Question.class));
  }

  @Test
  @DisplayName("Given question with blank text when creating question then return bad request")
  void givenQuestionWithBlankText_whenCreatingQuestion_thenReturnBadRequest() throws Exception {
    QuestionRequest request = Instancio.of(QuestionRequest.class)
        .set(field(QuestionRequest::text), null)
        .create();

    String body = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(QUESTION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(MESSAGE));
  }

  @Test
  @DisplayName("Given existing question when updating question then return updated question")
  void givenExistingQuestion_whenUpdatingQuestion_thenReturnUpdatedQuestion() throws Exception {
    Question updated = createQuestion();
    when(questionService.update(any(Question.class))).thenReturn(updated);

    String body = objectMapper.writeValueAsString(Instancio.create(QuestionRequest.class));

    mockMvc.perform(put(QUESTION + PATH_SEPARATOR + ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_QUESTION_TYPE).value(QUESTION_TYPE.name()));

    verify(questionService).update(any(Question.class));
  }

  private Question createQuestion() {
    return Instancio.of(Question.class)
        .set(field(Question::getId), ID)
        .set(field(Question::getText), TEXT)
        .set(field(Question::getQuestionType), QUESTION_TYPE)
        .set(field(Question::getQuestionAnswerOptions), List.of())
        .create();
  }
}