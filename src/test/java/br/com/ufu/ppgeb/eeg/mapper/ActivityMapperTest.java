package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.ActivityRequest;
import br.com.ufu.ppgeb.eeg.dto.ActivityResponse;
import br.com.ufu.ppgeb.eeg.model.Activity;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ActivityMapperTest {

  private static final Long EXAM_ID = 2002L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given an activity request and an exam id when mapping to entity then map every field with the exam id")
  void givenActivityRequestAndExamId_whenToEntity_thenMapEveryFieldWithTheExamId() {
    ActivityRequest request = Instancio.create(ActivityRequest.class);

    Activity activity = ActivityMapper.toEntity(request, EXAM_ID);

    assertThat(activity)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(activity.getId()).isEqualTo(request.id());
    assertThat(activity.getExamId()).isEqualTo(EXAM_ID);
    assertThat(activity.getStartTime()).isEqualTo(request.startTime());
    assertThat(activity.getDuration()).isEqualTo(request.duration());
    assertThat(activity.getDescription()).isEqualTo(request.description());
  }

  @Test
  @DisplayName("Given an activity response when mapping to entity then map every field")
  void givenActivityResponse_whenToEntity_thenMapEveryField() {
    ActivityResponse response = Instancio.create(ActivityResponse.class);

    Activity activity = ActivityMapper.toEntity(response);

    assertThat(activity)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(activity.getId()).isEqualTo(response.id());
    assertThat(activity.getExamId()).isEqualTo(response.examId());
    assertThat(activity.getStartTime()).isEqualTo(response.startTime());
    assertThat(activity.getDuration()).isEqualTo(response.duration());
    assertThat(activity.getDescription()).isEqualTo(response.description());
  }

  @Test
  @DisplayName("Given a null activity request when mapping to entity then return null")
  void givenNullActivityRequest_whenToEntity_thenReturnNull() {

    Activity activity = ActivityMapper.toEntity((ActivityRequest) null, EXAM_ID);

    assertThat(activity).isNull();
  }

  @Test
  @DisplayName("Given an activity when mapping to response then map every field")
  void givenActivity_whenToResponse_thenMapEveryField() {
    Activity activity = Instancio.create(Activity.class);

    ActivityResponse response = ActivityMapper.toResponse(activity);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(activity.getId());
    assertThat(response.examId()).isEqualTo(activity.getExamId());
    assertThat(response.startTime()).isEqualTo(activity.getStartTime());
    assertThat(response.duration()).isEqualTo(activity.getDuration());
    assertThat(response.description()).isEqualTo(activity.getDescription());
    assertThat(response.createdAt()).isEqualTo(activity.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(activity.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(activity.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(activity.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null activity when mapping to response then return null")
  void givenNullActivity_whenToResponse_thenReturnNull() {

    ActivityResponse response = ActivityMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
