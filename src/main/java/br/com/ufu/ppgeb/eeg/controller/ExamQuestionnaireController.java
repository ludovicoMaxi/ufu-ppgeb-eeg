package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerResponse;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireResponse;
import br.com.ufu.ppgeb.eeg.mapper.QuestionnaireAnswerMapper;
import br.com.ufu.ppgeb.eeg.mapper.QuestionnaireMapper;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireAnswerService;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireService;
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
 * REST controller for questionnaires of an exam.
 */
@RestController
@RequestMapping(ApiPaths.EXAM_QUESTIONNAIRES)
@AllArgsConstructor
public class ExamQuestionnaireController {

  private static final Logger logger = LoggerFactory.getLogger(ExamQuestionnaireController.class);

  private final QuestionnaireService questionnaireService;
  private final QuestionnaireAnswerService questionnaireAnswerService;

  /**
   * Lists questionnaires applicable to an exam.
   *
   * @param examId the exam id
   * @param pageable the pagination information
   * @return the page of questionnaires
   */
  @GetMapping
  public Page<QuestionnaireResponse> list(
      @PathVariable(value = "examId") Long examId,
      Pageable pageable) {

    logger.info("Consultando questionários aplicados ao exame id={}", examId);
    return questionnaireAnswerService
        .findAppliedQuestionnaires(ObjectType.EXAM.getId(), examId, pageable)
        .map(QuestionnaireMapper::toResponse);
  }

  /**
   * Lists the answers of a questionnaire applied to an exam.
   *
   * @param examId the exam id
   * @param questionnaireId the questionnaire id
   * @return the list of answers
   */
  @GetMapping("/{questionnaireId}/answers")
  public List<QuestionnaireAnswerResponse> findAnswers(
      @PathVariable(value = "examId") Long examId,
      @PathVariable(value = "questionnaireId") Long questionnaireId) {

    logger.info("Consultando respostas do questionário id={} do exame id={}",
        questionnaireId, examId);
    return questionnaireAnswerService
        .findAppliedAnswers(questionnaireId, ObjectType.EXAM.getId(), examId)
        .stream()
        .map(QuestionnaireAnswerMapper::toResponse)
        .toList();
  }

  /**
   * Updates the answers of a questionnaire applied to an exam.
   *
   * @param examId the exam id
   * @param questionnaireId the questionnaire id
   * @param answers the answers to update
   * @return the updated list of answers
   */
  @PutMapping(value = "/{questionnaireId}/answers",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<QuestionnaireAnswerResponse> updateAnswers(
      @PathVariable(value = "examId") Long examId,
      @PathVariable(value = "questionnaireId") Long questionnaireId,
      @Valid @RequestBody List<@Valid QuestionnaireAnswerRequest> answers) {

    logger.info("Recebendo atualização de respostas do questionário id={} "
        + "do exame id={}", questionnaireId, examId);
    List<QuestionnaireAnswer> entities = Optional.ofNullable(answers)
            .orElse(List.of())
            .stream()
            .filter(Objects::nonNull)
            .map(answer -> QuestionnaireAnswerMapper.toDomain(answer, questionnaireId,
                ObjectType.EXAM.getId(), examId))
            .toList();
    return questionnaireAnswerService
        .updateList(questionnaireId, ObjectType.EXAM.getId(), examId, entities)
        .stream()
        .map(QuestionnaireAnswerMapper::toResponse)
        .toList();
  }
}