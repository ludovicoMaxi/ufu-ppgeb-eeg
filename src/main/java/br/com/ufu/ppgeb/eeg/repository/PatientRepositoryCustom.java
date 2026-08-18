package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Patient;
import java.util.List;

/**
 * Custom repository for Patient queries.
 */
public interface PatientRepositoryCustom {

  /**
   * Finds patients by filter.
   *
   * @param name the name
   * @param documentNumber the document number
   * @return the list of patients
   */
  List<Patient> findByFilter(
      String name, String documentNumber);

}
