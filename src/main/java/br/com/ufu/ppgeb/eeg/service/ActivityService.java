package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.view.ActivityList;

/**
 * Service interface for Activity operations.
 */
public interface ActivityService {

  /**
   * Saves an activity.
   *
   * @param activity the activity to save
   * @return the saved activity
   */
  Activity save(Activity activity);

  /**
   * Finds an activity by id.
   *
   * @param id the activity id
   * @return the activity
   */
  Activity findById(Long id);

  /**
   * Finds activities by exam id.
   *
   * @param examId the exam id
   * @return the list of activities
   */
  List<Activity> findByExamId(Long examId);

  /**
   * Finds all activities.
   *
   * @return the list of activities
   */
  List<Activity> findAll();

  /**
   * Deletes an activity by id.
   *
   * @param id the activity id
   */
  void delete(Long id);

  /**
   * Updates a list of activities.
   *
   * @param activityList the activity list
   * @return the updated list
   */
  List<Activity> updateList(ActivityList activityList);

}
