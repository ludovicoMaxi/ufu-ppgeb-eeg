package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentResponse;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamEquipmentMapperTest {

  private static final Long EXAM_ID = 2002L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a request and an exam id when mapping to entity then map every field")
  void givenExamEquipmentRequestAndExamId_whenToEntity_thenMapEveryFieldWithTheExamId() {
    ExamEquipmentRequest request = Instancio.create(ExamEquipmentRequest.class);

    ExamEquipment examEquipment = ExamEquipmentMapper.toEntity(request, EXAM_ID);

    assertThat(examEquipment)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(examEquipment.getId()).isEqualTo(request.id());
    assertThat(examEquipment.getExam()).isNotNull();
    assertThat(examEquipment.getExam().getId()).isEqualTo(EXAM_ID);
    assertThat(examEquipment.getEquipment()).isNotNull();
    assertThat(examEquipment.getEquipment().getId()).isEqualTo(request.equipment().id());
    assertThat(examEquipment.getEquipment().getName()).isEqualTo(request.equipment().name());
    assertThat(examEquipment.getEquipment().getDescription())
        .isEqualTo(request.equipment().description());
    assertThat(examEquipment.getAmount()).isEqualTo(request.amount());
    assertThat(examEquipment.getUnit()).isNotNull();
    assertThat(examEquipment.getUnit().getId()).isEqualTo(request.unitId());
  }

  @Test
  @DisplayName("Given a null exam equipment request when mapping to entity then return null")
  void givenNullExamEquipmentRequest_whenToEntity_thenReturnNull() {

    ExamEquipment examEquipment = ExamEquipmentMapper.toEntity(null, EXAM_ID);

    assertThat(examEquipment).isNull();
  }

  @Test
  @DisplayName("Given an exam equipment when mapping to response then map every field")
  void givenExamEquipment_whenToResponse_thenMapEveryField() {
    ExamEquipment examEquipment = Instancio.of(ExamEquipment.class)
        .set(field(ExamEquipment::getExam), Exam.builder().id(EXAM_ID).build())
        .create();

    ExamEquipmentResponse response = ExamEquipmentMapper.toResponse(examEquipment);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(examEquipment.getId());
    assertThat(response.examId()).isEqualTo(EXAM_ID);
    assertThat(response.equipment().id()).isEqualTo(examEquipment.getEquipment().getId());
    assertThat(response.amount()).isEqualTo(examEquipment.getAmount());
    assertThat(response.unit().id()).isEqualTo(examEquipment.getUnit().getId());
    assertThat(response.createdAt()).isEqualTo(examEquipment.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(examEquipment.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(examEquipment.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(examEquipment.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null exam equipment when mapping to response then return null")
  void givenNullExamEquipment_whenToResponse_thenReturnNull() {

    ExamEquipmentResponse response = ExamEquipmentMapper.toResponse(null);

    assertThat(response).isNull();
  }
}