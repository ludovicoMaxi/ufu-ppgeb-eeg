package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EPOCHS_SUBPATH;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.EpochRequest;
import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.service.EpochService;
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

  private static final Long EXAM_ID = 1001L;
  private static final String EPOCH_URL = EXAM + PATH_SEPARATOR + EXAM_ID + EPOCHS_SUBPATH;
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

    mockMvc.perform(get(EPOCH_URL))
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

    mockMvc.perform(get(EPOCH_URL + PATH_SEPARATOR + EXAM_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value(DESCRIPTION));

    verify(epochService).findById(EXAM_ID);
  }

  @Test
  @DisplayName("Given an epoch when saving epoch then return created epoch")
  void givenEpoch_whenSavingEpoch_thenReturnCreatedEpoch() throws Exception {
    Epoch epoch = createEpoch();
    when(epochService.save(any(Epoch.class))).thenReturn(epoch);

    String body = objectMapper.writeValueAsString(Instancio.create(EpochRequest.class));

    mockMvc.perform(post(EPOCH_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            EPOCH_URL + PATH_SEPARATOR + EXAM_ID))
        .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value(DESCRIPTION));

    verify(epochService).save(any(Epoch.class));
  }

  @Test
  @DisplayName("Given an epoch list when updating epochs then return updated epoch list")
  void givenEpochList_whenUpdatingEpochs_thenReturnUpdatedEpochList() throws Exception {
    EpochRequest epochRequest = Instancio.of(EpochRequest.class)
        .set(field(EpochRequest::id), EXAM_ID)
        .set(field(EpochRequest::description), DESCRIPTION)
        .create();

    when(epochService.updateList(eq(EXAM_ID), anyList()))
        .thenReturn(List.of(createEpoch()));

    String body = objectMapper.writeValueAsString(List.of(epochRequest));

    mockMvc.perform(put(EPOCH_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_FIRST_DESCRIPTION).value(DESCRIPTION));

    verify(epochService).updateList(eq(EXAM_ID), anyList());
  }

  private Epoch createEpoch() {
    return Instancio.of(Epoch.class)
        .set(field(Epoch::getId), EXAM_ID)
        .set(field(Epoch::getExamId), EXAM_ID)
        .set(field(Epoch::getDescription), DESCRIPTION)
        .create();
  }
}
