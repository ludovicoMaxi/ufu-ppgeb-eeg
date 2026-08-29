package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.service.ExamService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for exam operations.
 */
@RestController
@RequestMapping(ApiPaths.EXAM)
@AllArgsConstructor
public class ExamController {

  private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

  private final ExamService examService;

  /**
   * Lists exams with optional filters.
   *
   * @param id the exam id filter
   * @param bed the bed filter
   * @param patientId the patient id filter
   * @param examRequestId the exam request id filter
   * @return the list of exams
   */
  @GetMapping
  public List<Exam> list(
      @RequestParam(value = "id",
          required = false) Long id,
      @RequestParam(value = "bed",
          required = false) String bed,
      @RequestParam(value = "patientId",
          required = false) Long patientId,
      @RequestParam(value = "examRequestId",
          required = false) Long examRequestId) {

    logger.info("Consultando exames; id={}, bed={}, patientId={}, examRequestId={}",
        id, bed, patientId, examRequestId);
    return examService.findByFilter(id, bed, patientId, examRequestId);
  }

  /**
   * Finds an exam by id.
   *
   * @param id the exam id
   * @return the exam
   */
  @GetMapping("/{id}")
  public Exam findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando exame id={}", id);
    return examService.findById(id);
  }

  /**
   * Saves a new exam.
   *
   * @param exam the exam to save
   * @return the saved exam
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Exam save(@RequestBody Exam exam) {

    logger.info("Recebendo criação de exame");
    return examService.save(exam);
  }

  /**
   * Updates an exam.
   *
   * @param exam the exam to update
   * @return the updated exam
   */
  @PutMapping
  public Exam update(@RequestBody Exam exam) {

    logger.info("Recebendo atualização de exame id={}", exam.getId());
    return examService.update(exam);
  }

  /**
   * Updates exam medicaments.
   *
   * @param examMedicamentList the exam with medicament list
   * @return the updated exam
   */
  @PutMapping(ApiPaths.MEDICAMENT_SUBPATH)
  public Exam updateExamMedicament(@RequestBody Exam examMedicamentList) {

    logger.info("Recebendo atualização de medicamentos do exame id={}",
        examMedicamentList.getId());
    return examService.updateExamMedicament(examMedicamentList);
  }

  /**
   * Updates exam equipment.
   *
   * @param examEquipmentList the exam with equipment list
   * @return the updated exam
   */
  @PutMapping(ApiPaths.EQUIPMENT_SUBPATH)
  public Exam updateExamEquipment(@RequestBody Exam examEquipmentList) {

    logger.info("Recebendo atualização de equipamentos do exame id={}",
        examEquipmentList.getId());
    return examService.updateExamEquipment(examEquipmentList);
  }
}
