package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.repository.ExamMedicamentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamRepository;
import br.com.ufu.ppgeb.eeg.service.ExamMedicamentService;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of ExamMedicamentService - mirrors ActivityServiceImpl/EpochServiceImpl.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ExamMedicamentServiceImpl implements ExamMedicamentService {

  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "ExamMedicament";

  private final ExamMedicamentRepository examMedicamentRepository;
  private final ExamRepository examRepository;
  private final MedicamentService medicamentService;

  @Override
  @Transactional(readOnly = true)
  public Page<ExamMedicament> findByExamId(Long examId, Pageable pageable) {

    Assert.notNull(examId, "examId cannot be null.");
    return examMedicamentRepository.findByExam(Exam.builder().id(examId).build(), pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public ExamMedicament findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return examMedicamentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ExamMedicament save(ExamMedicament examMedicament) {

    Assert.notNull(examMedicament, "examMedicament cannot be null.");
    Assert.isNull(examMedicament.getId(), "id must be null");
    validateExamMedicament(examMedicament);
    registerUnregisteredMedicament(examMedicament);
    ExamMedicament saved = examMedicamentRepository.save(examMedicament);
    log.info("Medicamento do exame criado com id={}", saved.getId());
    return saved;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    if (!examMedicamentRepository.existsById(id)) {
      throw new ResourceNotFoundException(RESOURCE_NAME, id);
    }
    examMedicamentRepository.deleteById(id);
    log.info("Medicamento do exame removido com id={}", id);
  }

  private void registerUnregisteredMedicaments(List<ExamMedicament> list) {

    if (isNotEmpty(list)) {
      for (ExamMedicament em : list) {
        registerUnregisteredMedicament(em);
      }
    }
  }

  private void registerUnregisteredMedicament(ExamMedicament em) {

    Medicament medicament = em.getMedicament();
    Assert.notNull(medicament, "medicament cannot be null.");
    Assert.hasText(medicament.getName(), "medicament name cannot be empty.");
    if (isNull(medicament.getId())) {
      em.setMedicament(medicamentService.save(medicament));
    }
  }

  private void validateExamMedicamentList(List<ExamMedicament> list) {

    if (isNotEmpty(list)) {
      for (ExamMedicament em : list) {
        validateExamMedicament(em);
      }
    }
  }

  private void validateExamMedicament(ExamMedicament em) {

    Assert.notNull(em, "examMedicament cannot be null");
    Assert.notNull(em.getAmount(), "examMedicament-amount cannot be null");
    Assert.notNull(em.getMedicament(), "examMedicament-medicament cannot be null");
    Assert.notNull(em.getMedicament().getId(), "examMedicament-medicament-id cannot be null");
    Assert.notNull(em.getUnit(), "examMedicament-unit cannot be null");
    Assert.notNull(em.getUnit().getId(), "examMedicament-unit-id cannot be null");
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<ExamMedicament> updateList(Long examId, List<ExamMedicament> currentMedicaments) {

    Assert.notNull(examId, "ExamId cannot be null.");

    if (!examRepository.existsById(examId)) {
      throw new ResourceNotFoundException("Exam", examId);
    }

    registerUnregisteredMedicaments(currentMedicaments);
    validateExamMedicamentList(currentMedicaments);

    Exam examRef = Exam.builder().id(examId).build();

    Map<Long, ExamMedicament> oldById = new HashMap<>();
    for (ExamMedicament old : examMedicamentRepository.findByExam(examRef)) {
      oldById.put(old.getId(), old);
    }

    List<ExamMedicament> saved = new ArrayList<>();
    if (isNotEmpty(currentMedicaments)) {
      for (ExamMedicament item : currentMedicaments) {

        if (nonNull(item.getExam())
            && nonNull(item.getExam().getId())
            && !item.getExam().getId().equals(examId)) {
          throw new IllegalArgumentException(item + " is not same examId in update=" + examId);
        }
        item.setExam(examRef);

        if (nonNull(item.getId())) {
          ExamMedicament old = oldById.remove(item.getId());
          if (isNull(old)) {
            throw new IllegalArgumentException("Exam Medicament with id=" + item.getId()
                + " not exist by examID=" + examId);
          }
          if (!item.equals(old)) {
            validateExamMedicament(item);
            item = examMedicamentRepository.save(item);
          }
        } else {
          validateExamMedicament(item);
          item = examMedicamentRepository.save(item);
        }
        saved.add(item);
      }
    }

    examMedicamentRepository.deleteAll(oldById.values());

    log.info("Medicamentos do exame atualizados; examId={}, quantidade={}", examId, saved.size());
    return saved;
  }
}
