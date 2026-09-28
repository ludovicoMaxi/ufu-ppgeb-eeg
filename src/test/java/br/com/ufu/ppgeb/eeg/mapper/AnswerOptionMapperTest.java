package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import br.com.ufu.ppgeb.eeg.dto.AnswerOptionRequest;
import br.com.ufu.ppgeb.eeg.dto.AnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerOptionMapperTest {

  private static final Long ID = 1001L;
  private static final String ID_FIELD = "id";
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given an answer option request when mapping to domain then map every field keeping id null")
  void givenAnswerOptionRequest_whenToDomain_thenMapEveryFieldKeepingIdNull() {
    AnswerOptionRequest request = Instancio.of(AnswerOptionRequest.class)
        .set(field(AnswerOptionRequest::id), null)
        .create();

    AnswerOption answerOption = AnswerOptionMapper.toDomain(request);

    assertThat(answerOption)
        .hasNoNullFieldsOrPropertiesExcept(ID_FIELD, CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(answerOption.getId()).isNull();
    assertThat(answerOption.getText()).isEqualTo(request.text());
    assertThat(answerOption.getActive()).isEqualTo(request.active());
  }

  @Test
  @DisplayName("Given an answer option request and an id when mapping to domain then map every field with the id")
  void givenAnswerOptionRequestAndId_whenToDomain_thenMapEveryFieldWithTheId() {
    AnswerOptionRequest request = Instancio.create(AnswerOptionRequest.class);

    AnswerOption answerOption = AnswerOptionMapper.toDomain(request, ID);

    assertThat(answerOption)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(answerOption.getId()).isEqualTo(ID);
    assertThat(answerOption.getText()).isEqualTo(request.text());
    assertThat(answerOption.getActive()).isEqualTo(request.active());
  }

  @Test
  @DisplayName("Given a null answer option request when mapping to domain then return null")
  void givenNullAnswerOptionRequest_whenToDomain_thenReturnNull() {

    AnswerOption answerOption = AnswerOptionMapper.toDomain((AnswerOptionRequest) null);

    assertThat(answerOption).isNull();
  }

  @Test
  @DisplayName("Given an answer option id when building reference then return entity with only the id")
  void givenAnswerOptionId_whenBuildingReference_thenReturnEntityWithOnlyTheId() {

    AnswerOption answerOption = AnswerOptionMapper.buildReference(ID);

    assertThat(answerOption.getId()).isEqualTo(ID);
    assertThat(answerOption.getText()).isNull();
  }

  @Test
  @DisplayName("Given a null answer option id when building reference then return null")
  void givenNullAnswerOptionId_whenBuildingReference_thenReturnNull() {

    AnswerOption answerOption = AnswerOptionMapper.buildReference(null);

    assertThat(answerOption).isNull();
  }

  @Test
  @DisplayName("Given an answer option when mapping to response then map every field")
  void givenAnswerOption_whenToResponse_thenMapEveryField() {
    AnswerOption answerOption = Instancio.create(AnswerOption.class);

    AnswerOptionResponse response = AnswerOptionMapper.toResponse(answerOption);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(answerOption.getId());
    assertThat(response.text()).isEqualTo(answerOption.getText());
    assertThat(response.active()).isEqualTo(answerOption.getActive());
    assertThat(response.createdAt()).isEqualTo(answerOption.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(answerOption.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(answerOption.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(answerOption.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null answer option when mapping to response then return null")
  void givenNullAnswerOption_whenToResponse_thenReturnNull() {

    AnswerOptionResponse response = AnswerOptionMapper.toResponse(null);

    assertThat(response).isNull();
  }
}