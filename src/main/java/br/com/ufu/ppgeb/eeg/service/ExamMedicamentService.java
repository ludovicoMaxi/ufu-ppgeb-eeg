package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.ExamMedicament;

/**
 * Service interface for ExamMedicament operations.
 */
public interface ExamMedicamentService {

  /**
   * Finds exam medicaments by exam id.
   *
   * @param examId the exam id
   * @return the list of exam medicaments
   */
  List<ExamMedicament> findByExamId(Long examId);

  /**
   * Finds an exam medicament by id.
   *
   * @param id the exam medicament id
   * @return the exam medicament
   */
  ExamMedicament findById(Long id);

  /**
   * Saves an exam medicament.
   *
   * @param examMedicament the exam medicament to save
   * @return the saved exam medicament
   */
  ExamMedicament save(ExamMedicament examMedicament);

  /**
   * Updates a list of medicaments of an exam.
   *
   * @param examId the exam id
   * @param medicaments the medicaments to update
   * @return the updated list of medicaments
   */
  List<ExamMedicament> updateList(Long examId, List<ExamMedicament> medicaments);

  /**
   * Deletes an exam medicament by id.
   *
   * @param id the exam medicament id
   */
  void delete(Long id);
}
