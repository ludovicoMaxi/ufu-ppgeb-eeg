package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.Patient;
import java.util.List;

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
   * @return the list of patients
   */
  List<Patient> findByFilter(
      String name, String documentNumber);

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
