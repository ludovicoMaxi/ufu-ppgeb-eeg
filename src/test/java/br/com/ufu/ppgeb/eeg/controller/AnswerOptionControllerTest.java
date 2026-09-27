package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.ANSWER_OPTION;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
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

import br.com.ufu.ppgeb.eeg.dto.AnswerOptionRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.service.AnswerOptionService;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
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
class AnswerOptionControllerTest {

  private static final Long ID = 1001L;
  private static final String TEXT = "Lúcido";
  private static final String JSON_PATH_TEXT = "$.text";
  private static final String MESSAGE = "text must not be blank";
  private static final int OPERATION_ARGUMENT_INDEX = 3;
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

  @Mock
  private AnswerOptionService answerOptionService;

  @Mock
  private IdempotencyService idempotencyService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
    mockMvc = MockMvcBuilders.standaloneSetup(
        new AnswerOptionController(answerOptionService, idempotencyService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setValidator(validator)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given existing answer option ID when finding answer option then return answer option")
  void givenExistingAnswerOptionId_whenFindingAnswerOption_thenReturnAnswerOption() throws Exception {
    AnswerOption answerOption = createAnswerOption();
    when(answerOptionService.findById(ID)).thenReturn(answerOption);

    mockMvc.perform(get(ANSWER_OPTION + PATH_SEPARATOR + ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_TEXT).value(TEXT));

    verify(answerOptionService).findById(ID);
  }

  @Test
  @DisplayName("Given unknown answer option ID when finding answer option then return not found")
  void givenUnknownAnswerOptionId_whenFindingAnswerOption_thenReturnNotFound() throws Exception {
    when(answerOptionService.findById(ID))
        .thenThrow(new ResourceNotFoundException("AnswerOption", ID));

    mockMvc.perform(get(ANSWER_OPTION + PATH_SEPARATOR + ID))
        .andExpect(status().isNotFound());

    verify(answerOptionService).findById(ID);
  }

  @Test
  @DisplayName("Given answer options when listing answer options then return answer options")
  void givenAnswerOptions_whenListingAnswerOptions_thenReturnAnswerOptions() throws Exception {
    AnswerOption answerOption = createAnswerOption();
    when(answerOptionService.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(answerOption), PAGE_REQUEST, 1));

    mockMvc.perform(get(ANSWER_OPTION))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));

    verify(answerOptionService).findAll(any(Pageable.class));
  }

  @Test
  @DisplayName("Given valid answer option when creating answer option then return created with location")
  void givenValidAnswerOption_whenCreatingAnswerOption_thenReturnCreated() throws Exception {
    AnswerOption saved = createAnswerOption();
    when(answerOptionService.save(any(AnswerOption.class))).thenReturn(saved);

    doAnswer(invocation -> {
      Supplier<?> operation = invocation.getArgument(OPERATION_ARGUMENT_INDEX);
      return operation.get();
    }).when(idempotencyService).execute(any(), any(), any(), any());

    String body = objectMapper
        .writeValueAsString(Instancio.create(AnswerOptionRequest.class));

    mockMvc.perform(post(ANSWER_OPTION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath(JSON_PATH_TEXT).value(TEXT))
        .andExpect(header().string("Location", ANSWER_OPTION + PATH_SEPARATOR + ID));

    verify(answerOptionService).save(any(AnswerOption.class));
  }

  @Test
  @DisplayName("Given answer option with blank text when creating answer option then return bad request")
  void givenAnswerOptionWithBlankText_whenCreatingAnswerOption_thenReturnBadRequest()
      throws Exception {
    AnswerOptionRequest request = Instancio.of(AnswerOptionRequest.class)
        .set(field(AnswerOptionRequest::text), null)
        .create();

    String body = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(ANSWER_OPTION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(MESSAGE));
  }

  @Test
  @DisplayName("Given existing answer option when updating answer option then return updated answer option")
  void givenExistingAnswerOption_whenUpdatingAnswerOption_thenReturnUpdatedAnswerOption()
      throws Exception {
    AnswerOption updated = createAnswerOption();
    when(answerOptionService.update(any(AnswerOption.class))).thenReturn(updated);

    String body = objectMapper
        .writeValueAsString(Instancio.create(AnswerOptionRequest.class));

    mockMvc.perform(put(ANSWER_OPTION + PATH_SEPARATOR + ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_TEXT).value(TEXT));

    verify(answerOptionService).update(any(AnswerOption.class));
  }

  private AnswerOption createAnswerOption() {
    return Instancio.of(AnswerOption.class)
        .set(field(AnswerOption::getId), ID)
        .set(field(AnswerOption::getText), TEXT)
        .create();
  }
}