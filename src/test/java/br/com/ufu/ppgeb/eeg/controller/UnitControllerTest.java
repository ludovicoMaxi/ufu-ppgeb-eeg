package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.UNIT;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Unit;
import br.com.ufu.ppgeb.eeg.service.UnitService;
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
class UnitControllerTest {

  private static final String FIRST_UNIT_NAME = "mg";
  private static final String JSON_PATH_LENGTH = "$.content.length()";
  private static final String JSON_PATH_FIRST_NAME = "$.content[0].name";
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);
  private static final int TWO_UNITS = 2;

  @Mock
  private UnitService unitService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new UnitController(unitService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Given units in service when finding all units then return units")
  void givenUnitsInService_whenFindingAllUnits_thenReturnUnits() throws Exception {
    Unit first = Instancio.of(Unit.class)
        .set(field(Unit::getName), FIRST_UNIT_NAME)
        .create();
    Unit second = Instancio.create(Unit.class);

    when(unitService.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(first, second), PAGE_REQUEST, TWO_UNITS));

    mockMvc.perform(get(UNIT))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(TWO_UNITS))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_UNIT_NAME));

    verify(unitService).findAll(any(Pageable.class));
  }

  @Test
  @DisplayName("Given empty list when finding all units then no units are returned")
  void givenEmptyList_whenFindingAllUnits_thenReturnEmptyList() throws Exception {
    when(unitService.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(), PAGE_REQUEST, 0));

    mockMvc.perform(get(UNIT))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(0));

    verify(unitService).findAll(any(Pageable.class));
  }
}
