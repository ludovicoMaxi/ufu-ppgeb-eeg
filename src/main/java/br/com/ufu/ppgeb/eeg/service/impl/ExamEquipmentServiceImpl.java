package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.repository.ExamEquipmentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamRepository;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;
import br.com.ufu.ppgeb.eeg.service.ExamEquipmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of ExamEquipmentService - mirrors ActivityServiceImpl/EpochServiceImpl.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ExamEquipmentServiceImpl implements ExamEquipmentService {

  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "ExamEquipment";

  private final ExamEquipmentRepository examEquipmentRepository;
  private final ExamRepository examRepository;
  private final EquipmentService equipmentService;

  @Override
  @Transactional(readOnly = true)
  public List<ExamEquipment> findByExamId(Long examId) {

    Assert.notNull(examId, "examId cannot be null.");
    return examEquipmentRepository.findByExam(Exam.builder().id(examId).build());
  }

  @Override
  @Transactional(readOnly = true)
  public ExamEquipment findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return examEquipmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ExamEquipment save(ExamEquipment examEquipment) {

    Assert.notNull(examEquipment, "examEquipment cannot be null.");
    Assert.isNull(examEquipment.getId(), "id must be null");
    validateExamEquipment(examEquipment);
    registerUnregisteredEquipment(examEquipment);
    ExamEquipment saved = examEquipmentRepository.save(examEquipment);
    log.info("Equipamento do exame criado com id={}", saved.getId());
    return saved;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    if (!examEquipmentRepository.existsById(id)) {
      throw new ResourceNotFoundException(RESOURCE_NAME, id);
    }
    examEquipmentRepository.deleteById(id);
    log.info("Equipamento do exame removido com id={}", id);
  }

  private void registerUnregisteredEquipments(List<ExamEquipment> list) {

    if (isNotEmpty(list)) {
      for (ExamEquipment ee : list) {
        registerUnregisteredEquipment(ee);
      }
    }
  }

  private void registerUnregisteredEquipment(ExamEquipment ee) {

    Equipment equipment = ee.getEquipment();
    Assert.notNull(equipment, "equipment cannot be null.");
    Assert.hasText(equipment.getName(), "equipment name cannot be empty.");
    if (isNull(equipment.getId())) {
      ee.setEquipment(equipmentService.save(equipment));
    }
  }

  private void validateExamEquipmentList(List<ExamEquipment> list) {

    if (isNotEmpty(list)) {
      for (ExamEquipment ee : list) {
        validateExamEquipment(ee);
      }
    }
  }

  private void validateExamEquipment(ExamEquipment ee) {

    Assert.notNull(ee, "examEquipment cannot be null");
    Assert.notNull(ee.getAmount(), "examEquipment-amount cannot be null");
    Assert.notNull(ee.getEquipment(), "examEquipment-equipment cannot be null");
    Assert.notNull(ee.getEquipment().getId(), "examEquipment-equipment-id cannot be null");
    Assert.notNull(ee.getUnit(), "examEquipment-unit cannot be null");
    Assert.notNull(ee.getUnit().getId(), "examEquipment-unit-id cannot be null");
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<ExamEquipment> updateList(Long examId, List<ExamEquipment> currentEquipments) {

    Assert.notNull(examId, "ExamId cannot be null.");

    if (!examRepository.existsById(examId)) {
      throw new ResourceNotFoundException("Exam", examId);
    }

    registerUnregisteredEquipments(currentEquipments);
    validateExamEquipmentList(currentEquipments);

    Exam examRef = Exam.builder().id(examId).build();

    Map<Long, ExamEquipment> oldById = new HashMap<>();
    for (ExamEquipment old : examEquipmentRepository.findByExam(examRef)) {
      oldById.put(old.getId(), old);
    }

    List<ExamEquipment> saved = new ArrayList<>();
    if (isNotEmpty(currentEquipments)) {
      for (ExamEquipment item : currentEquipments) {

        if (nonNull(item.getExam())
            && nonNull(item.getExam().getId())
            && !item.getExam().getId().equals(examId)) {
          throw new IllegalArgumentException(item + " is not same examId in update=" + examId);
        }
        item.setExam(examRef);

        if (nonNull(item.getId())) {
          ExamEquipment old = oldById.remove(item.getId());
          if (isNull(old)) {
            throw new IllegalArgumentException("Exam Equipment with id=" + item.getId()
                + " not exist by examID=" + examId);
          }
          if (!item.equals(old)) {
            validateExamEquipment(item);
            item = examEquipmentRepository.save(item);
          }
        } else {
          validateExamEquipment(item);
          item = examEquipmentRepository.save(item);
        }
        saved.add(item);
      }
    }

    examEquipmentRepository.deleteAll(oldById.values());

    log.info("Equipamentos do exame atualizados; examId={}, quantidade={}", examId, saved.size());
    return saved;
  }
}
