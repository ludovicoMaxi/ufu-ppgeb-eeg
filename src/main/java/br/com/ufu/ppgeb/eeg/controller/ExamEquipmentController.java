package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentResponse;
import br.com.ufu.ppgeb.eeg.mapper.ExamEquipmentMapper;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.service.ExamEquipmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for equipments of an exam - mirrors EpochController/ActivityController.
 */
@RestController
@RequestMapping(ApiPaths.EXAM_EQUIPMENTS)
@AllArgsConstructor
public class ExamEquipmentController {

  private static final Logger logger = LoggerFactory.getLogger(ExamEquipmentController.class);

  private final ExamEquipmentService examEquipmentService;

  /**
   * Lists equipments by exam id.
   *
   * @param examId the exam id
   * @return the list of equipments
   */
  @GetMapping
  public List<ExamEquipmentResponse> list(@PathVariable(value = "examId") Long examId) {

    logger.info("Consultando equipamentos do exame id={}", examId);
    return Optional.ofNullable(examEquipmentService.findByExamId(examId))
        .orElse(List.of())
        .stream()
        .map(ExamEquipmentMapper::toResponse)
        .toList();
  }

  /**
   * Updates a list of equipments of an exam.
   *
   * @param examId the exam id
   * @param equipments the equipments to update
   * @return the updated equipment list
   */
  @PutMapping
  public List<ExamEquipmentResponse> updateList(
      @PathVariable(value = "examId") Long examId,
      @RequestBody List<ExamEquipmentRequest> equipments) {

    logger.info("Recebendo atualização de equipamentos do exame id={}", examId);
    List<ExamEquipment> entities = Optional.ofNullable(equipments)
        .orElse(List.of())
        .stream()
        .filter(Objects::nonNull)
        .map(request -> ExamEquipmentMapper.toEntity(request, examId))
        .toList();
    return examEquipmentService.updateList(examId, entities)
        .stream()
        .map(ExamEquipmentMapper::toResponse)
        .toList();
  }
}
