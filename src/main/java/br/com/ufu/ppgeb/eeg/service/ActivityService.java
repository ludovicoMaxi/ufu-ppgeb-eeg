package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
   * Finds activities by exam id paginated.
   *
   * @param examId the exam id
   * @param pageable the pagination information
   * @return the page of activities
   */
  Page<Activity> findByExamId(Long examId, Pageable pageable);

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
   * Updates a list of activities of an exam.
   *
   * @param examId the exam id
   * @param activities the activities to update
   * @return the updated list of activities
   */
  List<Activity> updateList(Long examId, List<Activity> activities);

}
