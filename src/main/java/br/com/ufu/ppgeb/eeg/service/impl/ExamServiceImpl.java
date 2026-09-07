package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

import java.util.List;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.repository.ExamRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamSpecifications;
import br.com.ufu.ppgeb.eeg.service.ExamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of ExamService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ExamServiceImpl implements ExamService {

  private static final String MSG_EXAM_NULL = "exam cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_EXAM_ID_NULL = "exam ID cannot be null.";
  private static final String RESOURCE_NAME = "Exam";

  private final ExamRepository examRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Exam save(Exam exam) {

    Assert.notNull(exam, MSG_EXAM_NULL);

    validateExam(exam);

    Exam saved = examRepository.save(exam);
    log.info("Exame criado com id={}", saved.getId());
    return saved;
  }

  private void validateExam(Exam exam) {

    Assert.notNull(exam, "Exam cannot be null.");
    Assert.notNull(exam.getPatient(), "patient cannot be null.");
    Assert.notNull(exam.getPatient().getId(), "patient id cannot be null.");
  }

  @Override
  @Transactional(readOnly = true)
  public List<Exam> findAll() {

    return examRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Exam findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return examRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Exam> findByFilter(Long id, String bed,
      Long patientId, Long examRequestId, Pageable pageable) {

    if (isNull(id)
        && StringUtils.isBlank(bed)
        && isNull(patientId)
        && isNull(examRequestId)) {
      throw new IllegalArgumentException("Informe pelo menos um campo para consultar!");
    }

    return examRepository.findAll(
        ExamSpecifications.withFilters(id, bed, patientId, examRequestId), pageable);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    if (!examRepository.existsById(id)) {
      throw new ResourceNotFoundException(RESOURCE_NAME, id);
    }
    examRepository.deleteById(id);
    log.info("Exame removido com id={}", id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Exam update(Exam exam) {

    Assert.notNull(exam, MSG_EXAM_NULL);

    validateExam(exam);
    Assert.notNull(exam.getId(), MSG_EXAM_ID_NULL);

    Long examId = exam.getId();
    Exam oldExam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, examId));

    if (!oldExam.equals(exam)) {

      if (!exam.getPatient().getId()
          .equals(oldExam.getPatient().getId())) {
        throw new IllegalArgumentException("Patient ID is different. New=" + exam.getPatient().getId()
            + ", Old=" + oldExam.getPatient().getId());
      }

      Long newExamRequestId =
          isNull(exam.getExamRequest()) ? null : exam.getExamRequest().getId();
      Long oldExamRequestId =
          isNull(oldExam.getExamRequest()) ? null : oldExam.getExamRequest().getId();
      if (notEqual(newExamRequestId, oldExamRequestId)) {
        throw new IllegalArgumentException("Exam request ID cannot be changed.");
      }

      oldExam.setAchievementDate(exam.getAchievementDate());
      oldExam.setMedicalReport(exam.getMedicalReport());
      oldExam.setConclusion(exam.getConclusion());
      oldExam.setBed(exam.getBed());
      oldExam.setHeight(exam.getHeight());
      oldExam.setWeight(exam.getWeight());
      oldExam.setClinicalData(exam.getClinicalData());

      validateExam(oldExam);
      oldExam = examRepository.save(oldExam);
      log.info("Exame atualizado com id={}", examId);
    }

    return oldExam;
  }
}
