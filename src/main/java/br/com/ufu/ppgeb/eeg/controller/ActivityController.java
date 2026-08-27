package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.service.ActivityService;
import br.com.ufu.ppgeb.eeg.view.ActivityList;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for activity operations.
 */
@RestController
@RequestMapping("/api/activity")
@AllArgsConstructor
public class ActivityController {

  private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

  private final ActivityService activityService;

  /**
   * Lists activities by exam id.
   *
   * @param examId the exam id
   * @return the list of activities
   */
  @GetMapping
  public List<Activity> list(@RequestParam(value = "examId") Long examId) {

    logger.info("Consultando atividades do exame id={}", examId);
    return activityService.findByExamId(examId);
  }

  /**
   * Finds an activity by id.
   *
   * @param id the activity id
   * @return the activity
   */
  @GetMapping("/{id}")
  public Activity findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando atividade id={}", id);
    return activityService.findById(id);
  }

  /**
   * Saves a new activity.
   *
   * @param activity the activity to save
   * @return the saved activity
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Activity save(@RequestBody Activity activity) {

    logger.info("Recebendo criação de atividade");
    return activityService.save(activity);
  }

  /**
   * Updates a list of activities.
   *
   * @param activityList the activity list to update
   * @return the updated activity list
   */
  @PutMapping
  public ActivityList updateList(@RequestBody ActivityList activityList) {

    logger.info("Recebendo atualização de atividades do exame id={}", activityList.getExamId());
    activityList.setActivities(activityService.updateList(activityList));
    return activityList;
  }
}
