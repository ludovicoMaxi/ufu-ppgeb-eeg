package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ActivityRequest;
import br.com.ufu.ppgeb.eeg.dto.ActivityResponse;
import br.com.ufu.ppgeb.eeg.mapper.ActivityMapper;
import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.service.ActivityService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for activities of an exam.
 */
@RestController
@RequestMapping(ApiPaths.EXAM_ACTIVITIES)
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
  public List<ActivityResponse> list(@PathVariable(value = "examId") Long examId) {

    logger.info("Consultando atividades do exame id={}", examId);
    return Optional.ofNullable(activityService.findByExamId(examId))
        .orElse(List.of())
        .stream()
        .map(ActivityMapper::toResponse)
        .toList();
  }

  /**
   * Finds an activity by id.
   *
   * @param examId the exam id
   * @param id the activity id
   * @return the activity
   */
  @GetMapping("/{id}")
  public ActivityResponse findById(
      @PathVariable(value = "examId") Long examId,
      @PathVariable(value = "id") Long id) {

    logger.info("Consultando atividade id={} do exame id={}", id, examId);
    return ActivityMapper.toResponse(activityService.findById(id));
  }

  /**
   * Saves a new activity for an exam.
   *
   * @param examId the exam id
   * @param request the activity to save
   * @return the saved activity
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ActivityResponse save(
      @PathVariable(value = "examId") Long examId,
      @Valid @RequestBody ActivityRequest request) {

    logger.info("Recebendo criação de atividade do exame id={}", examId);
    return ActivityMapper.toResponse(activityService.save(ActivityMapper.toEntity(request, examId)));
  }

  /**
   * Updates a list of activities of an exam.
   *
   * @param examId the exam id
   * @param activities the activities to update
   * @return the updated activity list
   */
  @PutMapping
  public List<ActivityResponse> updateList(
      @PathVariable(value = "examId") Long examId,
      @RequestBody List<ActivityRequest> activities) {

    logger.info("Recebendo atualização de atividades do exame id={}", examId);
    List<Activity> entities = Optional.ofNullable(activities)
        .orElse(List.of())
        .stream()
        .filter(Objects::nonNull)
        .map(request -> ActivityMapper.toEntity(request, examId))
        .toList();
    return activityService.updateList(examId, entities)
        .stream()
        .map(ActivityMapper::toResponse)
        .toList();
  }
}
