package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.QuestionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionResponse;
import br.com.ufu.ppgeb.eeg.mapper.QuestionMapper;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
import br.com.ufu.ppgeb.eeg.service.QuestionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * REST controller for question operations.
 */
@RestController
@RequestMapping(ApiPaths.QUESTION)
@AllArgsConstructor
public class QuestionController {

  private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

  private final QuestionService questionService;
  private final IdempotencyService idempotencyService;

  /**
   * Lists questions with optional filters.
   *
   * @param id the question id filter
   * @param text the question text filter
   * @param questionType the question type filter
   * @param active the active flag filter
   * @param pageable the pagination information
   * @return the page of questions
   */
  @GetMapping
  public Page<QuestionResponse> list(
      @RequestParam(value = "id",
          required = false) Long id,
      @RequestParam(value = "text",
          required = false) String text,
      @RequestParam(value = "questionType",
          required = false) QuestionType questionType,
      @RequestParam(value = "active",
          required = false) Boolean active,
      Pageable pageable) {

    logger.info("Consultando questões; id={}, text={}, questionType={}, active={}",
        id, text, questionType, active);
    return questionService.findByFilter(id, text, questionType, active, pageable)
        .map(QuestionMapper::toResponse);
  }

  /**
   * Finds a question by id.
   *
   * @param id the question id
   * @return the question
   */
  @GetMapping("/{id}")
  public QuestionResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando questão id={}", id);
    return QuestionMapper.toResponse(questionService.findById(id));
  }

  /**
   * Saves a new question.
   *
   * @param idempotencyKey the idempotency key
   * @param request the question to save
   * @return the saved question
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QuestionResponse> save(
      @RequestHeader(name = IdempotencyService.IDEMPOTENCY_KEY_HEADER,
          required = false) String idempotencyKey,
      @Valid @RequestBody QuestionRequest request) {

    logger.info("Recebendo criação de questão");
    return idempotencyService.execute(
        "QUESTION", idempotencyKey, QuestionResponse.class,
        () -> {
          Question saved = questionService.save(QuestionMapper.toDomain(request));
          URI location = URI.create(ApiPaths.QUESTION + "/" + saved.getId());
          return ResponseEntity.created(location)
              .body(QuestionMapper.toResponse(saved));
        });
  }

  /**
   * Updates a question.
   *
   * @param id the question id
   * @param request the question to update
   * @return the updated question
   */
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public QuestionResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody QuestionRequest request) {

    logger.info("Recebendo atualização de questão id={}", id);
    return QuestionMapper.toResponse(
        questionService.update(QuestionMapper.toDomain(request, id)));
  }

  /**
   * Deletes a question.
   *
   * @param id the question id
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable(value = "id") Long id) {

    logger.info("Recebendo remoção de questão id={}", id);
    questionService.delete(id);
  }
}