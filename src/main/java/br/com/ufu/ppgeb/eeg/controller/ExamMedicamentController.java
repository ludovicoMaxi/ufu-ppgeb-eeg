package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentResponse;
import br.com.ufu.ppgeb.eeg.mapper.ExamMedicamentMapper;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.service.ExamMedicamentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for medicaments of an exam - mirrors EpochController/ActivityController.
 */
@RestController
@RequestMapping(ApiPaths.EXAM_MEDICAMENTS)
@AllArgsConstructor
public class ExamMedicamentController {

  private static final Logger logger = LoggerFactory.getLogger(ExamMedicamentController.class);

  private final ExamMedicamentService examMedicamentService;

  /**
   * Lists medicaments by exam id.
   *
   * @param examId the exam id
   * @param pageable the pagination information
   * @return the page of medicaments
   */
  @GetMapping
  public Page<ExamMedicamentResponse> list(
      @PathVariable(value = "examId") Long examId,
      Pageable pageable) {

    logger.info("Consultando medicamentos do exame id={}", examId);
    return examMedicamentService.findByExamId(examId, pageable)
        .map(ExamMedicamentMapper::toResponse);
  }

  /**
   * Updates a list of medicaments of an exam.
   *
   * @param examId the exam id
   * @param medicaments the medicaments to update
   * @return the updated medicament list
   */
  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<ExamMedicamentResponse> updateList(
      @PathVariable(value = "examId") Long examId,
      @Valid @RequestBody List<@Valid ExamMedicamentRequest> medicaments) {

    logger.info("Recebendo atualização de medicamentos do exame id={}", examId);
    List<ExamMedicament> entities = Optional.ofNullable(medicaments)
        .orElse(List.of())
        .stream()
        .filter(Objects::nonNull)
        .map(request -> ExamMedicamentMapper.toDomain(request, examId))
        .toList();
    return examMedicamentService.updateList(examId, entities)
        .stream()
        .map(ExamMedicamentMapper::toResponse)
        .toList();
  }
}
