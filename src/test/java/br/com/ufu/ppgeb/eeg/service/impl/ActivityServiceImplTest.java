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
import br.com.ufu.ppgeb.eeg.view.ActivityList;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

  private static final String MSG_ACTIVITY_NULL = "activity cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_START_TIME_NULL = "start time cannot be null.";
  private static final String MSG_DURATION_NULL = "duration cannot be null.";
  private static final String MSG_DESCRIPTION_EMPTY = "description cannot be empty.";
  private static final String MSG_EXAM_ID_NULL = "examId cannot be null.";
  private static final String MSG_ACTIVITY_LIST_NULL = "ActivityList cannot be null.";
  private static final String RESOURCE_NAME = "Activity";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final Long ACTIVITY_ID = 1L;
  private static final Long EXAM_ID = 10L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long DIFFERENT_EXAM_ID = 999L;
  private static final int TWO_ACTIVITIES = 2;

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

    when(activityRepository.save(any(Activity.class)))
        .thenReturn(activity);

    activityService.save(activity);

    verify(activityRepository).save(activity);
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

    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(List.of(activity));

    List<Activity> result = activityService.findByExamId(EXAM_ID);

    assertThat(result).hasSize(1);
    verify(activityRepository).findByExamId(EXAM_ID);
  }

  @Test
  @DisplayName("Given null exam id when findByExamId then throw exception")
  void givenNullExamId_whenFindByExamId_thenThrowException() {
    assertThatThrownBy(() -> activityService.findByExamId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_ID_NULL);

    verify(activityRepository, never()).findByExamId(any());
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
  @DisplayName("Given null activity list when updateList then throw exception")
  void givenNullActivityList_whenUpdateList_thenThrowException() {
    assertThatThrownBy(() -> activityService.updateList(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ACTIVITY_LIST_NULL);

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given activity list with null exam id when updateList then throw exception")
  void givenActivityListWithNullExamId_whenUpdateList_thenThrowException() {
    ActivityList activityList = new ActivityList();
    activityList.setExamId(null);

    assertThatThrownBy(() -> activityService.updateList(activityList))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ExamId cannot be null.");

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given activity list with new activities when updateList then return saved activities")
  void givenActivityListWithNewActivities_whenUpdateList_thenReturnSavedActivities() {
    Activity activity = createActivity(null, EXAM_ID);
    ActivityList activityList = createActivityList(EXAM_ID, activity);

    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());
    when(activityRepository.save(any(Activity.class)))
        .thenReturn(activity);

    List<Activity> result = activityService.updateList(activityList);

    assertThat(result).hasSize(1);
    verify(activityRepository).findByExamId(EXAM_ID);
    verify(activityRepository).save(activity);
  }

  private Activity createActivity(Long id, Long examId) {
    Activity activity = Instancio.create(Activity.class);
    activity.setId(id);
    activity.setExamId(examId);
    return activity;
  }

  private ActivityList createActivityList(Long examId, Activity activity) {
    ActivityList activityList = new ActivityList();
    activityList.setExamId(examId);
    activityList.setActivities(List.of(activity));
    return activityList;
  }

  @Test
  @DisplayName("Given activity with different exam id in list when updateList then throw exception")
  void givenActivityWithDifferentExamIdInList_whenUpdateList_thenThrowException() {
    Activity activity = createActivity(null, DIFFERENT_EXAM_ID);
    ActivityList activityList = createActivityList(EXAM_ID, activity);

    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());

    assertThatThrownBy(() -> activityService.updateList(activityList))
        .isInstanceOf(IllegalArgumentException.class);

    verify(activityRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given activity with nonexistent id in list when updateList then throw exception")
  void givenActivityWithNonexistentIdInList_whenUpdateList_thenThrowException() {
    Activity activity = createActivity(NONEXISTENT_ID, EXAM_ID);
    ActivityList activityList = createActivityList(EXAM_ID, activity);

    when(activityRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());

    assertThatThrownBy(() -> activityService.updateList(activityList))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Activity with id=" + NONEXISTENT_ID + " not exist by examID=" + EXAM_ID);

    verify(activityRepository, never()).save(any());
  }
}
