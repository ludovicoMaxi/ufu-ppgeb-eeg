package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.repository.ActivityRepository;
import br.com.ufu.ppgeb.eeg.service.ActivityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of ActivityService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {

  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "Activity";

  private final ActivityRepository activityRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Activity save(Activity activity) {

    Assert.notNull(activity, "activity cannot be null.");
    Assert.isNull(activity.getId(), "id must be null");

    validateActivity(activity);

    Activity saved = activityRepository.save(activity);
    log.info("Atividade criada com id={}", saved.getId());
    return saved;
  }

  private void validateActivity(Activity activity) {

    Assert.notNull(activity, "Activity cannot be null.");
    Assert.notNull(activity.getStartTime(), "start time cannot be null.");
    Assert.notNull(activity.getDuration(), "duration cannot be null.");
    Assert.hasText(activity.getDescription(), "description cannot be empty.");
  }

  @Override
  @Transactional(readOnly = true)
  public List<Activity> findAll() {

    return activityRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Activity findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return activityRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Activity> findByExamId(Long examId, Pageable pageable) {

    Assert.notNull(examId, "examId cannot be null.");

    return activityRepository.findByExamId(examId, pageable);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    if (!activityRepository.existsById(id)) {
      throw new ResourceNotFoundException(RESOURCE_NAME, id);
    }
    activityRepository.deleteById(id);
    log.info("Atividade removida com id={}", id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<Activity> updateList(Long examId, List<Activity> currentActivities) {

    Assert.notNull(examId, "ExamId cannot be null.");

    Map<Long, Activity> oldActivitiesById = new HashMap<>();
    for (Activity oldActivity : activityRepository.findByExamId(examId)) {
      oldActivitiesById.put(oldActivity.getId(), oldActivity);
    }

    List<Activity> savedActivities = new ArrayList<>();

    if (isNotEmpty(currentActivities)) {

      for (Activity activity : currentActivities) {

        if (nonNull(activity.getExamId())
            && !activity.getExamId()
                .equals(examId)) {
          throw new IllegalArgumentException(
              activity + " is not same examId in update=" + examId);
        }

        activity.setExamId(examId);

        if (nonNull(activity.getId())) {

          Activity oldActivity = oldActivitiesById.remove(activity.getId());
          if (isNull(oldActivity)) {
            throw new IllegalArgumentException("Activity with id=" + activity.getId()
                + " not exist by examID=" + examId);
          }

          if (!activity.equals(oldActivity)) {
            validateActivity(activity);
            activity = activityRepository.save(activity);
          }
        } else {
          validateActivity(activity);
          activity = activityRepository.save(activity);
        }

        savedActivities.add(activity);
      }
    }

    activityRepository.deleteAll(oldActivitiesById.values());

    log.info("Atividades do exame atualizadas; examId={}, quantidade={}",
        examId, savedActivities.size());
    return savedActivities;
  }
}
