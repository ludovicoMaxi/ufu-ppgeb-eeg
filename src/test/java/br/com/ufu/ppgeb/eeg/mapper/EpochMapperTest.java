package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.EpochRequest;
import br.com.ufu.ppgeb.eeg.dto.EpochResponse;
import br.com.ufu.ppgeb.eeg.model.Epoch;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EpochMapperTest {

  private static final Long EXAM_ID = 2002L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given an epoch request and an exam id when mapping to entity then map every field with the exam id")
  void givenEpochRequestAndExamId_whenToEntity_thenMapEveryFieldWithTheExamId() {
    EpochRequest request = Instancio.create(EpochRequest.class);

    Epoch epoch = EpochMapper.toEntity(request, EXAM_ID);

    assertThat(epoch)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(epoch.getId()).isEqualTo(request.id());
    assertThat(epoch.getExamId()).isEqualTo(EXAM_ID);
    assertThat(epoch.getStartTime()).isEqualTo(request.startTime());
    assertThat(epoch.getDuration()).isEqualTo(request.duration());
    assertThat(epoch.getDescription()).isEqualTo(request.description());
  }

  @Test
  @DisplayName("Given a null epoch request when mapping to entity then return null")
  void givenNullEpochRequest_whenToEntity_thenReturnNull() {

    Epoch epoch = EpochMapper.toEntity((EpochRequest) null, EXAM_ID);

    assertThat(epoch).isNull();
  }

  @Test
  @DisplayName("Given an epoch when mapping to response then map every field")
  void givenEpoch_whenToResponse_thenMapEveryField() {
    Epoch epoch = Instancio.create(Epoch.class);

    EpochResponse response = EpochMapper.toResponse(epoch);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(epoch.getId());
    assertThat(response.examId()).isEqualTo(epoch.getExamId());
    assertThat(response.startTime()).isEqualTo(epoch.getStartTime());
    assertThat(response.duration()).isEqualTo(epoch.getDuration());
    assertThat(response.description()).isEqualTo(epoch.getDescription());
    assertThat(response.createdAt()).isEqualTo(epoch.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(epoch.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(epoch.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(epoch.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null epoch when mapping to response then return null")
  void givenNullEpoch_whenToResponse_thenReturnNull() {

    EpochResponse response = EpochMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
