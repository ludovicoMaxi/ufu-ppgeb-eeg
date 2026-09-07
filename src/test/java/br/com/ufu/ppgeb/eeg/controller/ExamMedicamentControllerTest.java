package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.MEDICAMENTS_SUBPATH;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static org.instancio.Select.field;
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

import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentRequest;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.service.ExamMedicamentService;
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
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ExamMedicamentControllerTest {

  private static final Long EXAM_ID = 1001L;
  private static final String URL = EXAM + PATH_SEPARATOR + EXAM_ID + MEDICAMENTS_SUBPATH;
  private static final String JSON_PATH_LENGTH = "$.length()";
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

  @Mock
  private ExamMedicamentService examMedicamentService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(
        new ExamMedicamentController(examMedicamentService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Given exam ID when listing medicaments then return medicaments")
  void givenExamId_whenListingMedicaments_thenReturnMedicaments() throws Exception {
    ExamMedicament em = Instancio.of(ExamMedicament.class)
        .set(field(ExamMedicament::getId), 1L)
        .create();
    when(examMedicamentService.findByExamId(eq(EXAM_ID), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(em), PAGE_REQUEST, 1));

    mockMvc.perform(get(URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));

    verify(examMedicamentService).findByExamId(eq(EXAM_ID), any(Pageable.class));
  }

  @Test
  @DisplayName("Given medicament list when updating medicaments then return updated list")
  void givenMedicamentList_whenUpdatingMedicaments_thenReturnUpdatedList() throws Exception {
    ExamMedicament em = Instancio.of(ExamMedicament.class)
        .set(field(ExamMedicament::getId), 1L)
        .create();
    when(examMedicamentService.updateList(eq(EXAM_ID), anyList())).thenReturn(List.of(em));

    ExamMedicamentRequest req = Instancio.create(ExamMedicamentRequest.class);
    String body = objectMapper.writeValueAsString(List.of(req));

    mockMvc.perform(put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1));

    verify(examMedicamentService).updateList(eq(EXAM_ID), anyList());
  }
}
