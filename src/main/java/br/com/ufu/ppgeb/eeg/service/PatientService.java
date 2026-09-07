package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Patient operations.
 */
public interface PatientService {

  /**
   * Saves a patient.
   *
   * @param patient the patient to save
   * @return the saved patient
   */
  Patient save(Patient patient);

  /**
   * Finds a patient by id.
   *
   * @param id the patient id
   * @return the patient
   */
  Patient findById(Long id);

  /**
   * Finds patients by filter.
   *
   * @param name the name
   * @param documentNumber the document number
   * @param pageable the pagination information
   * @return the page of patients
   */
  Page<Patient> findByFilter(String name, String documentNumber, Pageable pageable);

  /**
   * Finds all patients.
   *
   * @return the list of patients
   */
  List<Patient> findAll();

  /**
   * Deletes a patient by id.
   *
   * @param id the patient id
   */
  void delete(Long id);

  /**
   * Updates a patient.
   *
   * @param patient the patient to update
   * @return the updated patient
   */
  Patient update(Patient patient);

}
