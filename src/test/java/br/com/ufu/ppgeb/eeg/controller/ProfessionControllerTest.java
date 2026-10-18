package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PROFESSION;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.model.Profession;
import br.com.ufu.ppgeb.eeg.service.ProfessionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ProfessionControllerTest {

  private static final String JSON_PATH_FIRST_NAME = "$[0].name";
  private static final String JSON_PATH_LENGTH = "$.length()";

  @Mock
  private ProfessionService professionService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new ProfessionController(professionService))
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given professions when listing professions then return them as a list")
  void givenProfessions_whenListingProfessions_thenReturnList() throws Exception {
    Profession profession = Instancio.create(Profession.class);
    when(professionService.findAll()).thenReturn(List.of(profession));

    mockMvc.perform(get(PROFESSION))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(profession.getName()));

    verify(professionService).findAll();
  }

  @Test
  @DisplayName("Given no professions when listing professions then return empty list")
  void givenNoProfessions_whenListingProfessions_thenReturnEmptyList() throws Exception {
    when(professionService.findAll()).thenReturn(List.of());

    mockMvc.perform(get(PROFESSION))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(0));

    verify(professionService).findAll();
  }
}
