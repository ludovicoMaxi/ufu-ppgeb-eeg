package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import java.util.List;

/**
 * Service interface for ExamRequest operations.
 */
public interface ExamRequestService {

  /**
   * Saves an exam request.
   *
   * @param examRequest the exam request to save
   * @return the saved exam request
   */
  ExamRequest save(ExamRequest examRequest);

  /**
   * Finds an exam request by id.
   *
   * @param id the exam request id
   * @return the exam request
   */
  ExamRequest findById(Long id);

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

  /**
   * Finds all exam requests.
   *
   * @return the list of exam requests
   */
  List<ExamRequest> findAll();

  /**
   * Deletes an exam request by id.
   *
   * @param id the exam request id
   */
  void delete(Long id);

  /**
   * Updates an exam request.
   *
   * @param examRequest the exam request to update
   * @return the updated exam request
   */
  ExamRequest update(ExamRequest examRequest);

}
