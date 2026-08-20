package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.repository.ExamEquipmentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamMedicamentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamRepository;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;
import br.com.ufu.ppgeb.eeg.service.ExamService;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
  private static final String MSG_NOT_EXIST_BY_EXAM_ID = " not exist by examID=";

  private final ExamRepository examRepository;

  private final ExamMedicamentRepository examMedicamentRepository;

  private final ExamEquipmentRepository examEquipmentRepository;

  private final MedicamentService medicamentService;

  private final EquipmentService equipmentService;

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
  public List<Exam> findByFilter(Long id, String bed,
      Long patientId, Long examRequestId) {

    if (isNull(id)
        && StringUtils.isBlank(bed)
        && isNull(patientId)
        && isNull(examRequestId)) {
      throw new IllegalArgumentException("Informe pelo menos um campo para consultar!");
    }

    return examRepository.findByFilter(id, bed, patientId, examRequestId);
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

  private void registerUnregisteredMedicaments(List<ExamMedicament> examMedicamentList) {

    if (isNotEmpty(examMedicamentList)) {
      for (ExamMedicament examMedicament : examMedicamentList) {
        Medicament medicament = examMedicament.getMedicament();
        Assert.notNull(medicament, "medicament cannot be null.");
        Assert.hasText(medicament.getName(), "medicament name cannot be empty.");

        if (isNull(medicament.getId())) {
          examMedicament.setMedicament(medicamentService.save(medicament));
        }
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Exam updateExamMedicament(Exam exam) {

    Assert.notNull(exam, MSG_EXAM_NULL);
    Assert.notNull(exam.getId(), MSG_EXAM_ID_NULL);

    registerUnregisteredMedicaments(exam.getExamMedicaments());
    validateExamMedicamentList(exam.getExamMedicaments());

    Exam oldExam = examRepository.findById(exam.getId())
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, exam.getId()));

    List<ExamMedicament> currentMedicaments =
        isNull(exam.getExamMedicaments()) ? new ArrayList<>() : exam.getExamMedicaments();

    Map<Long, ExamMedicament> oldMedicamentsById = new HashMap<>();
    if (nonNull(oldExam.getExamMedicaments())) {
      for (ExamMedicament oldMedicament : oldExam.getExamMedicaments()) {
        oldMedicamentsById.put(oldMedicament.getId(), oldMedicament);
      }
    }

    List<ExamMedicament> savedMedicaments = new ArrayList<>();
    for (ExamMedicament medicamentItem : currentMedicaments) {

      medicamentItem.setExam(oldExam);

      if (nonNull(medicamentItem.getId())) {

        ExamMedicament oldMedicament = oldMedicamentsById.remove(medicamentItem.getId());
        if (isNull(oldMedicament)) {
          throw new IllegalArgumentException("Exam Medicament with id=" + medicamentItem.getId()
              + MSG_NOT_EXIST_BY_EXAM_ID + exam.getId());
        }

        if (!medicamentItem.equals(oldMedicament)) {
          medicamentItem = examMedicamentRepository.save(medicamentItem);
        }
      } else {
        medicamentItem = examMedicamentRepository.save(medicamentItem);
      }
      savedMedicaments.add(medicamentItem);
    }

    examMedicamentRepository.deleteAll(oldMedicamentsById.values());

    exam.setExamMedicaments(savedMedicaments);
    log.info("Medicamentos do exame atualizados; examId={}, quantidade={}", exam.getId(), savedMedicaments.size());
    return exam;
  }

  private void validateExamMedicamentList(List<ExamMedicament> examMedicamentList) {

    if (isNotEmpty(examMedicamentList)) {
      for (ExamMedicament examMedicament : examMedicamentList) {
        validateExamMedicament(examMedicament);
      }
    }
  }

  private void validateExamMedicament(ExamMedicament examMedicament) {

    Assert.notNull(examMedicament, "examMedicament cannot be null");
    Assert.notNull(examMedicament.getAmount(), "examMedicament-amount cannot be null");
    Assert.notNull(examMedicament.getMedicament(), "examMedicament-medicament cannot be null");
    Assert.notNull(examMedicament.getMedicament().getId(),
        "examMedicament-medicament-id cannot be null");
    Assert.notNull(examMedicament.getUnit(), "examMedicament-unit cannot be null");
    Assert.notNull(examMedicament.getUnit().getId(), "examMedicament-unit-id cannot be null");
  }

  private void registerUnregisteredEquipments(List<ExamEquipment> examEquipmentList) {

    if (isNotEmpty(examEquipmentList)) {
      for (ExamEquipment examEquipment : examEquipmentList) {
        Equipment equipment = examEquipment.getEquipment();
        Assert.notNull(equipment, "equipment cannot be null.");
        Assert.hasText(equipment.getName(), "equipment name cannot be empty.");

        if (isNull(equipment.getId())) {
          examEquipment.setEquipment(equipmentService.save(equipment));
        }
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Exam updateExamEquipment(Exam exam) {

    Assert.notNull(exam, MSG_EXAM_NULL);
    Assert.notNull(exam.getId(), MSG_EXAM_ID_NULL);

    registerUnregisteredEquipments(exam.getExamEquipments());
    validateExamEquipmentList(exam.getExamEquipments());

    Exam oldExam = examRepository.findById(exam.getId())
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, exam.getId()));

    List<ExamEquipment> currentEquipments =
        isNull(exam.getExamEquipments()) ? new ArrayList<>() : exam.getExamEquipments();

    Map<Long, ExamEquipment> oldEquipmentsById = new HashMap<>();
    if (nonNull(oldExam.getExamEquipments())) {
      for (ExamEquipment oldEquipment : oldExam.getExamEquipments()) {
        oldEquipmentsById.put(oldEquipment.getId(), oldEquipment);
      }
    }

    List<ExamEquipment> savedEquipments = new ArrayList<>();
    for (ExamEquipment equipmentItem : currentEquipments) {

      equipmentItem.setExam(oldExam);

      if (nonNull(equipmentItem.getId())) {

        ExamEquipment oldEquipment = oldEquipmentsById.remove(equipmentItem.getId());
        if (isNull(oldEquipment)) {
          throw new IllegalArgumentException("Exam Equipment with id=" + equipmentItem.getId()
              + MSG_NOT_EXIST_BY_EXAM_ID + exam.getId());
        }

        if (!equipmentItem.equals(oldEquipment)) {
          equipmentItem = examEquipmentRepository.save(equipmentItem);
        }
      } else {
        equipmentItem = examEquipmentRepository.save(equipmentItem);
      }
      savedEquipments.add(equipmentItem);
    }

    examEquipmentRepository.deleteAll(oldEquipmentsById.values());

    exam.setExamEquipments(savedEquipments);
    log.info("Equipamentos do exame atualizados; examId={}, quantidade={}", exam.getId(), savedEquipments.size());
    return exam;
  }

  private void validateExamEquipmentList(List<ExamEquipment> examEquipmentList) {

    if (isNotEmpty(examEquipmentList)) {
      for (ExamEquipment examEquipment : examEquipmentList) {
        validateExamEquipment(examEquipment);
      }
    }
  }

  private void validateExamEquipment(ExamEquipment examEquipment) {

    Assert.notNull(examEquipment, "examEquipment cannot be null");
    Assert.notNull(examEquipment.getAmount(), "examEquipment-amount cannot be null");
    Assert.notNull(examEquipment.getEquipment(), "examEquipment-equipment cannot be null");
    Assert.notNull(examEquipment.getEquipment().getId(),
        "examEquipment-equipment-id cannot be null");
    Assert.notNull(examEquipment.getUnit(), "examEquipment-unit cannot be null");
    Assert.notNull(examEquipment.getUnit().getId(), "examEquipment-unit-id cannot be null");
  }
}
