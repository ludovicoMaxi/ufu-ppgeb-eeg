package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EPOCH;
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

import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.service.EpochService;
import br.com.ufu.ppgeb.eeg.view.EpochList;
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
class EpochControllerTest {

  private static final String EXAM_ID_PARAM = "examId";
  private static final Long EXAM_ID = 1001L;
  private static final String DESCRIPTION = "Em Silencio";
  private static final String JSON_PATH_LENGTH = "$.length()";
  private static final String JSON_PATH_FIRST_DESCRIPTION = "$[0].description";
  private static final String JSON_PATH_DESCRIPTION = "$.description";

  @Mock
  private EpochService epochService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new EpochController(epochService)).build();
  }

  @Test
  @DisplayName("Given exam ID when listing epochs then return epochs")
  void givenExamId_whenListingEpochs_thenReturnEpochs() throws Exception {
    Epoch epoch = createEpoch();
    when(epochService.findByFilter(EXAM_ID)).thenReturn(List.of(epoch));

    mockMvc.perform(get(EPOCH).param(EXAM_ID_PARAM, String.valueOf(EXAM_ID)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_DESCRIPTION).value(DESCRIPTION));

    verify(epochService).findByFilter(EXAM_ID);
  }

  @Test
  @DisplayName("Given existing epoch ID when finding epoch then return epoch")
  void givenExistingEpochId_whenFindingEpoch_thenReturnEpoch() throws Exception {
    Epoch epoch = createEpoch();
    when(epochService.findById(EXAM_ID)).thenReturn(epoch);

    mockMvc.perform(get(EPOCH + "/" + EXAM_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value(DESCRIPTION));

    verify(epochService).findById(EXAM_ID);
  }

  @Test
  @DisplayName("Given an epoch when saving epoch then return created epoch")
  void givenEpoch_whenSavingEpoch_thenReturnCreatedEpoch() throws Exception {
    Epoch epoch = createEpoch();
    when(epochService.save(any(Epoch.class))).thenReturn(epoch);

    String body = objectMapper.writeValueAsString(epoch);

    mockMvc.perform(post(EPOCH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value(DESCRIPTION));

    verify(epochService).save(any(Epoch.class));
  }

  @Test
  @DisplayName("Given an epoch list when updating epochs then return updated epoch list")
  void givenEpochList_whenUpdatingEpochs_thenReturnUpdatedEpochList() throws Exception {
    Epoch epoch = createEpoch();
    EpochList epochList = Instancio.of(EpochList.class)
        .set(field(EpochList::getExamId), EXAM_ID)
        .set(field(EpochList::getEpochs), List.of(epoch))
        .create();

    when(epochService.updateList(any(EpochList.class))).thenReturn(List.of(epoch));

    String body = objectMapper.writeValueAsString(epochList);

    mockMvc.perform(put(EPOCH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.examId").value(EXAM_ID))
        .andExpect(jsonPath("$.epochs[0].description").value(DESCRIPTION));

    verify(epochService).updateList(any(EpochList.class));
  }

  private Epoch createEpoch() {
    return Instancio.of(Epoch.class)
        .set(field(Epoch::getId), EXAM_ID)
        .set(field(Epoch::getExamId), EXAM_ID)
        .set(field(Epoch::getDescription), DESCRIPTION)
        .create();
  }
}
