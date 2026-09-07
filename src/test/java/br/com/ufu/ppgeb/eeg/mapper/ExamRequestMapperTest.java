package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.ExamRequestRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamRequestResponse;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.model.Patient;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamRequestMapperTest {

  private static final Long ID = 1001L;
  private static final String ID_FIELD = "id";
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given an exam request request when mapping to domain then map every field keeping id null")
  void givenExamRequestRequest_whenToDomain_thenMapEveryFieldKeepingIdNull() {
    ExamRequestRequest request = Instancio.create(ExamRequestRequest.class);

    ExamRequest examRequest = ExamRequestMapper.toDomain(request);

    assertThat(examRequest)
        .hasNoNullFieldsOrPropertiesExcept(ID_FIELD, CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(examRequest.getId()).isNull();
    assertThat(examRequest.getMedicalRecord()).isEqualTo(request.medicalRecord());
    assertThat(examRequest.getMedicalRequest()).isEqualTo(request.medicalRequest());
    assertThat(examRequest.getSector()).isEqualTo(request.sector());
    assertThat(examRequest.getAgreement()).isEqualTo(request.agreement());
    assertThat(examRequest.getDoctorRequestant()).isEqualTo(request.doctorRequestant());
    assertThat(examRequest.getUser()).isEqualTo(request.user());
    assertThat(examRequest.getClinicOrigin()).isEqualTo(request.clinicOrigin());
    assertThat(examRequest.getCityOrigin()).isEqualTo(request.cityOrigin());
    assertThat(examRequest.getPatient()).isNotNull();
    assertThat(examRequest.getPatient().getId()).isEqualTo(request.patientId());
    assertThat(examRequest.getRequestDate()).isEqualTo(request.requestDate());
    assertThat(examRequest.getAchievementDate()).isEqualTo(request.achievementDate());
  }

  @Test
  @DisplayName("Given an exam request request and an id when mapping to domain then map every field with the id")
  void givenExamRequestRequestAndId_whenToDomain_thenMapEveryFieldWithTheId() {
    ExamRequestRequest request = Instancio.create(ExamRequestRequest.class);

    ExamRequest examRequest = ExamRequestMapper.toDomain(request, ID);

    assertThat(examRequest)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(examRequest.getId()).isEqualTo(ID);
    assertThat(examRequest.getMedicalRecord()).isEqualTo(request.medicalRecord());
    assertThat(examRequest.getMedicalRequest()).isEqualTo(request.medicalRequest());
    assertThat(examRequest.getSector()).isEqualTo(request.sector());
    assertThat(examRequest.getAgreement()).isEqualTo(request.agreement());
    assertThat(examRequest.getDoctorRequestant()).isEqualTo(request.doctorRequestant());
    assertThat(examRequest.getUser()).isEqualTo(request.user());
    assertThat(examRequest.getClinicOrigin()).isEqualTo(request.clinicOrigin());
    assertThat(examRequest.getCityOrigin()).isEqualTo(request.cityOrigin());
    assertThat(examRequest.getPatient().getId()).isEqualTo(request.patientId());
    assertThat(examRequest.getRequestDate()).isEqualTo(request.requestDate());
    assertThat(examRequest.getAchievementDate()).isEqualTo(request.achievementDate());
  }

  @Test
  @DisplayName("Given a null exam request request when mapping to domain then return null")
  void givenNullExamRequestRequest_whenToDomain_thenReturnNull() {

    ExamRequest examRequest = ExamRequestMapper.toDomain((ExamRequestRequest) null);

    assertThat(examRequest).isNull();
  }

  @Test
  @DisplayName("Given an exam request when mapping to response then map every field")
  void givenExamRequest_whenToResponse_thenMapEveryField() {
    ExamRequest examRequest = Instancio.create(ExamRequest.class);
    examRequest.setPatient(Patient.builder().id(ID).build());

    ExamRequestResponse response = ExamRequestMapper.toResponse(examRequest);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(examRequest.getId());
    assertThat(response.medicalRecord()).isEqualTo(examRequest.getMedicalRecord());
    assertThat(response.medicalRequest()).isEqualTo(examRequest.getMedicalRequest());
    assertThat(response.sector()).isEqualTo(examRequest.getSector());
    assertThat(response.agreement()).isEqualTo(examRequest.getAgreement());
    assertThat(response.doctorRequestant()).isEqualTo(examRequest.getDoctorRequestant());
    assertThat(response.user()).isEqualTo(examRequest.getUser());
    assertThat(response.clinicOrigin()).isEqualTo(examRequest.getClinicOrigin());
    assertThat(response.cityOrigin()).isEqualTo(examRequest.getCityOrigin());
    assertThat(response.patientId()).isEqualTo(ID);
    assertThat(response.requestDate()).isEqualTo(examRequest.getRequestDate());
    assertThat(response.achievementDate()).isEqualTo(examRequest.getAchievementDate());
    assertThat(response.createdAt()).isEqualTo(examRequest.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(examRequest.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(examRequest.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(examRequest.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null exam request when mapping to response then return null")
  void givenNullExamRequest_whenToResponse_thenReturnNull() {

    ExamRequestResponse response = ExamRequestMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
