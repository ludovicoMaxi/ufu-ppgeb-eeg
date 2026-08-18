package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ExamEquipment entities.
 */
public interface ExamEquipmentRepository
    extends JpaRepository<ExamEquipment, Long> {

  /**
   * Finds exam equipment by exam.
   *
   * @param exam the exam
   * @return the list of exam equipment
   */
  List<ExamEquipment> findByExam(Exam exam);

}
