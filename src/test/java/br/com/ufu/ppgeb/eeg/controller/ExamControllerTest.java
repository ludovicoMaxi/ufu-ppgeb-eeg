package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM;
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

import br.com.ufu.ppgeb.eeg.dto.ExamRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.service.ExamService;
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
class ExamControllerTest {

  private static final Long EXAM_ID = 1001L;
  private static final String BED = "BED_A";
  private static final String JSON_PATH_BED = "$.bed";
  private static final String JSON_PATH_LENGTH = "$.length()";

  @Mock
  private ExamService examService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new ExamController(examService))
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given exams in service when listing exams then return exams")
  void givenExamsInService_whenListingExams_thenReturnExams() throws Exception {
    Exam exam = createExam();
    when(examService.findByFilter(EXAM_ID, null, null, null)).thenReturn(List.of(exam));

    mockMvc.perform(get(EXAM).param("id", String.valueOf(EXAM_ID)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath("$[0].bed").value(BED));

    verify(examService).findByFilter(EXAM_ID, null, null, null);
  }

  @Test
  @DisplayName("Given existing exam ID when finding exam then return exam")
  void givenExistingExamId_whenFindingExam_thenReturnExam() throws Exception {
    Exam exam = createExam();
    when(examService.findById(EXAM_ID)).thenReturn(exam);

    mockMvc.perform(get(EXAM + PATH_SEPARATOR + EXAM_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_BED).value(BED));

    verify(examService).findById(EXAM_ID);
  }

  @Test
  @DisplayName("Given unknown exam ID when finding exam then return not found")
  void givenUnknownExamId_whenFindingExam_thenReturnNotFound() throws Exception {
    when(examService.findById(EXAM_ID))
        .thenThrow(new ResourceNotFoundException("Exame", EXAM_ID));

    mockMvc.perform(get(EXAM + PATH_SEPARATOR + EXAM_ID))
        .andExpect(status().isNotFound());

    verify(examService).findById(EXAM_ID);
  }

  @Test
  @DisplayName("Given an exam when saving exam then return created exam")
  void givenExam_whenSavingExam_thenReturnCreatedExam() throws Exception {
    Exam exam = createExam();
    when(examService.save(any(Exam.class))).thenReturn(exam);

    String body = objectMapper.writeValueAsString(Instancio.create(ExamRequest.class));

    mockMvc.perform(post(EXAM)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(JSON_PATH_BED).value(BED));

    verify(examService).save(any(Exam.class));
  }

  @Test
  @DisplayName("Given an exam when updating exam then return updated exam")
  void givenExam_whenUpdatingExam_thenReturnUpdatedExam() throws Exception {
    Exam exam = createExam();
    when(examService.update(any(Exam.class))).thenReturn(exam);

    String body = objectMapper.writeValueAsString(Instancio.create(ExamRequest.class));

    mockMvc.perform(put(EXAM + PATH_SEPARATOR + EXAM_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_BED).value(BED));

    verify(examService).update(any(Exam.class));
  }

  private Exam createExam() {
    return Instancio.of(Exam.class)
        .set(field(Exam::getId), EXAM_ID)
        .set(field(Exam::getBed), BED)
        .ignore(field(Exam::getExamMedicaments))
        .ignore(field(Exam::getExamEquipments))
        .create();
  }
}
