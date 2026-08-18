package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Exam;
import java.util.List;

/**
 * Custom repository for Exam queries.
 */
public interface ExamRepositoryCustom {

  /**
   * Finds exams by filter.
   *
   * @param id the exam id
   * @param bed the bed
   * @param patientId the patient id
   * @param examRequestId the exam request id
   * @return the list of exams
   */
  List<Exam> findByFilter(
      Long id, String bed, Long patientId,
      Long examRequestId);

}
