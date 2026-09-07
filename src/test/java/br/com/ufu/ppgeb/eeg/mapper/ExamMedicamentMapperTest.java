package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentResponse;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamMedicamentMapperTest {

  private static final Long EXAM_ID = 2002L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a request and an exam id when mapping to domain then map every field")
  void givenExamMedicamentRequestAndExamId_whenToDomain_thenMapEveryFieldWithTheExamId() {
    ExamMedicamentRequest request = Instancio.create(ExamMedicamentRequest.class);

    ExamMedicament examMedicament = ExamMedicamentMapper.toDomain(request, EXAM_ID);

    assertThat(examMedicament)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(examMedicament.getId()).isEqualTo(request.id());
    assertThat(examMedicament.getExam()).isNotNull();
    assertThat(examMedicament.getExam().getId()).isEqualTo(EXAM_ID);
    assertThat(examMedicament.getMedicament()).isNotNull();
    assertThat(examMedicament.getMedicament().getId()).isEqualTo(request.medicament().id());
    assertThat(examMedicament.getMedicament().getName()).isEqualTo(request.medicament().name());
    assertThat(examMedicament.getMedicament().getDescription())
        .isEqualTo(request.medicament().description());
    assertThat(examMedicament.getAmount()).isEqualTo(request.amount());
    assertThat(examMedicament.getUnit()).isNotNull();
    assertThat(examMedicament.getUnit().getId()).isEqualTo(request.unitId());
  }

  @Test
  @DisplayName("Given a null exam medicament request when mapping to domain then return null")
  void givenNullExamMedicamentRequest_whenToDomain_thenReturnNull() {

    ExamMedicament examMedicament = ExamMedicamentMapper.toDomain(null, EXAM_ID);

    assertThat(examMedicament).isNull();
  }

  @Test
  @DisplayName("Given an exam medicament when mapping to response then map every field")
  void givenExamMedicament_whenToResponse_thenMapEveryField() {
    ExamMedicament examMedicament = Instancio.of(ExamMedicament.class)
        .set(field(ExamMedicament::getExam), Exam.builder().id(EXAM_ID).build())
        .create();

    ExamMedicamentResponse response = ExamMedicamentMapper.toResponse(examMedicament);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(examMedicament.getId());
    assertThat(response.examId()).isEqualTo(EXAM_ID);
    assertThat(response.medicament().id()).isEqualTo(examMedicament.getMedicament().getId());
    assertThat(response.amount()).isEqualTo(examMedicament.getAmount());
    assertThat(response.unit().id()).isEqualTo(examMedicament.getUnit().getId());
    assertThat(response.createdAt()).isEqualTo(examMedicament.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(examMedicament.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(examMedicament.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(examMedicament.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null exam medicament when mapping to response then return null")
  void givenNullExamMedicament_whenToResponse_thenReturnNull() {

    ExamMedicamentResponse response = ExamMedicamentMapper.toResponse(null);

    assertThat(response).isNull();
  }
}