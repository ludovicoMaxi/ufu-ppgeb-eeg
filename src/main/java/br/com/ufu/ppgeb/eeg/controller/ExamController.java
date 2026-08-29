package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ExamRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamResponse;
import br.com.ufu.ppgeb.eeg.mapper.ExamMapper;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.service.ExamService;
import jakarta.validation.Valid;
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
  public List<ExamResponse> list(
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
    return Optional.ofNullable(examService.findByFilter(id, bed, patientId, examRequestId))
        .orElse(List.of())
        .stream()
        .map(ExamMapper::toResponse)
        .toList();
  }

  /**
   * Finds an exam by id.
   *
   * @param id the exam id
   * @return the exam
   */
  @GetMapping("/{id}")
  public ExamResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando exame id={}", id);
    return ExamMapper.toResponse(examService.findById(id));
  }

  /**
   * Saves a new exam.
   *
   * @param request the exam to save
   * @return the saved exam
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ExamResponse save(@Valid @RequestBody ExamRequest request) {

    logger.info("Recebendo criação de exame");
    return ExamMapper.toResponse(examService.save(ExamMapper.toEntity(request)));
  }

  /**
   * Updates an exam.
   *
   * @param id the exam id
   * @param request the exam to update
   * @return the updated exam
   */
  @PutMapping("/{id}")
  public ExamResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody ExamRequest request) {

    logger.info("Recebendo atualização de exame id={}", id);
    return ExamMapper.toResponse(examService.update(ExamMapper.toEntity(request, id)));
  }

  /**
   * Updates exam medicaments.
   *
   * @param id the exam id
   * @param request the exam with medicament list
   * @return the updated exam
   */
  @PutMapping("/{id}" + ApiPaths.MEDICAMENTS_SUBPATH)
  public ExamResponse updateExamMedicament(
      @PathVariable(value = "id") Long id,
      @RequestBody ExamRequest request) {

    logger.info("Recebendo atualização de medicamentos do exame id={}", id);
    Exam exam = examService.updateExamMedicament(
        ExamMapper.toEntity(request, id));
    return ExamMapper.toResponse(exam);
  }

  /**
   * Updates exam equipment.
   *
   * @param id the exam id
   * @param request the exam with equipment list
   * @return the updated exam
   */
  @PutMapping("/{id}" + ApiPaths.EQUIPMENTS_SUBPATH)
  public ExamResponse updateExamEquipment(
      @PathVariable(value = "id") Long id,
      @RequestBody ExamRequest request) {

    logger.info("Recebendo atualização de equipamentos do exame id={}", id);
    Exam exam = examService.updateExamEquipment(
        ExamMapper.toEntity(request, id));
    return ExamMapper.toResponse(exam);
  }
}
