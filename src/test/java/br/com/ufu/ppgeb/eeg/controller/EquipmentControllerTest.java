package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EQUIPMENT;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;
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
class EquipmentControllerTest {

  private static final String FIRST_EQUIPMENT_NAME = "BRAINVISIAN";
  private static final String JSON_PATH_LENGTH = "$.content.length()";
  private static final String JSON_PATH_FIRST_NAME = "$.content[0].name";
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);
  private static final int TWO_EQUIPMENT = 2;

  @Mock
  private EquipmentService equipmentService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new EquipmentController(equipmentService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Given equipment in service when finding all equipment then return equipment")
  void givenEquipmentInService_whenFindingAllEquipment_thenReturnEquipment() throws Exception {
    Equipment first = Instancio.of(Equipment.class)
        .set(field(Equipment::getName), FIRST_EQUIPMENT_NAME)
        .create();
    Equipment second = Instancio.create(Equipment.class);

    when(equipmentService.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(first, second), PAGE_REQUEST, TWO_EQUIPMENT));

    mockMvc.perform(get(EQUIPMENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(TWO_EQUIPMENT))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_EQUIPMENT_NAME));

    verify(equipmentService).findAll(any(Pageable.class));
  }

  @Test
  @DisplayName("Given empty list when finding all equipment then no equipment is returned")
  void givenEmptyList_whenFindingAllEquipment_thenReturnEmptyList() throws Exception {
    when(equipmentService.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(), PAGE_REQUEST, 0));

    mockMvc.perform(get(EQUIPMENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(0));

    verify(equipmentService).findAll(any(Pageable.class));
  }
}
