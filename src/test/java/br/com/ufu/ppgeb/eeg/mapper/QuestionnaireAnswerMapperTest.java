
package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.AnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerResponse;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswerOption;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionnaireAnswerMapperTest {

  private static final Long QUESTIONNAIRE_ID = 1001L;
  private static final Long QUESTION_ID = 1002L;
  private static final Long OBJECT_TYPE = 2L;
  private static final Long OBJECT_ID = 2002L;
  private static final Long FIRST_OPTION_ID = 31L;
  private static final Long SECOND_OPTION_ID = 32L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a request when mapping to domain then map every field")
  void givenRequest_whenToDomain_thenMapEveryField() {
    QuestionnaireAnswerRequest request = Instancio.create(QuestionnaireAnswerRequest.class);

    QuestionnaireAnswer answer = QuestionnaireAnswerMapper.toDomain(request,
        QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID);

    assertThat(answer)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(answer.getId()).isEqualTo(request.id());
    assertThat(answer.getQuestionnaire()).isNotNull();
    assertThat(answer.getQuestionnaire().getId()).isEqualTo(QUESTIONNAIRE_ID);
    assertThat(answer.getQuestion()).isNotNull();
    assertThat(answer.getQuestion().getId()).isEqualTo(request.questionId());
    assertThat(answer.getObjectType()).isEqualTo(OBJECT_TYPE);
    assertThat(answer.getObjectId()).isEqualTo(OBJECT_ID);
    assertThat(answer.getFreeText()).isEqualTo(request.freeText());
    assertThat(answer.getAnswerOptions())
        .hasSize(request.answerOptionIds().size());
    for (int i = 0; i < request.answerOptionIds().size(); i++) {
      assertThat(answer.getAnswerOptions().get(i).getAnswerOption().getId())
          .isEqualTo(request.answerOptionIds().get(i));
      assertThat(answer.getAnswerOptions().get(i).getQuestionnaireAnswer())
          .isEqualTo(answer);
    }
  }

  @Test
  @DisplayName("Given a null request when mapping to domain then return null")
  void givenNullRequest_whenToDomain_thenReturnNull() {

    QuestionnaireAnswer answer = QuestionnaireAnswerMapper.toDomain(
        null, QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID);

    assertThat(answer).isNull();
  }

  @Test
  @DisplayName("Given an answer when mapping to response then map every field")
  void givenAnswer_whenToResponse_thenMapEveryField() {
    QuestionnaireAnswer answer = createAnswerWithTwoOptions();

    QuestionnaireAnswerResponse response = QuestionnaireAnswerMapper.toResponse(answer);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(answer.getId());
    assertThat(response.questionnaireId()).isEqualTo(QUESTIONNAIRE_ID);
    assertThat(response.questionId()).isEqualTo(QUESTION_ID);
    assertThat(response.objectType()).isEqualTo(OBJECT_TYPE);
    assertThat(response.objectId()).isEqualTo(OBJECT_ID);
    assertThat(response.freeText()).isEqualTo(answer.getFreeText());
    assertThat(response.answerOptions())
        .extracting(AnswerOptionResponse::id)
        .containsExactly(FIRST_OPTION_ID, SECOND_OPTION_ID);
    assertThat(response.createdAt()).isEqualTo(answer.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(answer.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(answer.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(answer.getUpdatedBy());
  }

  private QuestionnaireAnswer createAnswerWithTwoOptions() {
    QuestionnaireAnswer answer = Instancio.of(QuestionnaireAnswer.class)
        .set(field(QuestionnaireAnswer::getQuestionnaire),
            Questionnaire.builder().id(QUESTIONNAIRE_ID).build())
        .set(field(QuestionnaireAnswer::getQuestion),
            Question.builder().id(QUESTION_ID).build())
        .set(field(QuestionnaireAnswer::getObjectType), OBJECT_TYPE)
        .set(field(QuestionnaireAnswer::getObjectId), OBJECT_ID)
        .create();
    answer.setAnswerOptions(List.of(
        createAnswerOptionLink(answer, FIRST_OPTION_ID),
        createAnswerOptionLink(answer, SECOND_OPTION_ID)));
    return answer;
  }

  private QuestionnaireAnswerOption createAnswerOptionLink(QuestionnaireAnswer answer, Long optionId) {
    AnswerOption option = Instancio.of(AnswerOption.class)
        .set(field(AnswerOption::getId), optionId)
        .create();
    return QuestionnaireAnswerOption.builder()
        .questionnaireAnswer(answer)
        .answerOption(option)
        .build();
  }

  @Test
  @DisplayName("Given a null answer when mapping to response then return null")
  void givenNullAnswer_whenToResponse_thenReturnNull() {

    QuestionnaireAnswerResponse response = QuestionnaireAnswerMapper.toResponse(null);

    assertThat(response).isNull();
  }
}