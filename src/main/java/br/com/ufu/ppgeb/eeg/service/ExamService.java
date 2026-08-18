package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.Exam;
import java.util.List;

/**
 * Service interface for Exam operations.
 */
public interface ExamService {

  /**
   * Saves an exam.
   *
   * @param exam the exam to save
   * @return the saved exam
   */
  Exam save(Exam exam);

  /**
   * Finds an exam by id.
   *
   * @param id the exam id
   * @return the exam
   */
  Exam findById(Long id);

  /**
   * Finds exams by filter.
   *
   * @param id the exam id
   * @param bed the bed
   * @param patientId the patient id
   * @param examRequestId the exam request id
   * @return the list of exams
   */
  List<Exam> findByFilter(Long id, String bed,
      Long patientId, Long examRequestId);

  /**
   * Finds all exams.
   *
   * @return the list of exams
   */
  List<Exam> findAll();

  /**
   * Deletes an exam by id.
   *
   * @param id the exam id
   */
  void delete(Long id);

  /**
   * Updates an exam.
   *
   * @param exam the exam to update
   * @return the updated exam
   */
  Exam update(Exam exam);

  /**
   * Updates exam medicaments.
   *
   * @param exam the exam with medicaments
   * @return the updated exam
   */
  Exam updateExamMedicament(Exam exam);

  /**
   * Updates exam equipment.
   *
   * @param exam the exam with equipment
   * @return the updated exam
   */
  Exam updateExamEquipment(Exam exam);

}
