package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for Patient entities.
 */
public interface PatientRepository
    extends JpaRepository<Patient, Long>,
    JpaSpecificationExecutor<Patient> {

  /**
   * Checks if patient exists by document number.
   *
   * @param documentNumber the document number
   * @return true if exists
   */
  boolean existsByDocumentNumber(String documentNumber);
}