package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ExamMedicament entities.
 */
public interface ExamMedicamentRepository
    extends JpaRepository<ExamMedicament, Long> {

  /**
   * Finds exam medicaments by exam.
   *
   * @param exam the exam
   * @return the list of exam medicaments
   */
  List<ExamMedicament> findByExam(Exam exam);

}
