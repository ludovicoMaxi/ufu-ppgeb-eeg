package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ActivityRequest;
import br.com.ufu.ppgeb.eeg.dto.ActivityResponse;
import br.com.ufu.ppgeb.eeg.mapper.ActivityMapper;
import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.service.ActivityService;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
  private final IdempotencyService idempotencyService;

  /**
   * Lists activities by exam id.
   *
   * @param examId the exam id
   * @param pageable the pagination information
   * @return the page of activities
   */
  @GetMapping
  public Page<ActivityResponse> list(
      @PathVariable(value = "examId") Long examId,
      Pageable pageable) {

    logger.info("Consultando atividades do exame id={}", examId);
    return activityService.findByExamId(examId, pageable)
        .map(ActivityMapper::toResponse);
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
   * @param idempotencyKey the idempotency key
   * @param request the activity to save
   * @return the saved activity
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ActivityResponse> save(
      @PathVariable(value = "examId") Long examId,
      @RequestHeader(name = IdempotencyService.IDEMPOTENCY_KEY_HEADER,
          required = false) String idempotencyKey,
      @Valid @RequestBody ActivityRequest request) {

    logger.info("Recebendo criação de atividade do exame id={}", examId);
    return idempotencyService.execute(
        "ACTIVITY:" + examId, idempotencyKey, ActivityResponse.class,
        () -> {
          Activity saved = activityService.save(
              ActivityMapper.toDomain(request, examId));
          URI location = URI.create(ApiPaths.EXAM_ACTIVITIES
              .replace(ApiPaths.EXAM_ID_PATTERN, examId.toString())
              + ApiPaths.PATH_SEPARATOR + saved.getId());
          return ResponseEntity.created(location)
              .body(ActivityMapper.toResponse(saved));
        });
  }

  /**
   * Updates a list of activities of an exam.
   *
   * @param examId the exam id
   * @param activities the activities to update
   * @return the updated activity list
   */
  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<ActivityResponse> updateList(
      @PathVariable(value = "examId") Long examId,
      @Valid @RequestBody List<@Valid ActivityRequest> activities) {

    logger.info("Recebendo atualização de atividades do exame id={}", examId);
    List<Activity> entities = Optional.ofNullable(activities)
        .orElse(List.of())
        .stream()
        .filter(Objects::nonNull)
        .map(request -> ActivityMapper.toDomain(request, examId))
        .toList();
    return activityService.updateList(examId, entities)
        .stream()
        .map(ActivityMapper::toResponse)
        .toList();
  }
}
