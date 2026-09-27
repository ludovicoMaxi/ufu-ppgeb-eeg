package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireResponse;
import br.com.ufu.ppgeb.eeg.mapper.QuestionnaireMapper;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireService;
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
 * REST controller for questionnaire operations.
 */
@RestController
@RequestMapping(ApiPaths.QUESTIONNAIRE)
@AllArgsConstructor
public class QuestionnaireController {

  private static final Logger logger = LoggerFactory.getLogger(QuestionnaireController.class);

  private final QuestionnaireService questionnaireService;
  private final IdempotencyService idempotencyService;

  /**
   * Lists questionnaires with optional filters.
   *
   * @param id the questionnaire id filter
   * @param title the questionnaire title filter
   * @param pageable the pagination information
   * @return the page of questionnaires
   */
  @GetMapping
  public Page<QuestionnaireResponse> list(
      @RequestParam(value = "id",
          required = false) Long id,
      @RequestParam(value = "title",
          required = false) String title,
      Pageable pageable) {

    logger.info("Consultando questionários; id={}, title={}", id, title);
    return questionnaireService.findByFilter(id, title, pageable)
        .map(QuestionnaireMapper::toResponse);
  }

  /**
   * Finds a questionnaire by id.
   *
   * @param id the questionnaire id
   * @return the questionnaire
   */
  @GetMapping("/{id}")
  public QuestionnaireResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando questionário id={}", id);
    return QuestionnaireMapper.toResponse(questionnaireService.findById(id));
  }

  /**
   * Saves a new questionnaire.
   *
   * @param idempotencyKey the idempotency key
   * @param request the questionnaire to save
   * @return the saved questionnaire
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QuestionnaireResponse> save(
      @RequestHeader(name = IdempotencyService.IDEMPOTENCY_KEY_HEADER,
          required = false) String idempotencyKey,
      @Valid @RequestBody QuestionnaireRequest request) {

    logger.info("Recebendo criação de questionário");
    return idempotencyService.execute(
        "QUESTIONNAIRE", idempotencyKey, QuestionnaireResponse.class,
        () -> {
          Questionnaire saved = questionnaireService
              .save(QuestionnaireMapper.toDomain(request));
          URI location = URI.create(ApiPaths.QUESTIONNAIRE + "/" + saved.getId());
          return ResponseEntity.created(location)
              .body(QuestionnaireMapper.toResponse(saved));
        });
  }

  /**
   * Updates a questionnaire.
   *
   * @param id the questionnaire id
   * @param request the questionnaire to update
   * @return the updated questionnaire
   */
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public QuestionnaireResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody QuestionnaireRequest request) {

    logger.info("Recebendo atualização de questionário id={}", id);
    return QuestionnaireMapper.toResponse(
        questionnaireService.update(QuestionnaireMapper.toDomain(request, id)));
  }

  /**
   * Deletes a questionnaire.
   *
   * @param id the questionnaire id
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable(value = "id") Long id) {

    logger.info("Recebendo remoção de questionário id={}", id);
    questionnaireService.delete(id);
  }
}