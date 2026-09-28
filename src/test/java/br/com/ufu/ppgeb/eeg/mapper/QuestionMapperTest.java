package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.QuestionAnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.dto.QuestionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionResponse;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionMapperTest {

  private static final Long ID = 1001L;
  private static final Long FIRST_OPTION_ID = 11L;
  private static final Long SECOND_OPTION_ID = 12L;
  private static final Long FIRST_SORT_ORDER = 1L;
  private static final Long SECOND_SORT_ORDER = 2L;
  private static final Long FIRST_ANSWER_OPTION_ID = 31L;
  private static final Long SECOND_ANSWER_OPTION_ID = 32L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a question request when mapping to domain then map every field")
  void givenQuestionRequest_whenToDomain_thenMapEveryField() {
    QuestionRequest request = Instancio.create(QuestionRequest.class);

    Question question = QuestionMapper.toDomain(request);

    assertThat(question)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(question.getId()).isEqualTo(request.id());
    assertThat(question.getText()).isEqualTo(request.text());
    assertThat(question.getDescription()).isEqualTo(request.description());
    assertThat(question.getQuestionType()).isEqualTo(request.questionType());
    assertThat(question.getActive()).isEqualTo(request.active());
    assertThat(question.getQuestionAnswerOptions()).hasSize(request.options().size());
    for (int i = 0; i < request.options().size(); i++) {
      assertThat(question.getQuestionAnswerOptions().get(i).getAnswerOption().getId())
          .isEqualTo(request.options().get(i).answerOptionId());
      assertThat(question.getQuestionAnswerOptions().get(i).getSortOrder())
          .isEqualTo(request.options().get(i).sortOrder());
      assertThat(question.getQuestionAnswerOptions().get(i).getQuestion())
          .isEqualTo(question);
    }
  }

  @Test
  @DisplayName("Given a question request and an id when mapping to domain then map every field with the id")
  void givenQuestionRequestAndId_whenToDomain_thenMapEveryFieldWithTheId() {
    QuestionRequest request = Instancio.create(QuestionRequest.class);

    Question question = QuestionMapper.toDomain(request, ID);

    assertThat(question)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(question.getId()).isEqualTo(ID);
  }

  @Test
  @DisplayName("Given a null question request when mapping to domain then return null")
  void givenNullQuestionRequest_whenToDomain_thenReturnNull() {

    Question question = QuestionMapper.toDomain((QuestionRequest) null);

    assertThat(question).isNull();
  }

  @Test
  @DisplayName("Given a question id when building reference then return entity with only the id")
  void givenQuestionId_whenBuildingReference_thenReturnEntityWithOnlyTheId() {

    Question question = QuestionMapper.buildReference(ID);

    assertThat(question.getId()).isEqualTo(ID);
    assertThat(question.getText()).isNull();
  }

  @Test
  @DisplayName("Given a question with linked options when mapping to response then map option fields")
  void givenQuestionWithLinkedOptions_whenToResponse_thenMapOptionFields() {
    Question question = createQuestionWithTwoOptions();

    QuestionResponse response = QuestionMapper.toResponse(question);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.options()).extracting(QuestionAnswerOptionResponse::id)
        .containsExactly(FIRST_OPTION_ID, SECOND_OPTION_ID);
    assertThat(response.options()).extracting(QuestionAnswerOptionResponse::questionId)
        .containsOnly(ID);
    assertThat(response.options()).extracting(QuestionAnswerOptionResponse::sortOrder)
        .containsExactly(FIRST_SORT_ORDER, SECOND_SORT_ORDER);
    assertThat(response.options()).extracting(option -> option.answerOption().id())
        .containsExactly(FIRST_ANSWER_OPTION_ID, SECOND_ANSWER_OPTION_ID);
  }

  private Question createQuestionWithTwoOptions() {
    Question question = Instancio.of(Question.class)
        .ignore(field(Question::getQuestionAnswerOptions))
        .set(field(Question::getId), ID)
        .create();
    QuestionAnswerOption first = createOptionLink(question, FIRST_OPTION_ID, FIRST_SORT_ORDER,
        FIRST_ANSWER_OPTION_ID);
    QuestionAnswerOption second = createOptionLink(question, SECOND_OPTION_ID, SECOND_SORT_ORDER,
        SECOND_ANSWER_OPTION_ID);
    question.setQuestionAnswerOptions(List.of(first, second));
    return question;
  }

  private QuestionAnswerOption createOptionLink(Question question, Long id, Long sortOrder,
      Long answerOptionId) {
    AnswerOption answerOption = Instancio.of(AnswerOption.class)
        .set(field(AnswerOption::getId), answerOptionId)
        .create();
    return QuestionAnswerOption.builder()
        .id(id)
        .question(question)
        .answerOption(answerOption)
        .sortOrder(sortOrder)
        .build();
  }

  @Test
  @DisplayName("Given a null question when mapping to response then return null")
  void givenNullQuestion_whenToResponse_thenReturnNull() {

    QuestionResponse response = QuestionMapper.toResponse(null);

    assertThat(response).isNull();
  }
}