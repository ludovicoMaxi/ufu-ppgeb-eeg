package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  /**
   * Finds exam equipment by exam paginated.
   *
   * @param exam the exam
   * @param pageable the pagination information
   * @return the page of exam equipment
   */
  Page<ExamEquipment> findByExam(Exam exam, Pageable pageable);

}