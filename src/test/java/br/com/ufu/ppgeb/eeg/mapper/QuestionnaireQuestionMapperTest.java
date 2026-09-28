package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import br.com.ufu.ppgeb.eeg.dto.QuestionnaireQuestionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireQuestionResponse;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionnaireQuestionMapperTest {

  private static final Long QUESTIONNAIRE_ID = 1001L;
  private static final Long QUESTION_ID = 1002L;
  private static final Long LINK_ID = 88L;
  private static final Long SORT_ORDER = 1L;
  private static final String ID_FIELD = "id";
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a request and a questionnaire when mapping to domain then map every field")
  void givenRequestAndQuestionnaire_whenToDomain_thenMapEveryField() {
    QuestionnaireQuestionRequest request = Instancio.create(QuestionnaireQuestionRequest.class);
    Questionnaire questionnaire = Questionnaire.builder().id(QUESTIONNAIRE_ID).build();

    QuestionnaireQuestion link = QuestionnaireQuestionMapper.toDomain(request, questionnaire);

    assertThat(link)
        .hasNoNullFieldsOrPropertiesExcept(ID_FIELD, CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(link.getQuestionnaire()).isEqualTo(questionnaire);
    assertThat(link.getQuestion()).isNotNull();
    assertThat(link.getQuestion().getId()).isEqualTo(request.questionId());
    assertThat(link.getSortOrder()).isEqualTo(request.sortOrder());
  }

  @Test
  @DisplayName("Given a null request when mapping to domain then return null")
  void givenNullRequest_whenToDomain_thenReturnNull() {

    QuestionnaireQuestion link = QuestionnaireQuestionMapper.toDomain(null,
        Questionnaire.builder().build());

    assertThat(link).isNull();
  }

  @Test
  @DisplayName("Given a link when mapping to response then map every field")
  void givenLink_whenToResponse_thenMapEveryField() {
    QuestionnaireQuestion stored = createStoredLink();

    QuestionnaireQuestionResponse response = QuestionnaireQuestionMapper.toResponse(stored);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(LINK_ID);
    assertThat(response.questionnaireId()).isEqualTo(QUESTIONNAIRE_ID);
    assertThat(response.question().id()).isEqualTo(QUESTION_ID);
    assertThat(response.sortOrder()).isEqualTo(SORT_ORDER);
    assertThat(response.createdAt()).isEqualTo(stored.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(stored.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(stored.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(stored.getUpdatedBy());
  }

  private QuestionnaireQuestion createStoredLink() {
    Question question = Instancio.of(Question.class)
        .ignore(field(Question::getQuestionAnswerOptions))
        .set(field(Question::getId), QUESTION_ID)
        .create();
    return Instancio.of(QuestionnaireQuestion.class)
        .set(field(QuestionnaireQuestion::getId), LINK_ID)
        .set(field(QuestionnaireQuestion::getQuestionnaire),
            Questionnaire.builder().id(QUESTIONNAIRE_ID).build())
        .set(field(QuestionnaireQuestion::getQuestion), question)
        .set(field(QuestionnaireQuestion::getSortOrder), SORT_ORDER)
        .create();
  }

  @Test
  @DisplayName("Given a null link when mapping to response then return null")
  void givenNullLink_whenToResponse_thenReturnNull() {

    QuestionnaireQuestionResponse response = QuestionnaireQuestionMapper.toResponse(null);

    assertThat(response).isNull();
  }
}