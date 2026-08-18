package br.com.ufu.ppgeb.eeg.controller;

import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.service.PatientService;
import java.util.List;
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
 * REST controller for patient operations.
 */
@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {

  private static final Logger logger =
      LoggerFactory.getLogger(PatientController.class);

  private final PatientService patientService;

  /**
   * Lists patients with optional filters.
   *
   * @param name the name filter
   * @param documentNumber the document number filter
   * @return the list of patients
   */
  @GetMapping
  public List<Patient> list(
      @RequestParam(value = "name",
          required = false) String name,
      @RequestParam(value = "documentNumber",
          required = false) String documentNumber) {

    logger.info(
        "Consultando pacientes; name={}, documentNumber={}",
        name, documentNumber);
    return patientService.findByFilter(name, documentNumber);
  }

  /**
   * Finds a patient by id.
   *
   * @param id the patient id
   * @return the patient
   */
  @GetMapping("/{id}")
  public Patient findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando paciente id={}", id);
    return patientService.findById(id);
  }

  /**
   * Saves a new patient.
   *
   * @param patient the patient to save
   * @return the saved patient
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Patient save(@RequestBody Patient patient) {

    logger.info("Recebendo criação de paciente");
    return patientService.save(patient);
  }

  /**
   * Updates a patient.
   *
   * @param patient the patient to update
   * @return the updated patient
   */
  @PutMapping
  public Patient update(@RequestBody Patient patient) {

    logger.info(
        "Recebendo atualização de paciente id={}",
        patient.getId());
    return patientService.update(patient);
  }
}
