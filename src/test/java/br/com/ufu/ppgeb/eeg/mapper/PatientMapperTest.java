package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.PatientRequest;
import br.com.ufu.ppgeb.eeg.dto.PatientResponse;
import br.com.ufu.ppgeb.eeg.model.Patient;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PatientMapperTest {

  private static final Long ID = 1001L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a patient request when mapping to entity then map every field keeping id null")
  void givenPatientRequest_whenToEntity_thenMapEveryFieldKeepingIdNull() {
    PatientRequest request = Instancio.create(PatientRequest.class);

    Patient patient = PatientMapper.toEntity(request);

    assertThat(patient)
        .hasNoNullFieldsOrPropertiesExcept("id", CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(patient.getName()).isEqualTo(request.name());
    assertThat(patient.getDocumentNumber()).isEqualTo(request.documentNumber());
    assertThat(patient.getSex()).isEqualTo(request.sex());
    assertThat(patient.getBirthDate()).isEqualTo(request.birthDate());
    assertThat(patient.getNationality()).isEqualTo(request.nationality());
    assertThat(patient.getCivilStatus()).isEqualTo(request.civilStatus());
    assertThat(patient.getJob()).isEqualTo(request.job());
  }

  @Test
  @DisplayName("Given a patient request and an id when mapping to entity then map every field with the id")
  void givenPatientRequestAndId_whenToEntity_thenMapEveryFieldWithTheId() {
    PatientRequest request = Instancio.create(PatientRequest.class);

    Patient patient = PatientMapper.toEntity(request, ID);

    assertThat(patient)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(patient.getId()).isEqualTo(ID);
    assertThat(patient.getName()).isEqualTo(request.name());
    assertThat(patient.getDocumentNumber()).isEqualTo(request.documentNumber());
    assertThat(patient.getSex()).isEqualTo(request.sex());
    assertThat(patient.getBirthDate()).isEqualTo(request.birthDate());
    assertThat(patient.getNationality()).isEqualTo(request.nationality());
    assertThat(patient.getCivilStatus()).isEqualTo(request.civilStatus());
    assertThat(patient.getJob()).isEqualTo(request.job());
  }

  @Test
  @DisplayName("Given a null patient request when mapping to entity then return null")
  void givenNullPatientRequest_whenToEntity_thenReturnNull() {

    Patient patient = PatientMapper.toEntity(null);

    assertThat(patient).isNull();
  }

  @Test
  @DisplayName("Given a patient when mapping to response then map every field")
  void givenPatient_whenToResponse_thenMapEveryField() {
    Patient patient = Instancio.create(Patient.class);

    PatientResponse response = PatientMapper.toResponse(patient);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(patient.getId());
    assertThat(response.name()).isEqualTo(patient.getName());
    assertThat(response.documentNumber()).isEqualTo(patient.getDocumentNumber());
    assertThat(response.sex()).isEqualTo(patient.getSex());
    assertThat(response.birthDate()).isEqualTo(patient.getBirthDate());
    assertThat(response.nationality()).isEqualTo(patient.getNationality());
    assertThat(response.civilStatus()).isEqualTo(patient.getCivilStatus());
    assertThat(response.job()).isEqualTo(patient.getJob());
    assertThat(response.createdAt()).isEqualTo(patient.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(patient.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(patient.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(patient.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null patient when mapping to response then return null")
  void givenNullPatient_whenToResponse_thenReturnNull() {

    PatientResponse response = PatientMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
