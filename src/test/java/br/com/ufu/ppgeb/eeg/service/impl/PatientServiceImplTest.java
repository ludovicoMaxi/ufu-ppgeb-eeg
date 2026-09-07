package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.model.Sex;
import br.com.ufu.ppgeb.eeg.repository.PatientRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

  private static final String PATIENT_NAME = "JOHN DOE";
  private static final String DOCUMENT_NUMBER = "12345678900";
  private static final String MSG_PATIENT_NULL = "patient cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_NAME_EMPTY = "name cannot be empty.";
  private static final String MSG_DOCUMENT_NUMBER_EMPTY = "documentNumber cannot be empty.";
  private static final String MSG_BIRTH_DATE_EMPTY = "birthDate cannot be empty.";
  private static final String MSG_NATIONALITY_NULL = "nationality cannot be null.";
  private static final String MSG_SEX_NULL = "sex cannot be null.";
  private static final String MSG_CPF_DUPLICATED = "CPF já foi cadastrado, por favor informe outro.";
  private static final String MSG_FILTER_EMPTY = "Informe pelo menos um campo para consultar!";
  private static final String MSG_PATIENT_ID_NULL = "patient ID cannot be null.";
  private static final String MSG_DOCUMENT_NUMBER_DIVERGENT = "CPF/CNPJ está divergente.";
  private static final String RESOURCE_NAME = "Patient";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final String UPDATED_NAME = "NEW NAME";
  private static final Long PATIENT_ID = 1L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final String OLD_NAME = "OLD NAME";
  private static final int TWO_PATIENTS = 2;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Mock
  private PatientRepository patientRepository;

  @InjectMocks
  private PatientServiceImpl patientService;

  @Test
  @DisplayName("Given two patients in database when findAll then return all patients")
  void givenTwoPatientsInDatabase_whenFindAll_thenReturnAllPatients() {
    Patient patient1 = Instancio.create(Patient.class);
    Patient patient2 = Instancio.create(Patient.class);

    List<Patient> patients = List.of(patient1, patient2);
    when(patientRepository.findAll()).thenReturn(patients);

    List<Patient> result = patientService.findAll();

    assertThat(result).hasSize(TWO_PATIENTS);
    verify(patientRepository).findAll();
  }

  @Test
  @DisplayName("Given no patients in database when findAll then return empty list")
  void givenNoPatientsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(patientRepository.findAll()).thenReturn(Collections.emptyList());

    List<Patient> result = patientService.findAll();

    assertThat(result).isEmpty();
    verify(patientRepository).findAll();
  }

  @Test
  @DisplayName("Given valid patient when save then return saved patient")
  void givenValidPatient_whenSave_thenReturnSavedPatient() {
    Patient patient = Instancio.create(Patient.class);
    patient.setSex(Sex.MALE);
    patient.setDocumentNumber(DOCUMENT_NUMBER);

    when(patientRepository.existsByDocumentNumber(DOCUMENT_NUMBER)).thenReturn(false);
    when(patientRepository.save(any(Patient.class)))
        .thenReturn(patient);

    patientService.save(patient);

    verify(patientRepository).existsByDocumentNumber(DOCUMENT_NUMBER);
    verify(patientRepository).save(patient);
  }

  @Test
  @DisplayName("Given null patient when save then throw exception")
  void givenNullPatient_whenSave_thenThrowException() {
    assertThatThrownBy(() -> patientService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_PATIENT_NULL);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with empty name when save then throw exception")
  void givenPatientWithEmptyName_whenSave_thenThrowException() {
    Patient patient = Instancio.create(Patient.class);
    patient.setName("");

    assertThatThrownBy(() -> patientService.save(patient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_NAME_EMPTY);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with empty document number when save then throw exception")
  void givenPatientWithEmptyDocumentNumber_whenSave_thenThrowException() {
    Patient patient = Instancio.create(Patient.class);
    patient.setDocumentNumber("");

    assertThatThrownBy(() -> patientService.save(patient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DOCUMENT_NUMBER_EMPTY);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with null birth date when save then throw exception")
  void givenPatientWithNullBirthDate_whenSave_thenThrowException() {
    Patient patient = Instancio.create(Patient.class);
    patient.setBirthDate(null);

    assertThatThrownBy(() -> patientService.save(patient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_BIRTH_DATE_EMPTY);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with null nationality when save then throw exception")
  void givenPatientWithNullNationality_whenSave_thenThrowException() {
    Patient patient = Instancio.create(Patient.class);
    patient.setNationality(null);

    assertThatThrownBy(() -> patientService.save(patient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_NATIONALITY_NULL);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with null sex when save then throw exception")
  void givenPatientWithNullSex_whenSave_thenThrowException() {
    Patient patient = Instancio.create(Patient.class);
    patient.setSex(null);

    assertThatThrownBy(() -> patientService.save(patient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_SEX_NULL);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with duplicated document number when save then throw exception")
  void givenPatientWithDuplicatedDocumentNumber_whenSave_thenThrowException() {
    Patient patient = Instancio.create(Patient.class);
    patient.setSex(Sex.MALE);
    patient.setDocumentNumber(DOCUMENT_NUMBER);

    when(patientRepository.existsByDocumentNumber(DOCUMENT_NUMBER)).thenReturn(true);

    assertThatThrownBy(() -> patientService.save(patient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_CPF_DUPLICATED);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when findById then return patient")
  void givenExistingId_whenFindById_thenReturnPatient() {
    Patient patient = Instancio.create(Patient.class);
    patient.setId(PATIENT_ID);

    when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));

    Patient result = patientService.findById(PATIENT_ID);

    assertThat(result.getId()).isEqualTo(PATIENT_ID);
    verify(patientRepository).findById(PATIENT_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {
    assertThatThrownBy(() -> patientService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(patientRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(patientRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> patientService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid name when findByFilter then return matching patients")
  void givenValidName_whenFindByFilter_thenReturnMatchingPatients() {
    Patient patient = Instancio.create(Patient.class);

    when(patientRepository.findByFilter(PATIENT_NAME, null, PAGEABLE))
        .thenReturn(new PageImpl<>(List.of(patient)));

    Page<Patient> result = patientService.findByFilter(PATIENT_NAME, null, PAGEABLE);

    assertThat(result.getContent()).hasSize(1);
    verify(patientRepository).findByFilter(PATIENT_NAME, null, PAGEABLE);
  }

  @Test
  @DisplayName("Given both filter fields blank when findByFilter then throw exception")
  void givenBothFilterFieldsBlank_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> patientService.findByFilter("", "", PAGEABLE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_FILTER_EMPTY);

    verify(patientRepository, never()).findByFilter(any(), any(), any());
  }

  @Test
  @DisplayName("Given existing id when delete then remove patient")
  void givenExistingId_whenDelete_thenRemovePatient() {
    when(patientRepository.existsById(PATIENT_ID)).thenReturn(true);

    patientService.delete(PATIENT_ID);

    verify(patientRepository).existsById(PATIENT_ID);
    verify(patientRepository).deleteById(PATIENT_ID);
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {
    assertThatThrownBy(() -> patientService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(patientRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when delete then throw exception")
  void givenNonexistentId_whenDelete_thenThrowException() {
    when(patientRepository.existsById(NONEXISTENT_ID)).thenReturn(false);

    assertThatThrownBy(() -> patientService.delete(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(patientRepository, never()).deleteById(NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid patient with changed data when update then return updated patient")
  void givenValidPatientWithChangedData_whenUpdate_thenReturnUpdatedPatient() {
    Patient oldPatient = createPatient(PATIENT_ID, DOCUMENT_NUMBER, OLD_NAME);
    Patient newPatient = createPatient(PATIENT_ID, DOCUMENT_NUMBER, UPDATED_NAME);

    when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(oldPatient));
    when(patientRepository.save(any(Patient.class)))
        .thenReturn(oldPatient);

    Patient result = patientService.update(newPatient);

    assertThat(result.getName()).isEqualTo(UPDATED_NAME);
    verify(patientRepository).findById(PATIENT_ID);
    verify(patientRepository).save(oldPatient);
  }

  private Patient createPatient(Long id, String documentNumber, String name) {
    Patient patient = Instancio.create(Patient.class);
    patient.setId(id);
    patient.setDocumentNumber(documentNumber);
    patient.setName(name);
    patient.setSex(Sex.MALE);
    return patient;
  }

  @Test
  @DisplayName("Given null patient when update then throw exception")
  void givenNullPatient_whenUpdate_thenThrowException() {
    assertThatThrownBy(() -> patientService.update(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_PATIENT_NULL);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with null id when update then throw exception")
  void givenPatientWithNullId_whenUpdate_thenThrowException() {
    Patient patient = Instancio.create(Patient.class);
    patient.setSex(Sex.MALE);
    patient.setId(null);

    assertThatThrownBy(() -> patientService.update(patient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_PATIENT_ID_NULL);

    verify(patientRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given patient with different document number when update then throw exception")
  void givenPatientWithDifferentDocumentNumber_whenUpdate_thenThrowException() {
    Patient oldPatient = createPatient(PATIENT_ID, DOCUMENT_NUMBER, OLD_NAME);
    Patient newPatient = createPatient(PATIENT_ID, "99999999999", "DIFFERENT NAME");

    when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(oldPatient));

    assertThatThrownBy(() -> patientService.update(newPatient))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DOCUMENT_NUMBER_DIVERGENT);

    verify(patientRepository, never()).save(any());
  }
}
