package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.QuestionnaireQuestionResponse;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireResponse;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionnaireMapperTest {

  private static final Long ID = 1001L;
  private static final Long FIRST_LINK_ID = 21L;
  private static final Long SECOND_LINK_ID = 22L;
  private static final Long FIRST_SORT_ORDER = 1L;
  private static final Long SECOND_SORT_ORDER = 2L;
  private static final Long FIRST_QUESTION_ID = 41L;
  private static final Long SECOND_QUESTION_ID = 42L;
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a questionnaire request when mapping to domain then map every field")
  void givenQuestionnaireRequest_whenToDomain_thenMapEveryField() {
    QuestionnaireRequest request = Instancio.create(QuestionnaireRequest.class);

    Questionnaire questionnaire = QuestionnaireMapper.toDomain(request);

    assertThat(questionnaire)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(questionnaire.getId()).isEqualTo(request.id());
    assertThat(questionnaire.getTitle()).isEqualTo(request.title());
    assertThat(questionnaire.getDescription()).isEqualTo(request.description());
    assertThat(questionnaire.getQuestionnaireQuestions())
        .hasSize(request.questions().size());
    for (int i = 0; i < request.questions().size(); i++) {
      assertThat(questionnaire.getQuestionnaireQuestions().get(i).getQuestion().getId())
          .isEqualTo(request.questions().get(i).questionId());
      assertThat(questionnaire.getQuestionnaireQuestions().get(i).getSortOrder())
          .isEqualTo(request.questions().get(i).sortOrder());
      assertThat(questionnaire.getQuestionnaireQuestions().get(i).getQuestionnaire())
          .isEqualTo(questionnaire);
    }
  }

  @Test
  @DisplayName("Given a questionnaire request and an id when mapping to domain then map every field with the id")
  void givenQuestionnaireRequestAndId_whenToDomain_thenMapEveryFieldWithTheId() {
    QuestionnaireRequest request = Instancio.create(QuestionnaireRequest.class);

    Questionnaire questionnaire = QuestionnaireMapper.toDomain(request, ID);

    assertThat(questionnaire)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(questionnaire.getId()).isEqualTo(ID);
  }

  @Test
  @DisplayName("Given a null questionnaire request when mapping to domain then return null")
  void givenNullQuestionnaireRequest_whenToDomain_thenReturnNull() {

    Questionnaire questionnaire = QuestionnaireMapper.toDomain((QuestionnaireRequest) null);

    assertThat(questionnaire).isNull();
  }

  @Test
  @DisplayName("Given a questionnaire id when building reference then return entity with only the id")
  void givenQuestionnaireId_whenBuildingReference_thenReturnEntityWithOnlyTheId() {

    Questionnaire questionnaire = QuestionnaireMapper.buildReference(ID);

    assertThat(questionnaire.getId()).isEqualTo(ID);
    assertThat(questionnaire.getTitle()).isNull();
  }

  @Test
  @DisplayName("Given a questionnaire with linked questions when mapping to response then map every field")
  void givenQuestionnaireWithLinkedQuestions_whenToResponse_thenMapEveryField() {
    Questionnaire questionnaire = createQuestionnaireWithTwoQuestions();

    QuestionnaireResponse response = QuestionnaireMapper.toResponse(questionnaire);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(ID);
    assertThat(response.title()).isEqualTo(questionnaire.getTitle());
    assertThat(response.description()).isEqualTo(questionnaire.getDescription());
    assertThat(response.questions()).extracting(QuestionnaireQuestionResponse::id)
        .containsExactly(FIRST_LINK_ID, SECOND_LINK_ID);
    assertThat(response.questions()).extracting(QuestionnaireQuestionResponse::questionnaireId)
        .containsOnly(ID);
    assertThat(response.questions()).extracting(QuestionnaireQuestionResponse::sortOrder)
        .containsExactly(FIRST_SORT_ORDER, SECOND_SORT_ORDER);
    assertThat(response.questions()).extracting(question -> question.question().id())
        .containsExactly(FIRST_QUESTION_ID, SECOND_QUESTION_ID);
    assertThat(response.createdAt()).isEqualTo(questionnaire.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(questionnaire.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(questionnaire.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(questionnaire.getUpdatedBy());
  }

  private Questionnaire createQuestionnaireWithTwoQuestions() {
    Questionnaire questionnaire = Instancio.of(Questionnaire.class)
        .ignore(field(Questionnaire::getQuestionnaireQuestions))
        .set(field(Questionnaire::getId), ID)
        .create();
    QuestionnaireQuestion first = createQuestionLink(questionnaire, FIRST_LINK_ID, FIRST_SORT_ORDER,
        FIRST_QUESTION_ID);
    QuestionnaireQuestion second = createQuestionLink(questionnaire, SECOND_LINK_ID,
        SECOND_SORT_ORDER, SECOND_QUESTION_ID);
    questionnaire.setQuestionnaireQuestions(List.of(first, second));
    return questionnaire;
  }

  private QuestionnaireQuestion createQuestionLink(Questionnaire questionnaire, Long id,
      Long sortOrder, Long questionId) {
    Question question = Instancio.of(Question.class)
        .ignore(field(Question::getQuestionAnswerOptions))
        .set(field(Question::getId), questionId)
        .create();
    return QuestionnaireQuestion.builder()
        .id(id)
        .questionnaire(questionnaire)
        .question(question)
        .sortOrder(sortOrder)
        .build();
  }

  @Test
  @DisplayName("Given a null questionnaire when mapping to response then return null")
  void givenNullQuestionnaire_whenToResponse_thenReturnNull() {

    QuestionnaireResponse response = QuestionnaireMapper.toResponse(null);

    assertThat(response).isNull();
  }
}