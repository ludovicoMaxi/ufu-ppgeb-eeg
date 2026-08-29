package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM_REQUEST;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.ExamRequestRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.service.ExamRequestService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ExamRequestControllerTest {

  private static final Long ID = 1001L;
  private static final String SECTOR = "NEUROLOGIA";
  private static final String JSON_PATH_SECTOR = "$.sector";
  private static final String JSON_PATH_LENGTH = "$.length()";

  @Mock
  private ExamRequestService examRequestService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new ExamRequestController(examRequestService))
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given exam requests in service when listing exam requests then return exam requests")
  void givenExamRequestsInService_whenListingExamRequests_thenReturnExamRequests()
      throws Exception {
    ExamRequest examRequest = createExamRequest();
    when(examRequestService.findByFilter(ID, null, null, null))
        .thenReturn(List.of(examRequest));

    mockMvc.perform(get(EXAM_REQUEST).param("medicalRecord", String.valueOf(ID)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath("$[0].sector").value(SECTOR));

    verify(examRequestService).findByFilter(ID, null, null, null);
  }

  @Test
  @DisplayName("Given existing exam request ID when finding exam request then return exam request")
  void givenExistingExamRequestId_whenFindingExamRequest_thenReturnExamRequest()
      throws Exception {
    ExamRequest examRequest = createExamRequest();
    when(examRequestService.findById(ID)).thenReturn(examRequest);

    mockMvc.perform(get(EXAM_REQUEST + PATH_SEPARATOR + ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_SECTOR).value(SECTOR));

    verify(examRequestService).findById(ID);
  }

  @Test
  @DisplayName("Given unknown exam request ID when finding exam request then return not found")
  void givenUnknownExamRequestId_whenFindingExamRequest_thenReturnNotFound() throws Exception {
    when(examRequestService.findById(ID))
        .thenThrow(new ResourceNotFoundException("Solicitação de exame", ID));

    mockMvc.perform(get(EXAM_REQUEST + PATH_SEPARATOR + ID))
        .andExpect(status().isNotFound());

    verify(examRequestService).findById(ID);
  }

  @Test
  @DisplayName("Given an exam request when saving exam request then return created exam request")
  void givenExamRequest_whenSavingExamRequest_thenReturnCreatedExamRequest() throws Exception {
    ExamRequest examRequest = createExamRequest();
    when(examRequestService.save(any(ExamRequest.class))).thenReturn(examRequest);

    String body = objectMapper.writeValueAsString(Instancio.create(ExamRequestRequest.class));

    mockMvc.perform(post(EXAM_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(JSON_PATH_SECTOR).value(SECTOR));

    verify(examRequestService).save(any(ExamRequest.class));
  }

  @Test
  @DisplayName("Given an exam request when updating exam request then return updated exam request")
  void givenExamRequest_whenUpdatingExamRequest_thenReturnUpdatedExamRequest() throws Exception {
    ExamRequest examRequest = createExamRequest();
    when(examRequestService.update(any(ExamRequest.class))).thenReturn(examRequest);

    String body = objectMapper.writeValueAsString(Instancio.create(ExamRequestRequest.class));

    mockMvc.perform(put(EXAM_REQUEST + PATH_SEPARATOR + ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_SECTOR).value(SECTOR));

    verify(examRequestService).update(any(ExamRequest.class));
  }

  private ExamRequest createExamRequest() {
    return Instancio.of(ExamRequest.class)
        .set(field(ExamRequest::getId), ID)
        .set(field(ExamRequest::getSector), SECTOR)
        .ignore(field(ExamRequest::getPatient))
        .create();
  }
}
