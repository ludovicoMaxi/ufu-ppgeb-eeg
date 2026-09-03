package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.EpochRequest;
import br.com.ufu.ppgeb.eeg.dto.EpochResponse;
import br.com.ufu.ppgeb.eeg.mapper.EpochMapper;
import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.service.EpochService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for epochs of an exam.
 */
@RestController
@RequestMapping(ApiPaths.EXAM_EPOCHS)
@AllArgsConstructor
public class EpochController {

  private static final Logger logger = LoggerFactory.getLogger(EpochController.class);

  private final EpochService epochService;

  /**
   * Lists epochs by exam id.
   *
   * @param examId the exam id
   * @return the list of epochs
   */
  @GetMapping
  public List<EpochResponse> list(@PathVariable(value = "examId") Long examId) {

    logger.info("Consultando épocas do exame id={}", examId);
    return Optional.ofNullable(epochService.findByFilter(examId))
        .orElse(List.of())
        .stream()
        .map(EpochMapper::toResponse)
        .toList();
  }

  /**
   * Finds an epoch by id.
   *
   * @param examId the exam id
   * @param id the epoch id
   * @return the epoch
   */
  @GetMapping("/{id}")
  public EpochResponse findById(
      @PathVariable(value = "examId") Long examId,
      @PathVariable(value = "id") Long id) {

    logger.info("Consultando época id={} do exame id={}", id, examId);
    return EpochMapper.toResponse(epochService.findById(id));
  }

  /**
   * Saves a new epoch for an exam.
   *
   * @param examId the exam id
   * @param request the epoch to save
   * @return the saved epoch
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EpochResponse> save(
      @PathVariable(value = "examId") Long examId,
      @Valid @RequestBody EpochRequest request) {

    logger.info("Recebendo criação de época do exame id={}", examId);
    Epoch saved = epochService.save(EpochMapper.toEntity(request, examId));
    URI location = URI.create(ApiPaths.EXAM_EPOCHS
        .replace(ApiPaths.EXAM_ID_PATTERN, examId.toString())
        + ApiPaths.PATH_SEPARATOR + saved.getId());
    return ResponseEntity.created(location)
        .body(EpochMapper.toResponse(saved));
  }

  /**
   * Updates a list of epochs of an exam.
   *
   * @param examId the exam id
   * @param epochs the epochs to update
   * @return the updated epoch list
   */
  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<EpochResponse> updateList(
      @PathVariable(value = "examId") Long examId,
      @Valid @RequestBody List<@Valid EpochRequest> epochs) {

    logger.info("Recebendo atualização de épocas do exame id={}", examId);
    List<Epoch> entities = Optional.ofNullable(epochs)
        .orElse(List.of())
        .stream()
        .filter(Objects::nonNull)
        .map(request -> EpochMapper.toEntity(request, examId))
        .toList();
    return epochService.updateList(examId, entities)
        .stream()
        .map(EpochMapper::toResponse)
        .toList();
  }
}
