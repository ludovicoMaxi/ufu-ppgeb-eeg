package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ExamRequestRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamRequestResponse;
import br.com.ufu.ppgeb.eeg.mapper.ExamRequestMapper;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.service.ExamRequestService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for exam request operations.
 */
@RestController
@RequestMapping(ApiPaths.EXAM_REQUEST)
@AllArgsConstructor
public class ExamRequestController {

  private static final Logger logger = LoggerFactory.getLogger(ExamRequestController.class);

  private final ExamRequestService examRequestService;

  /**
   * Lists exam requests with optional filters.
   *
   * @param medicalRecord the medical record filter
   * @param medicalRequest the medical request filter
   * @param patientId the patient id filter
   * @param doctorRequestant the doctor requestant filter
   * @return the list of exam requests
   */
  @GetMapping
  public List<ExamRequestResponse> list(
          @RequestParam(value = "medicalRecord", required = false) Long medicalRecord,
          @RequestParam(value = "medicalRequest", required = false) Long medicalRequest,
          @RequestParam(value = "patientId", required = false) Long patientId,
          @RequestParam(value = "doctorRequestant", required = false) String doctorRequestant) {

    logger.info("Consultando solicitações; medicalRecord={}, medicalRequest={}, patientId={}, doctorRequestant={}",
            medicalRecord, medicalRequest, patientId, doctorRequestant);
    return Optional.ofNullable(examRequestService.findByFilter(
            medicalRecord, medicalRequest, patientId, doctorRequestant))
        .orElse(List.of())
        .stream()
        .map(ExamRequestMapper::toResponse)
        .toList();
  }

  /**
   * Finds an exam request by id.
   *
   * @param id the exam request id
   * @return the exam request
   */
  @GetMapping("/{id}")
  public ExamRequestResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando solicitação de exame id={}", id);
    return ExamRequestMapper.toResponse(examRequestService.findById(id));
  }

  /**
   * Saves a new exam request.
   *
   * @param request the exam request to save
   * @return the saved exam request
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ExamRequestResponse> save(@Valid @RequestBody ExamRequestRequest request) {

    logger.info("Recebendo criação de solicitação de exame");
    ExamRequest saved = examRequestService.save(ExamRequestMapper.toEntity(request));
    URI location = URI.create(ApiPaths.EXAM_REQUEST + ApiPaths.PATH_SEPARATOR + saved.getId());
    return ResponseEntity.created(location)
        .body(ExamRequestMapper.toResponse(saved));
  }

  /**
   * Updates an exam request.
   *
   * @param id the exam request id
   * @param request the exam request to update
   * @return the updated exam request
   */
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ExamRequestResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody ExamRequestRequest request) {

    logger.info("Recebendo atualização de solicitação de exame id={}", id);
    return ExamRequestMapper.toResponse(
        examRequestService.update(ExamRequestMapper.toEntity(request, id)));
  }
}
