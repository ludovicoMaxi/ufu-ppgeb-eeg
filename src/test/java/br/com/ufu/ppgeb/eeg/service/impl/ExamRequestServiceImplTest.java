package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.repository.ExamRequestRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExamRequestServiceImplTest {

  private static final String MSG_EXAM_REQUEST_NULL = "examRequest cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_MEDICAL_RECORD_NULL = "medicalRecord cannot be empty.";
  private static final String MSG_MEDICAL_REQUEST_NULL = "medicalRequest cannot be empty.";
  private static final String MSG_SECTOR_EMPTY = "sector cannot be empty.";
  private static final String MSG_DOCTOR_EMPTY = "doctorRequestant cannot be empty.";
  private static final String MSG_USER_EMPTY = "user cannot be empty.";
  private static final String MSG_REQUEST_DATE_NULL = "requestDate cannot be empty.";
  private static final String MSG_PATIENT_NULL = "patient cannot be null.";
  private static final String MSG_PATIENT_ID_NULL = "patient id cannot be null.";
  private static final String MSG_FILTER_EMPTY = "Informe pelo menos um campo para consultar!";
  private static final String MSG_EXAM_REQUEST_ID_NULL = "examRequest ID cannot be null.";
  private static final String MSG_PATIENT_ID_DIFFERENT = "Patient ID is different. New=";
  private static final String RESOURCE_NAME = "ExamRequest";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final String UPDATED_SECTOR = "NEW SECTOR";
  private static final Long EXAM_REQUEST_ID = 1L;
  private static final Long PATIENT_ID = 10L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long DIFFERENT_PATIENT_ID = 999L;
  private static final Long MEDICAL_RECORD_VALUE = 1000L;
  private static final String OLD_SECTOR = "OLD SECTOR";
  private static final int TWO_EXAM_REQUESTS = 2;

  @Mock
  private ExamRequestRepository examRequestRepository;

  @InjectMocks
  private ExamRequestServiceImpl examRequestService;

  @Test
  @DisplayName("Given two exam requests in database when findAll then return all exam requests")
  void givenTwoExamRequestsInDatabase_whenFindAll_thenReturnAllExamRequests() {
    ExamRequest request1 = Instancio.create(ExamRequest.class);
    ExamRequest request2 = Instancio.create(ExamRequest.class);

    List<ExamRequest> requests = List.of(request1, request2);
    when(examRequestRepository.findAll()).thenReturn(requests);

    List<ExamRequest> result = examRequestService.findAll();

    assertThat(result).hasSize(TWO_EXAM_REQUESTS);
    verify(examRequestRepository).findAll();
  }

  @Test
  @DisplayName("Given no exam requests in database when findAll then return empty list")
  void givenNoExamRequestsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(examRequestRepository.findAll()).thenReturn(Collections.emptyList());

    List<ExamRequest> result = examRequestService.findAll();

    assertThat(result).isEmpty();
    verify(examRequestRepository).findAll();
  }

  @Test
  @DisplayName("Given valid exam request when save then return saved exam request")
  void givenValidExamRequest_whenSave_thenReturnSavedExamRequest() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);

    when(examRequestRepository.save(any(ExamRequest.class)))
        .thenReturn(examRequest);

    examRequestService.save(examRequest);

    verify(examRequestRepository).save(examRequest);
  }

  @Test
  @DisplayName("Given null exam request when save then throw exception")
  void givenNullExamRequest_whenSave_thenThrowException() {
    assertThatThrownBy(() -> examRequestService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_REQUEST_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with null medical record when save then throw exception")
  void givenExamRequestWithNullMedicalRecord_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setMedicalRecord(null);

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_MEDICAL_RECORD_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with null medical request when save then throw exception")
  void givenExamRequestWithNullMedicalRequest_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setMedicalRequest(null);

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_MEDICAL_REQUEST_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with empty sector when save then throw exception")
  void givenExamRequestWithEmptySector_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setSector("");

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_SECTOR_EMPTY);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with empty doctor when save then throw exception")
  void givenExamRequestWithEmptyDoctor_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setDoctorRequestant("");

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DOCTOR_EMPTY);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with empty user when save then throw exception")
  void givenExamRequestWithEmptyUser_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setUser("");

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_USER_EMPTY);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with null request date when save then throw exception")
  void givenExamRequestWithNullRequestDate_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setRequestDate(null);

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_REQUEST_DATE_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with null patient when save then throw exception")
  void givenExamRequestWithNullPatient_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setPatient(null);

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_PATIENT_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with null patient id when save then throw exception")
  void givenExamRequestWithNullPatientId_whenSave_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.getPatient().setId(null);

    assertThatThrownBy(() -> examRequestService.save(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_PATIENT_ID_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when findById then return exam request")
  void givenExistingId_whenFindById_thenReturnExamRequest() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setId(EXAM_REQUEST_ID);

    when(examRequestRepository.findById(EXAM_REQUEST_ID)).thenReturn(Optional.of(examRequest));

    ExamRequest result = examRequestService.findById(EXAM_REQUEST_ID);

    assertThat(result.getId()).isEqualTo(EXAM_REQUEST_ID);
    verify(examRequestRepository).findById(EXAM_REQUEST_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {
    assertThatThrownBy(() -> examRequestService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(examRequestRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(examRequestRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> examRequestService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid medical record when findByFilter then return matching exam requests")
  void givenValidMedicalRecord_whenFindByFilter_thenReturnMatchingExamRequests() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);

    when(examRequestRepository.findByFilter(MEDICAL_RECORD_VALUE, null, null, null)).thenReturn(List.of(examRequest));

    List<ExamRequest> result = examRequestService.findByFilter(MEDICAL_RECORD_VALUE, null, null, null);

    assertThat(result).hasSize(1);
    verify(examRequestRepository).findByFilter(MEDICAL_RECORD_VALUE, null, null, null);
  }

  @Test
  @DisplayName("Given all filter fields blank when findByFilter then throw exception")
  void givenAllFilterFieldsBlank_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> examRequestService.findByFilter(null, null, null, ""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_FILTER_EMPTY);

    verify(examRequestRepository, never()).findByFilter(any(), any(), any(), any());
  }

  @Test
  @DisplayName("Given existing id when delete then remove exam request")
  void givenExistingId_whenDelete_thenRemoveExamRequest() {
    when(examRequestRepository.existsById(EXAM_REQUEST_ID)).thenReturn(true);

    examRequestService.delete(EXAM_REQUEST_ID);

    verify(examRequestRepository, times(1)).existsById(EXAM_REQUEST_ID);
    verify(examRequestRepository, times(1)).deleteById(EXAM_REQUEST_ID);
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {
    assertThatThrownBy(() -> examRequestService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(examRequestRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when delete then throw exception")
  void givenNonexistentId_whenDelete_thenThrowException() {
    when(examRequestRepository.existsById(NONEXISTENT_ID)).thenReturn(false);

    assertThatThrownBy(() -> examRequestService.delete(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(examRequestRepository, never()).deleteById(NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid exam request with changed data when update then return updated exam request")
  void givenValidExamRequestWithChangedData_whenUpdate_thenReturnUpdatedExamRequest() {
    ExamRequest oldRequest = createExamRequest(EXAM_REQUEST_ID, PATIENT_ID, OLD_SECTOR);
    ExamRequest newRequest = createExamRequest(EXAM_REQUEST_ID, PATIENT_ID, UPDATED_SECTOR);

    when(examRequestRepository.findById(EXAM_REQUEST_ID)).thenReturn(Optional.of(oldRequest));
    when(examRequestRepository.save(any(ExamRequest.class)))
        .thenReturn(oldRequest);

    ExamRequest result = examRequestService.update(newRequest);

    assertThat(result.getSector()).isEqualTo(UPDATED_SECTOR);
    verify(examRequestRepository).findById(EXAM_REQUEST_ID);
    verify(examRequestRepository).save(oldRequest);
  }

  private ExamRequest createExamRequest(Long id, Long patientId, String sector) {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setId(id);
    examRequest.getPatient().setId(patientId);
    examRequest.setSector(sector);
    return examRequest;
  }

  @Test
  @DisplayName("Given null exam request when update then throw exception")
  void givenNullExamRequest_whenUpdate_thenThrowException() {
    assertThatThrownBy(() -> examRequestService.update(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_REQUEST_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with null id when update then throw exception")
  void givenExamRequestWithNullId_whenUpdate_thenThrowException() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setId(null);

    assertThatThrownBy(() -> examRequestService.update(examRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_REQUEST_ID_NULL);

    verify(examRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given exam request with different patient id when update then throw exception")
  void givenExamRequestWithDifferentPatientId_whenUpdate_thenThrowException() {
    ExamRequest oldRequest = createExamRequest(EXAM_REQUEST_ID, PATIENT_ID, OLD_SECTOR);
    ExamRequest newRequest = createExamRequest(EXAM_REQUEST_ID, PATIENT_ID, "DIFFERENT SECTOR");
    newRequest.setPatient(createPatient(DIFFERENT_PATIENT_ID));

    when(examRequestRepository.findById(EXAM_REQUEST_ID)).thenReturn(Optional.of(oldRequest));

    assertThatThrownBy(() -> examRequestService.update(newRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(MSG_PATIENT_ID_DIFFERENT);

    verify(examRequestRepository, never()).save(any());
  }

  private Patient createPatient(Long id) {
    Patient patient = Instancio.create(Patient.class);
    patient.setId(id);
    return patient;
  }
}
