package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.ExamRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamResponse;
import br.com.ufu.ppgeb.eeg.model.Exam;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamMapperTest {

  private static final Long ID = 1001L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given an exam request when mapping to domain then map every field")
  void givenExamRequest_whenToDomain_thenMapEveryField() {
    ExamRequest request = Instancio.create(ExamRequest.class);

    Exam exam = ExamMapper.toDomain(request);

    assertThat(exam)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(exam.getId()).isEqualTo(request.id());
    assertThat(exam.getPatient().getId()).isEqualTo(request.patientId());
    if (nonNull(request.examRequestId())) {
      assertThat(exam.getExamRequest()).isNotNull();
      assertThat(exam.getExamRequest().getId()).isEqualTo(request.examRequestId());
    }
    assertThat(exam.getAchievementDate()).isEqualTo(request.achievementDate());
    assertThat(exam.getMedicalReport()).isEqualTo(request.medicalReport());
    assertThat(exam.getConclusion()).isEqualTo(request.conclusion());
    assertThat(exam.getBed()).isEqualTo(request.bed());
    assertThat(exam.getHeight()).isEqualTo(request.height());
    assertThat(exam.getWeight()).isEqualTo(request.weight());
    assertThat(exam.getClinicalData()).isEqualTo(request.clinicalData());
    assertThat(exam.getExamMedicaments()).hasSize(request.examMedicaments().size());
    assertThat(exam.getExamEquipments()).hasSize(request.examEquipments().size());
  }

  @Test
  @DisplayName("Given an exam request and an id when mapping to domain then map every field with the id")
  void givenExamRequestAndId_whenToDomain_thenMapEveryFieldWithTheId() {
    ExamRequest request = Instancio.create(ExamRequest.class);

    Exam exam = ExamMapper.toDomain(request, ID);

    assertThat(exam)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(exam.getId()).isEqualTo(ID);
    assertThat(exam.getPatient().getId()).isEqualTo(request.patientId());
    assertThat(exam.getAchievementDate()).isEqualTo(request.achievementDate());
    assertThat(exam.getMedicalReport()).isEqualTo(request.medicalReport());
    assertThat(exam.getConclusion()).isEqualTo(request.conclusion());
    assertThat(exam.getBed()).isEqualTo(request.bed());
    assertThat(exam.getHeight()).isEqualTo(request.height());
    assertThat(exam.getWeight()).isEqualTo(request.weight());
    assertThat(exam.getClinicalData()).isEqualTo(request.clinicalData());
  }

  @Test
  @DisplayName("Given a null exam request when mapping to domain then return null")
  void givenNullExamRequest_whenToDomain_thenReturnNull() {

    Exam exam = ExamMapper.toDomain((ExamRequest) null);

    assertThat(exam).isNull();
  }

  @Test
  @DisplayName("Given an exam when mapping to response then map every field")
  void givenExam_whenToResponse_thenMapEveryField() {
    Exam exam = Instancio.create(Exam.class);

    ExamResponse response = ExamMapper.toResponse(exam);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(exam.getId());
    assertThat(response.patientId()).isEqualTo(exam.getPatient().getId());
    assertThat(response.examRequestId()).isEqualTo(exam.getExamRequest().getId());
    assertThat(response.achievementDate()).isEqualTo(exam.getAchievementDate());
    assertThat(response.medicalReport()).isEqualTo(exam.getMedicalReport());
    assertThat(response.conclusion()).isEqualTo(exam.getConclusion());
    assertThat(response.bed()).isEqualTo(exam.getBed());
    assertThat(response.height()).isEqualTo(exam.getHeight());
    assertThat(response.weight()).isEqualTo(exam.getWeight());
    assertThat(response.clinicalData()).isEqualTo(exam.getClinicalData());
    assertThat(response.examMedicaments()).hasSize(exam.getExamMedicaments().size());
    assertThat(response.examEquipments()).hasSize(exam.getExamEquipments().size());
    assertThat(response.createdAt()).isEqualTo(exam.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(exam.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(exam.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(exam.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null exam when mapping to response then return null")
  void givenNullExam_whenToResponse_thenReturnNull() {

    ExamResponse response = ExamMapper.toResponse(null);

    assertThat(response).isNull();
  }
}