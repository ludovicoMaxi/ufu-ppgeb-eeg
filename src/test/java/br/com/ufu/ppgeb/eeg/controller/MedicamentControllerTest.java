package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.MEDICAMENT;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MedicamentControllerTest {

  private static final String FIRST_MEDICAMENT_NAME = "DIPIRONA";
  private static final String JSON_PATH_LENGTH = "$.content.length()";
  private static final String JSON_PATH_FIRST_NAME = "$.content[0].name";
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);
  private static final int TWO_MEDICAMENTS = 2;

  @Mock
  private MedicamentService medicamentService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new MedicamentController(medicamentService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Given medicaments in service when finding all medicaments then return medicaments")
  void givenMedicamentsInService_whenFindingAllMedicaments_thenReturnMedicaments()
      throws Exception {
    Medicament first = Instancio.of(Medicament.class)
        .set(field(Medicament::getName), FIRST_MEDICAMENT_NAME)
        .create();
    Medicament second = Instancio.create(Medicament.class);

    when(medicamentService.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(first, second), PAGE_REQUEST, TWO_MEDICAMENTS));

    mockMvc.perform(get(MEDICAMENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(TWO_MEDICAMENTS))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_MEDICAMENT_NAME));

    verify(medicamentService).findAll(any(Pageable.class));
  }

  @Test
  @DisplayName("Given empty list when finding all medicaments then no medicaments are returned")
  void givenEmptyList_whenFindingAllMedicaments_thenReturnEmptyList() throws Exception {
    when(medicamentService.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(), PAGE_REQUEST, 0));

    mockMvc.perform(get(MEDICAMENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(0));

    verify(medicamentService).findAll(any(Pageable.class));
  }
}
