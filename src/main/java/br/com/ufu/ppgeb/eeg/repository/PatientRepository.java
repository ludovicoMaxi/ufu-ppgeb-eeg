package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Patient entities.
 */
public interface PatientRepository
    extends JpaRepository<Patient, Long>,
    PatientRepositoryCustom {

  /**
   * Checks if patient exists by document number.
   *
   * @param documentNumber the document number
   * @return true if exists
   */
  boolean existsByDocumentNumber(String documentNumber);
}
