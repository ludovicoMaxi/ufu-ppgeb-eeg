package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ExamRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamResponse;
import br.com.ufu.ppgeb.eeg.mapper.ExamMapper;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.service.ExamService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for exam operations.
 */
@RestController
@RequestMapping(ApiPaths.EXAM)
@AllArgsConstructor
public class ExamController {

  private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

  private final ExamService examService;
  private final IdempotencyService idempotencyService;

  /**
   * Lists exams with optional filters.
   *
   * @param id the exam id filter
   * @param bed the bed filter
   * @param patientId the patient id filter
   * @param examRequestId the exam request id filter
   * @param pageable the pagination information
   * @return the page of exams
   */
  @GetMapping
  public Page<ExamResponse> list(
      @RequestParam(value = "id",
          required = false) Long id,
      @RequestParam(value = "bed",
          required = false) String bed,
      @RequestParam(value = "patientId",
          required = false) Long patientId,
      @RequestParam(value = "examRequestId",
          required = false) Long examRequestId,
      Pageable pageable) {

    logger.info("Consultando exames; id={}, bed={}, patientId={}, examRequestId={}",
        id, bed, patientId, examRequestId);
    return examService.findByFilter(id, bed, patientId, examRequestId, pageable)
        .map(ExamMapper::toResponse);
  }

  /**
   * Finds an exam by id.
   *
   * @param id the exam id
   * @return the exam
   */
  @GetMapping("/{id}")
  public ExamResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando exame id={}", id);
    return ExamMapper.toResponse(examService.findById(id));
  }

  /**
   * Saves a new exam.
   *
   * @param idempotencyKey the idempotency key
   * @param request the exam to save
   * @return the saved exam
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ExamResponse> save(
      @RequestHeader(name = IdempotencyService.IDEMPOTENCY_KEY_HEADER,
          required = false) String idempotencyKey,
      @Valid @RequestBody ExamRequest request) {

    logger.info("Recebendo criação de exame");
    return idempotencyService.execute(
        "EXAM", idempotencyKey, ExamResponse.class,
        () -> {
          Exam saved = examService.save(ExamMapper.toDomain(request));
          URI location = URI.create(
              ApiPaths.EXAM + ApiPaths.PATH_SEPARATOR + saved.getId());
          return ResponseEntity.created(location)
              .body(ExamMapper.toResponse(saved));
        });
  }

  /**
   * Updates an exam.
   *
   * @param id the exam id
   * @param request the exam to update
   * @return the updated exam
   */
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ExamResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody ExamRequest request) {

    logger.info("Recebendo atualização de exame id={}", id);
    return ExamMapper.toResponse(examService.update(ExamMapper.toDomain(request, id)));
  }
}
