package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.ACTIVITIES_SUBPATH;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.EXAM;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.function.Supplier;

import br.com.ufu.ppgeb.eeg.dto.ActivityRequest;
import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.service.ActivityService;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
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
class ActivityControllerTest {

  private static final Long EXAM_ID = 1001L;
  private static final String ACTIVITY_URL = EXAM + PATH_SEPARATOR + EXAM_ID + ACTIVITIES_SUBPATH;
  private static final String DESCRIPTION = "Repouso";
  private static final String JSON_PATH_LENGTH = "$.content.length()";
  private static final String JSON_PATH_FIRST_DESCRIPTION = "$[0].description";
  private static final String JSON_PATH_DESCRIPTION = "$.description";
  private static final int OPERATION_ARGUMENT_INDEX = 3;
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

  @Mock
  private ActivityService activityService;

  @Mock
  private IdempotencyService idempotencyService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(
        new ActivityController(activityService, idempotencyService))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Given exam ID when listing activities then return activities")
  void givenExamId_whenListingActivities_thenReturnActivities() throws Exception {
    Activity activity = createActivity();
    when(activityService.findByExamId(eq(EXAM_ID), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(activity), PAGE_REQUEST, 1));

    mockMvc.perform(get(ACTIVITY_URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath("$.content[0].description").value(DESCRIPTION));

    verify(activityService).findByExamId(eq(EXAM_ID), any(Pageable.class));
  }

  @Test
  @DisplayName("Given existing activity ID when finding activity then return activity")
  void givenExistingActivityId_whenFindingActivity_thenReturnActivity() throws Exception {
    Activity activity = createActivity();
    when(activityService.findById(EXAM_ID)).thenReturn(activity);

    mockMvc.perform(get(ACTIVITY_URL + PATH_SEPARATOR + EXAM_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value(DESCRIPTION));

    verify(activityService).findById(EXAM_ID);
  }

  @Test
  @DisplayName("Given an activity when saving activity then return created activity")
  void givenActivity_whenSavingActivity_thenReturnCreatedActivity() throws Exception {
    Activity activity = createActivity();
    when(activityService.save(any(Activity.class))).thenReturn(activity);

    doAnswer(invocation -> {
      Supplier<?> operation = invocation.getArgument(OPERATION_ARGUMENT_INDEX);
      return operation.get();
    }).when(idempotencyService).execute(any(), any(), any(), any());

    String body = objectMapper.writeValueAsString(Instancio.create(ActivityRequest.class));

    mockMvc.perform(post(ACTIVITY_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            ACTIVITY_URL + PATH_SEPARATOR + EXAM_ID))
        .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value(DESCRIPTION));

    verify(activityService).save(any(Activity.class));
  }

  @Test
  @DisplayName("Given an activity list when updating activities then return updated activity list")
  void givenActivityList_whenUpdatingActivities_thenReturnUpdatedActivityList() throws Exception {
    ActivityRequest activityRequest = Instancio.of(ActivityRequest.class)
        .set(field(ActivityRequest::id), EXAM_ID)
        .set(field(ActivityRequest::description), DESCRIPTION)
        .create();

    when(activityService.updateList(eq(EXAM_ID), anyList()))
        .thenReturn(List.of(createActivity()));

    String body = objectMapper.writeValueAsString(List.of(activityRequest));

    mockMvc.perform(put(ACTIVITY_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_FIRST_DESCRIPTION).value(DESCRIPTION));

    verify(activityService).updateList(eq(EXAM_ID), anyList());
  }

  private Activity createActivity() {
    return Instancio.of(Activity.class)
        .set(field(Activity::getId), EXAM_ID)
        .set(field(Activity::getExamId), EXAM_ID)
        .set(field(Activity::getDescription), DESCRIPTION)
        .create();
  }
}
