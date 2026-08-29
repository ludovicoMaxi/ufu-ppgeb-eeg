package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.PatientRequest;
import br.com.ufu.ppgeb.eeg.dto.PatientResponse;
import br.com.ufu.ppgeb.eeg.mapper.PatientMapper;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for patient operations.
 */
@RestController
@RequestMapping(ApiPaths.PATIENT)
@AllArgsConstructor
public class PatientController {

  private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

  private final PatientService patientService;

  /**
   * Lists patients with optional filters.
   *
   * @param name the name filter
   * @param documentNumber the document number filter
   * @return the list of patients
   */
  @GetMapping
  public List<PatientResponse> list(
      @RequestParam(value = "name",
          required = false) String name,
      @RequestParam(value = "documentNumber",
          required = false) String documentNumber) {

    logger.info("Consultando pacientes; name={}, documentNumber={}", name, documentNumber);
    return Optional.ofNullable(patientService.findByFilter(name, documentNumber))
        .orElse(List.of())
        .stream()
        .map(PatientMapper::toResponse)
        .toList();
  }

  /**
   * Finds a patient by id.
   *
   * @param id the patient id
   * @return the patient
   */
  @GetMapping("/{id}")
  public PatientResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando paciente id={}", id);
    return PatientMapper.toResponse(patientService.findById(id));
  }

  /**
   * Saves a new patient.
   *
   * @param request the patient to save
   * @return the saved patient
   */
  @PostMapping
  public ResponseEntity<PatientResponse> save(
      @Valid @RequestBody PatientRequest request) {

    logger.info("Recebendo criação de paciente");
    Patient saved = patientService.save(PatientMapper.toEntity(request));
    URI location = URI.create(ApiPaths.PATIENT + "/" + saved.getId());
    return ResponseEntity.created(location)
        .body(PatientMapper.toResponse(saved));
  }

  /**
   * Updates a patient.
   *
   * @param id the patient id
   * @param request the patient to update
   * @return the updated patient
   */
  @PutMapping("/{id}")
  public PatientResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody PatientRequest request) {

    logger.info("Recebendo atualização de paciente id={}", id);
    return PatientMapper.toResponse(patientService.update(PatientMapper.toEntity(request, id)));
  }
}
