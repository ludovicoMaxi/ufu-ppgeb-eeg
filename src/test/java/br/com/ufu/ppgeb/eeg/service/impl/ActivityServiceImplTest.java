package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.repository.ActivityRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

  private static final String MSG_ACTIVITY_NULL = "activity cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_START_TIME_NULL = "start time cannot be null.";
  private static final String MSG_DURATION_NULL = "duration cannot be null.";
  private static final String MSG_DESCRIPTION_EMPTY = "description cannot be empty.";
  private static final String MSG_EXAM_ID_NULL = "examId cannot be null.";
  private static final String RESOURCE_NAME = "Activity";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final Long ACTIVITY_ID = 1L;
  private static final Long EXAM_ID = 10L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long DIFFERENT_EXAM_ID = 999L;
  private static final int TWO_ACTIVITIES = 2;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Mock
  private ActivityRepository activityRepository;

  @InjectMocks
  private ActivityServiceImpl activityService;

  @Test
  @DisplayName("Given two activities in database when findAll then return all activities")
  void givenTwoActivitiesInDatabase_whenFindAll_thenReturnAllActivities() {
    Activity activity1 = Instancio.create(Activity.class);
    Activity activity2 = Instancio.create(Activity.class);

    List<Activity> activities = List.of(activity1, activity2);
    when(activityRepository.findAll()).thenReturn(activities);

    List<Activity> result = activityService.findAll();

    assertThat(result).hasSize(TWO_ACTIVITIES);
    verify(activityRepository).findAll();
  }

  @Test
  @DisplayName("Given no activities in database when findAll then return empty list")
  void givenNoActivitiesInDatabase_whenFindAll_thenReturnEmptyList() {
    when(activityRepository.findAll()).thenReturn(Collections.emptyList());

    List<Activity> result = activityService.findAll();

    assertThat(result).isEmpty();
    verify(activityRepository).findAll();
  }

  @Test
  @DisplayName("Given valid activity when save then return saved activity")
  void givenValidActivity_whenSave_thenReturnSavedActivity() {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(null);

    when(activityRepository.save(any(Activity.class)))
        .thenReturn(activity);

    activityService.save(activity);

    verify(activityRepository).save(activity);
  }

  @Test
  @DisplayName("Given activity with non-null id when save then throw exception")
  void givenActivityWithNonNullId_whenSave_thenThrowException() {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(ACTIVITY_ID);

    assertThatThrownBy(() -> activityService.save(activity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("id must be null");

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given null activity when save then throw exception")
  void givenNullActivity_whenSave_thenThrowException() {
    assertThatThrownBy(() -> activityService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ACTIVITY_NULL);

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given activity with null start time when save then throw exception")
  void givenActivityWithNullStartTime_whenSave_thenThrowException() {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(null);
    activity.setStartTime(null);

    assertThatThrownBy(() -> activityService.save(activity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_START_TIME_NULL);

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given activity with null duration when save then throw exception")
  void givenActivityWithNullDuration_whenSave_thenThrowException() {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(null);
    activity.setDuration(null);

    assertThatThrownBy(() -> activityService.save(activity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DURATION_NULL);

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given activity with empty description when save then throw exception")
  void givenActivityWithEmptyDescription_whenSave_thenThrowException() {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(null);
    activity.setDescription("");

    assertThatThrownBy(() -> activityService.save(activity))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DESCRIPTION_EMPTY);

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when findById then return activity")
  void givenExistingId_whenFindById_thenReturnActivity() {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(ACTIVITY_ID);

    when(activityRepository.findById(ACTIVITY_ID)).thenReturn(Optional.of(activity));

    Activity result = activityService.findById(ACTIVITY_ID);

    assertThat(result.getId()).isEqualTo(ACTIVITY_ID);
    verify(activityRepository).findById(ACTIVITY_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {
    assertThatThrownBy(() -> activityService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(activityRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(activityRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> activityService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid exam id when findByExamId then return matching activities")
  void givenValidExamId_whenFindByExamId_thenReturnMatchingActivities() {
    Activity activity = Instancio.create(Activity.class);

    when(activityRepository.findByExamId(EXAM_ID, PAGEABLE))
        .thenReturn(new PageImpl<>(List.of(activity)));

    Page<Activity> result = activityService.findByExamId(EXAM_ID, PAGEABLE);

    assertThat(result.getContent()).hasSize(1);
    verify(activityRepository).findByExamId(EXAM_ID, PAGEABLE);
  }

  @Test
  @DisplayName("Given null exam id when findByExamId then throw exception")
  void givenNullExamId_whenFindByExamId_thenThrowException() {
    assertThatThrownBy(() -> activityService.findByExamId(null, PAGEABLE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_ID_NULL);

    verify(activityRepository, never()).findByExamId(any(), any());
  }

  @Test
  @DisplayName("Given existing id when delete then remove activity")
  void givenExistingId_whenDelete_thenRemoveActivity() {
    when(activityRepository.existsById(ACTIVITY_ID)).thenReturn(true);

    activityService.delete(ACTIVITY_ID);

    verify(activityRepository).existsById(ACTIVITY_ID);
    verify(activityRepository).deleteById(ACTIVITY_ID);
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {
    assertThatThrownBy(() -> activityService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(activityRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when delete then throw exception")
  void givenNonexistentId_whenDelete_thenThrowException() {
    when(activityRepository.existsById(NONEXISTENT_ID)).thenReturn(false);

    assertThatThrownBy(() -> activityService.delete(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(activityRepository, never()).deleteById(NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given null exam id when updateList then throw exception")
  void givenNullExamId_whenUpdateList_thenThrowException() {
    assertThatThrownBy(() -> activityService.updateList(null, List.of(createActivity(null, EXAM_ID))))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ExamId cannot be null.");

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given null activities when updateList then return empty list")
  void givenNullActivities_whenUpdateList_thenReturnEmpty() {
    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());

    List<Activity> result = activityService.updateList(EXAM_ID, null);

    assertThat(result).isEmpty();
    verify(activityRepository).findByExamId(EXAM_ID);
    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given new activities when updateList then return saved activities")
  void givenNewActivities_whenUpdateList_thenReturnSavedActivities() {
    Activity activity = createActivity(null, EXAM_ID);

    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());
    when(activityRepository.save(any(Activity.class)))
        .thenReturn(activity);

    List<Activity> result = activityService.updateList(EXAM_ID, List.of(activity));

    assertThat(result).hasSize(1);
    verify(activityRepository).findByExamId(EXAM_ID);
    verify(activityRepository).save(any(Activity.class));
  }

  private Activity createActivity(Long id, Long examId) {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(id);
    activity.setExamId(examId);
    return activity;
  }

  @Test
  @DisplayName("Given activity with different exam id in list when updateList then throw exception")
  void givenActivityWithDifferentExamIdInList_whenUpdateList_thenThrowException() {
    Activity activity = createActivity(null, DIFFERENT_EXAM_ID);

    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());

    assertThatThrownBy(() -> activityService.updateList(EXAM_ID, List.of(activity)))
        .isInstanceOf(IllegalArgumentException.class);

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given activity with nonexistent id in list when updateList then throw exception")
  void givenActivityWithNonexistentIdInList_whenUpdateList_thenThrowException() {
    Activity activity = createActivity(NONEXISTENT_ID, EXAM_ID);

    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());

    assertThatThrownBy(() -> activityService.updateList(EXAM_ID, List.of(activity)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Activity with id=" + NONEXISTENT_ID + " not exist by examID=" + EXAM_ID);

    verify(activityRepository, never()).save(any());
  }
}
