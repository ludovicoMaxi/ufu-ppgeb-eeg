package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.AnswerOptionRequest;
import br.com.ufu.ppgeb.eeg.dto.AnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.mapper.AnswerOptionMapper;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.service.AnswerOptionService;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
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
 * REST controller for answer option operations.
 */
@RestController
@RequestMapping(ApiPaths.ANSWER_OPTION)
@AllArgsConstructor
public class AnswerOptionController {

  private static final Logger logger = LoggerFactory.getLogger(AnswerOptionController.class);

  private final AnswerOptionService answerOptionService;
  private final IdempotencyService idempotencyService;

  /**
   * Lists answer options, applying the optional filters.
   *
   * @param id the id filter
   * @param text the text filter
   * @param active the active flag filter
   * @param pageable the pagination information
   * @return the page of answer options
   */
  @GetMapping
  public Page<AnswerOptionResponse> list(
      @RequestParam(value = "id", required = false) Long id,
      @RequestParam(value = "text", required = false) String text,
      @RequestParam(value = "active", required = false) Boolean active,
      Pageable pageable) {

    logger.info("Consultando alternativas de resposta; id={}, text={}, active={}",
        id, text, active);
    return answerOptionService.findByFilter(id, text, active, pageable)
        .map(AnswerOptionMapper::toResponse);
  }

  /**
   * Finds an answer option by id.
   *
   * @param id the answer option id
   * @return the answer option
   */
  @GetMapping("/{id}")
  public AnswerOptionResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando alternativa de resposta id={}", id);
    return AnswerOptionMapper.toResponse(answerOptionService.findById(id));
  }

  /**
   * Saves a new answer option.
   *
   * @param idempotencyKey the idempotency key
   * @param request the answer option to save
   * @return the saved answer option
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AnswerOptionResponse> save(
      @RequestHeader(name = IdempotencyService.IDEMPOTENCY_KEY_HEADER,
          required = false) String idempotencyKey,
      @Valid @RequestBody AnswerOptionRequest request) {

    logger.info("Recebendo criação de alternativa de resposta");
    return idempotencyService.execute(
        "ANSWER_OPTION", idempotencyKey, AnswerOptionResponse.class,
        () -> {
          AnswerOption saved = answerOptionService.save(AnswerOptionMapper.toDomain(request));
          URI location = URI.create(ApiPaths.ANSWER_OPTION + "/" + saved.getId());
          return ResponseEntity.created(location)
              .body(AnswerOptionMapper.toResponse(saved));
        });
  }

  /**
   * Updates an answer option.
   *
   * @param id the answer option id
   * @param request the answer option to update
   * @return the updated answer option
   */
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public AnswerOptionResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody AnswerOptionRequest request) {

    logger.info("Recebendo atualização de alternativa de resposta id={}", id);
    return AnswerOptionMapper.toResponse(
        answerOptionService.update(AnswerOptionMapper.toDomain(request, id)));
  }

  /**
   * Deletes an answer option.
   *
   * @param id the answer option id
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable(value = "id") Long id) {

    logger.info("Recebendo remoção de alternativa de resposta id={}", id);
    answerOptionService.delete(id);
  }
}