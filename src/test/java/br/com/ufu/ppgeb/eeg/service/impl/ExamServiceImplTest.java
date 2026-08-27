package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.model.Unit;
import br.com.ufu.ppgeb.eeg.repository.ExamEquipmentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamMedicamentRepository;
import br.com.ufu.ppgeb.eeg.repository.ExamRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

  private static final String MSG_EXAM_NULL = "exam cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_EXAM_ID_NULL = "exam ID cannot be null.";
  private static final String MSG_PATIENT_NULL = "patient cannot be null.";
  private static final String MSG_PATIENT_ID_NULL = "patient id cannot be null.";
  private static final String MSG_FILTER_EMPTY = "Informe pelo menos um campo para consultar!";
  private static final String MSG_PATIENT_ID_DIFFERENT = "Patient ID is different. New=";
  private static final String MSG_EXAM_REQUEST_CHANGED = "Exam request ID cannot be changed.";
  private static final String MSG_MEDICAMENT_NOT_EXIST = "Exam Medicament with id=";
  private static final String MSG_EQUIPMENT_NOT_EXIST = "Exam Equipment with id=";
  private static final String MSG_NOT_EXIST_BY_EXAM_ID = " not exist by examID=";
  private static final String RESOURCE_NAME = "Exam";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final String UPDATED_BED = "NEW BED";
  private static final String DIFFERENT_BED = "DIFFERENT BED";
  private static final Long EXAM_ID = 1L;
  private static final Long PATIENT_ID = 10L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long DIFFERENT_PATIENT_ID = 999L;
  private static final Long AMOUNT_VALUE = 10L;
  private static final int TWO_EXAMS = 2;

  @Mock
  private ExamRepository examRepository;

  @Mock
  private ExamMedicamentRepository examMedicamentRepository;

  @Mock
  private ExamEquipmentRepository examEquipmentRepository;

  @InjectMocks
  private ExamServiceImpl examService;

  @Test
  @DisplayName("Given two exams in database when findAll then return all exams")
  void givenTwoExamsInDatabase_whenFindAll_thenReturnAllExams() {
    Exam exam1 = Instancio.create(Exam.class);
    Exam exam2 = Instancio.create(Exam.class);

    List<Exam> exams = List.of(exam1, exam2);
    when(examRepository.findAll()).thenReturn(exams);

    List<Exam> result = examService.findAll();

    assertThat(result).hasSize(TWO_EXAMS);
    verify(examRepository).findAll();
  }

  @Test
  @DisplayName("Given no exams in database when findAll then return empty list")
  void givenNoExamsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(examRepository.findAll()).thenReturn(Collections.emptyList());

    List<Exam> result = examService.findAll();

    assertThat(result).isEmpty();
    verify(examRepository).findAll();
  }

  @Test
  @DisplayName("Given valid exam when save then return saved exam")
  void givenValidExam_whenSave_thenReturnSavedExam() {
    Exam exam = Instancio.create(Exam.class);

    when(examRepository.save(any(Exam.class)))
        .thenReturn(exam);

    examService.save(exam);

    verify(examRepository).save(exam);
  }

  @Test
  @DisplayName("Given null exam when save then throw exception")
  void givenNullExam_whenSave_thenThrowException() {
    assertThatThrownBy(() -> examService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_NULL);

    verify(examRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam with null patient when save then throw exception")
  void givenExamWithNullPatient_whenSave_thenThrowException() {
    Exam exam = Instancio.create(Exam.class);
    exam.setPatient(null);

    assertThatThrownBy(() -> examService.save(exam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_PATIENT_NULL);

    verify(examRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam with null patient id when save then throw exception")
  void givenExamWithNullPatientId_whenSave_thenThrowException() {
    Exam exam = Instancio.create(Exam.class);
    exam.getPatient().setId(null);

    assertThatThrownBy(() -> examService.save(exam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_PATIENT_ID_NULL);

    verify(examRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when findById then return exam")
  void givenExistingId_whenFindById_thenReturnExam() {
    Exam exam = Instancio.create(Exam.class);
    exam.setId(EXAM_ID);

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(exam));

    Exam result = examService.findById(EXAM_ID);

    assertThat(result.getId()).isEqualTo(EXAM_ID);
    verify(examRepository).findById(EXAM_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {
    assertThatThrownBy(() -> examService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(examRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(examRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> examService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid id when findByFilter then return matching exams")
  void givenValidId_whenFindByFilter_thenReturnMatchingExams() {
    Exam exam = Instancio.create(Exam.class);

    when(examRepository.findByFilter(EXAM_ID, null, null, null)).thenReturn(List.of(exam));

    List<Exam> result = examService.findByFilter(EXAM_ID, null, null, null);

    assertThat(result).hasSize(1);
    verify(examRepository).findByFilter(EXAM_ID, null, null, null);
  }

  @Test
  @DisplayName("Given all filter fields blank when findByFilter then throw exception")
  void givenAllFilterFieldsBlank_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> examService.findByFilter(null, "", null, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_FILTER_EMPTY);

    verify(examRepository, never()).findByFilter(any(), any(), any(), any());
  }

  @Test
  @DisplayName("Given existing id when delete then remove exam")
  void givenExistingId_whenDelete_thenRemoveExam() {
    when(examRepository.existsById(EXAM_ID)).thenReturn(true);

    examService.delete(EXAM_ID);

    verify(examRepository).existsById(EXAM_ID);
    verify(examRepository).deleteById(EXAM_ID);
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {
    assertThatThrownBy(() -> examService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(examRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when delete then throw exception")
  void givenNonexistentId_whenDelete_thenThrowException() {
    when(examRepository.existsById(NONEXISTENT_ID)).thenReturn(false);

    assertThatThrownBy(() -> examService.delete(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(examRepository, never()).deleteById(NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid exam with changed data when update then return updated exam")
  void givenValidExamWithChangedData_whenUpdate_thenReturnUpdatedExam() {
    Exam oldExam = createExam(EXAM_ID, PATIENT_ID, "OLD BED");
    Exam newExam = createExam(EXAM_ID, PATIENT_ID, UPDATED_BED);

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(oldExam));
    when(examRepository.save(any(Exam.class)))
        .thenReturn(oldExam);

    Exam result = examService.update(newExam);

    assertThat(result.getBed()).isEqualTo(UPDATED_BED);
    verify(examRepository).findById(EXAM_ID);
    verify(examRepository).save(oldExam);
  }

  private Exam createExam(Long id, Long patientId, String bed) {
    Exam exam = Instancio.create(Exam.class);
    exam.setId(id);
    if (nonNull(patientId)) {
      exam.getPatient().setId(patientId);
    }
    if (nonNull(bed)) {
      exam.setBed(bed);
    }
    exam.setExamRequest(null);
    return exam;
  }

  @Test
  @DisplayName("Given null exam when update then throw exception")
  void givenNullExam_whenUpdate_thenThrowException() {
    assertThatThrownBy(() -> examService.update(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_NULL);

    verify(examRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam with null id when update then throw exception")
  void givenExamWithNullId_whenUpdate_thenThrowException() {
    Exam exam = Instancio.create(Exam.class);
    exam.setId(null);

    assertThatThrownBy(() -> examService.update(exam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_ID_NULL);

    verify(examRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam with different patient id when update then throw exception")
  void givenExamWithDifferentPatientId_whenUpdate_thenThrowException() {
    Exam oldExam = createExam(EXAM_ID, PATIENT_ID, DIFFERENT_BED);
    Exam newExam = createExam(EXAM_ID, DIFFERENT_PATIENT_ID, DIFFERENT_BED);

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(oldExam));

    assertThatThrownBy(() -> examService.update(newExam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(MSG_PATIENT_ID_DIFFERENT);

    verify(examRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam with different exam request id when update then throw exception")
  void givenExamWithDifferentExamRequestId_whenUpdate_thenThrowException() {
    Exam oldExam = createExam(EXAM_ID, PATIENT_ID, DIFFERENT_BED);
    Exam newExam = createExam(EXAM_ID, PATIENT_ID, DIFFERENT_BED);
    newExam.setExamRequest(Instancio.create(ExamRequest.class));

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(oldExam));

    assertThatThrownBy(() -> examService.update(newExam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_REQUEST_CHANGED);

    verify(examRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam with medicaments when updateExamMedicament then return updated exam")
  void givenExamWithMedicaments_whenUpdateExamMedicament_thenReturnUpdatedExam() {
    ExamMedicament examMedicament = createExamMedicament(null);
    Exam exam = createExamWithMedicaments(examMedicament);
    Exam oldExam = createExamWithEmptyMedicaments();

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(oldExam));
    when(examMedicamentRepository.save(any(ExamMedicament.class)))
        .thenReturn(examMedicament);

    Exam result = examService.updateExamMedicament(exam);

    assertThat(result.getExamMedicaments()).hasSize(1);
    verify(examRepository).findById(EXAM_ID);
    verify(examMedicamentRepository).save(examMedicament);
  }

  private Medicament createMedicament() {
    return Instancio.of(Medicament.class)
        .set(field(Medicament::getId), 1L)
        .set(field(Medicament::getName), "DIPIRONA")
        .create();
  }

  private Unit createUnit() {
    return Instancio.of(Unit.class)
        .set(field(Unit::getId), 1L)
        .create();
  }

  private ExamMedicament createExamMedicament(Long id) {
    ExamMedicament examMedicament = new ExamMedicament();
    examMedicament.setId(id);
    examMedicament.setAmount(AMOUNT_VALUE);
    examMedicament.setMedicament(createMedicament());
    examMedicament.setUnit(createUnit());
    return examMedicament;
  }

  private Exam createExamWithMedicaments(ExamMedicament examMedicament) {
    Exam exam = createExam(EXAM_ID, null, null);
    exam.setExamMedicaments(List.of(examMedicament));
    return exam;
  }

  private Exam createExamWithEmptyMedicaments() {
    Exam exam = createExam(EXAM_ID, null, null);
    exam.setExamMedicaments(new ArrayList<>());
    return exam;
  }

  @Test
  @DisplayName("Given null exam when updateExamMedicament then throw exception")
  void givenNullExam_whenUpdateExamMedicament_thenThrowException() {
    assertThatThrownBy(() -> examService.updateExamMedicament(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_NULL);

    verify(examRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given exam with null id when updateExamMedicament then throw exception")
  void givenExamWithNullId_whenUpdateExamMedicament_thenThrowException() {
    Exam exam = Instancio.create(Exam.class);
    exam.setId(null);

    assertThatThrownBy(() -> examService.updateExamMedicament(exam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_ID_NULL);

    verify(examRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given exam medicament with nonexistent id when updateExamMedicament then throw exception")
  void givenExamMedicamentWithNonexistentId_whenUpdateExamMedicament_thenThrowException() {
    ExamMedicament examMedicament = createExamMedicament(NONEXISTENT_ID);
    Exam exam = createExamWithMedicaments(examMedicament);
    Exam oldExam = createExamWithEmptyMedicaments();

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(oldExam));

    assertThatThrownBy(() -> examService.updateExamMedicament(exam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(MSG_MEDICAMENT_NOT_EXIST + NONEXISTENT_ID + MSG_NOT_EXIST_BY_EXAM_ID + EXAM_ID);

    verify(examMedicamentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam with equipments when updateExamEquipment then return updated exam")
  void givenExamWithEquipments_whenUpdateExamEquipment_thenReturnUpdatedExam() {
    ExamEquipment examEquipment = createExamEquipment(null);
    Exam exam = createExamWithEquipments(examEquipment);
    Exam oldExam = createExamWithEmptyEquipments();

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(oldExam));
    when(examEquipmentRepository.save(any(ExamEquipment.class)))
        .thenReturn(examEquipment);

    Exam result = examService.updateExamEquipment(exam);

    assertThat(result.getExamEquipments()).hasSize(1);
    verify(examRepository).findById(EXAM_ID);
    verify(examEquipmentRepository).save(examEquipment);
  }

  private Equipment createEquipment() {
    return Instancio.of(Equipment.class)
        .set(field(Equipment::getId), 1L)
        .set(field(Equipment::getName), "BRAINVISIAN")
        .create();
  }

  private ExamEquipment createExamEquipment(Long id) {
    ExamEquipment examEquipment = new ExamEquipment();
    examEquipment.setId(id);
    examEquipment.setAmount(AMOUNT_VALUE);
    examEquipment.setEquipment(createEquipment());
    examEquipment.setUnit(createUnit());
    return examEquipment;
  }

  private Exam createExamWithEquipments(ExamEquipment examEquipment) {
    Exam exam = createExam(EXAM_ID, null, null);
    exam.setExamEquipments(List.of(examEquipment));
    return exam;
  }

  private Exam createExamWithEmptyEquipments() {
    Exam exam = createExam(EXAM_ID, null, null);
    exam.setExamEquipments(new ArrayList<>());
    return exam;
  }

  @Test
  @DisplayName("Given null exam when updateExamEquipment then throw exception")
  void givenNullExam_whenUpdateExamEquipment_thenThrowException() {
    assertThatThrownBy(() -> examService.updateExamEquipment(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_NULL);

    verify(examRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given exam with null id when updateExamEquipment then throw exception")
  void givenExamWithNullId_whenUpdateExamEquipment_thenThrowException() {
    Exam exam = Instancio.create(Exam.class);
    exam.setId(null);

    assertThatThrownBy(() -> examService.updateExamEquipment(exam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_ID_NULL);

    verify(examRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given exam equipment with nonexistent id when updateExamEquipment then throw exception")
  void givenExamEquipmentWithNonexistentId_whenUpdateExamEquipment_thenThrowException() {
    ExamEquipment examEquipment = createExamEquipment(NONEXISTENT_ID);
    Exam exam = createExamWithEquipments(examEquipment);
    Exam oldExam = createExamWithEmptyEquipments();

    when(examRepository.findById(EXAM_ID)).thenReturn(Optional.of(oldExam));

    assertThatThrownBy(() -> examService.updateExamEquipment(exam))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(MSG_EQUIPMENT_NOT_EXIST + NONEXISTENT_ID + MSG_NOT_EXIST_BY_EXAM_ID + EXAM_ID);

    verify(examEquipmentRepository, never()).save(any());
  }
}
