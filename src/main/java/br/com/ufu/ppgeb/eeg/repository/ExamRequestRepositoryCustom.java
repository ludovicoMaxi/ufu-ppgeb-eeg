package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;

/**
 * Custom repository for ExamRequest queries.
 */
public interface ExamRequestRepositoryCustom {

  /**
   * Finds exam requests by filter.
   *
   * @param medicalRecord the medical record
   * @param medicalRequest the medical request
   * @param patientId the patient id
   * @param doctorRequestant the doctor requestant
   * @return the list of exam requests
   */
  List<ExamRequest> findByFilter(Long medicalRecord,
      Long medicalRequest, Long patientId,
      String doctorRequestant);

}
