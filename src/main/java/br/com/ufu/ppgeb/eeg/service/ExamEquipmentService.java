package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for ExamEquipment operations.
 */
public interface ExamEquipmentService {

  /**
   * Finds exam equipments by exam id paginated.
   *
   * @param examId the exam id
   * @param pageable the pagination information
   * @return the page of exam equipments
   */
  Page<ExamEquipment> findByExamId(Long examId, Pageable pageable);

  /**
   * Finds an exam equipment by id.
   *
   * @param id the exam equipment id
   * @return the exam equipment
   */
  ExamEquipment findById(Long id);

  /**
   * Saves an exam equipment.
   *
   * @param examEquipment the exam equipment to save
   * @return the saved exam equipment
   */
  ExamEquipment save(ExamEquipment examEquipment);

  /**
   * Updates a list of equipments of an exam.
   *
   * @param examId the exam id
   * @param equipments the equipments to update
   * @return the updated list of equipments
   */
  List<ExamEquipment> updateList(Long examId, List<ExamEquipment> equipments);

  /**
   * Deletes an exam equipment by id.
   *
   * @param id the exam equipment id
   */
  void delete(Long id);
}
