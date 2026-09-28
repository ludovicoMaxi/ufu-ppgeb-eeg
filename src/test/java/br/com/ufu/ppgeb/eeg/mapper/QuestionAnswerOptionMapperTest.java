package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import br.com.ufu.ppgeb.eeg.dto.QuestionAnswerOptionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionAnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionAnswerOptionMapperTest {

  private static final Long QUESTION_ID = 1001L;
  private static final Long LINK_ID = 77L;
  private static final Long ANSWER_OPTION_ID = 55L;
  private static final Long SORT_ORDER = 1L;
  private static final String ID_FIELD = "id";
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a request and a question when mapping to domain then map every field")
  void givenRequestAndQuestion_whenToDomain_thenMapEveryField() {
    QuestionAnswerOptionRequest request = Instancio.create(QuestionAnswerOptionRequest.class);
    Question question = Question.builder().id(QUESTION_ID).build();

    QuestionAnswerOption link = QuestionAnswerOptionMapper.toDomain(request, question);

    assertThat(link)
        .hasNoNullFieldsOrPropertiesExcept(ID_FIELD, CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(link.getQuestion()).isEqualTo(question);
    assertThat(link.getAnswerOption()).isNotNull();
    assertThat(link.getAnswerOption().getId()).isEqualTo(request.answerOptionId());
    assertThat(link.getSortOrder()).isEqualTo(request.sortOrder());
  }

  @Test
  @DisplayName("Given a null request when mapping to domain then return null")
  void givenNullRequest_whenToDomain_thenReturnNull() {

    QuestionAnswerOption link = QuestionAnswerOptionMapper.toDomain(null, Question.builder().build());

    assertThat(link).isNull();
  }

  @Test
  @DisplayName("Given a link when mapping to response then map every field")
  void givenLink_whenToResponse_thenMapEveryField() {
    QuestionAnswerOption stored = createStoredLink();

    QuestionAnswerOptionResponse response = QuestionAnswerOptionMapper.toResponse(stored);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(LINK_ID);
    assertThat(response.questionId()).isEqualTo(QUESTION_ID);
    assertThat(response.answerOption().id()).isEqualTo(ANSWER_OPTION_ID);
    assertThat(response.sortOrder()).isEqualTo(SORT_ORDER);
    assertThat(response.createdAt()).isEqualTo(stored.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(stored.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(stored.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(stored.getUpdatedBy());
  }

  private QuestionAnswerOption createStoredLink() {
    AnswerOption answerOption = Instancio.of(AnswerOption.class)
        .set(field(AnswerOption::getId), ANSWER_OPTION_ID)
        .create();
    return Instancio.of(QuestionAnswerOption.class)
        .set(field(QuestionAnswerOption::getId), LINK_ID)
        .set(field(QuestionAnswerOption::getQuestion),
            Question.builder().id(QUESTION_ID).build())
        .set(field(QuestionAnswerOption::getAnswerOption), answerOption)
        .set(field(QuestionAnswerOption::getSortOrder), SORT_ORDER)
        .create();
  }

  @Test
  @DisplayName("Given a null link when mapping to response then return null")
  void givenNullLink_whenToResponse_thenReturnNull() {

    QuestionAnswerOptionResponse response = QuestionAnswerOptionMapper.toResponse(null);

    assertThat(response).isNull();
  }
}