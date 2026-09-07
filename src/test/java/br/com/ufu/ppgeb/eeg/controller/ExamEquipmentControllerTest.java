package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EQUIPMENTS_SUBPATH;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM;
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

import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentRequest;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.service.ExamEquipmentService;
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
class ExamEquipmentControllerTest {

  private static final Long EXAM_ID = 1001L;
  private static final String URL = EXAM + PATH_SEPARATOR + EXAM_ID + EQUIPMENTS_SUBPATH;
  private static final String JSON_PATH_LENGTH = "$.length()";
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

  @Mock
  private ExamEquipmentService examEquipmentService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(
        new ExamEquipmentController(examEquipmentService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Given exam ID when listing equipments then return equipments")
  void givenExamId_whenListingEquipments_thenReturnEquipments() throws Exception {
    ExamEquipment ee = Instancio.of(ExamEquipment.class)
        .set(field(ExamEquipment::getId), 1L)
        .create();
    when(examEquipmentService.findByExamId(eq(EXAM_ID), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(ee), PAGE_REQUEST, 1));

    mockMvc.perform(get(URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));

    verify(examEquipmentService).findByExamId(eq(EXAM_ID), any(Pageable.class));
  }

  @Test
  @DisplayName("Given equipment list when updating equipments then return updated list")
  void givenEquipmentList_whenUpdatingEquipments_thenReturnUpdatedList() throws Exception {
    ExamEquipment ee = Instancio.of(ExamEquipment.class)
        .set(field(ExamEquipment::getId), 1L)
        .create();
    when(examEquipmentService.updateList(eq(EXAM_ID), anyList())).thenReturn(List.of(ee));

    ExamEquipmentRequest req = Instancio.create(ExamEquipmentRequest.class);
    String body = objectMapper.writeValueAsString(List.of(req));

    mockMvc.perform(put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1));

    verify(examEquipmentService).updateList(eq(EXAM_ID), anyList());
  }
}
