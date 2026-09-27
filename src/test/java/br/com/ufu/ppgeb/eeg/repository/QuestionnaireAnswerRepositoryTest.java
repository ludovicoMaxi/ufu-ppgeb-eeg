package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswerOption;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(AuditingConfig.class)
class QuestionnaireAnswerRepositoryTest {

  private static final Long QUESTIONNAIRE_ID = 1001L;
  private static final Long OBJECT_ID_1 = 1L;
  private static final Long OBJECT_ID_2 = 2L;
  private static final Long ANSWER_OPTION_ID = 1001L;
  private static final String ANSWER_OPTION_TEXT = "Sim";
  private static final String QUESTION_TEXT = "Houve melhora no quadro clínico?";
  private static final String FREE_TEXT = "Paciente adormeceu ao final do registro.";
  private static final String USERNAME = "joaol";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Autowired
  private QuestionnaireAnswerRepository questionnaireAnswerRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearAnswerTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM QUESTIONNAIRE_ANSWER_OPTION").executeUpdate();
    em.createNativeQuery("DELETE FROM QUESTIONNAIRE_ANSWER").executeUpdate();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
  }

  @BeforeEach
  void setUpAuthentication() {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new UsernamePasswordAuthenticationToken(USERNAME, "123",
        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    SecurityContextHolder.setContext(context);
  }

  @AfterEach
  void tearDownAuthentication() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Given valid answer when save then generate id and fill auditing fields")
  void givenValidAnswer_whenSave_thenGenerateIdAndFillAuditingFields() {
    Questionnaire questionnaire = loadQuestionnaire();
    Question question = persistQuestion();
    AnswerOption answerOption = AnswerOption.builder()
        .id(ANSWER_OPTION_ID)
        .text(ANSWER_OPTION_TEXT)
        .active(Boolean.TRUE)
        .build();
    var answer = createAnswer(questionnaire, question, OBJECT_ID_1, answerOption);
    assertThat(answer.getId()).isNull();

    QuestionnaireAnswer saved = questionnaireAnswerRepository.save(answer);

    assertThat(saved).hasNoNullFieldsOrPropertiesExcept(UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getQuestionnaire().getId()).isEqualTo(QUESTIONNAIRE_ID);
    assertThat(saved.getQuestion().getId()).isEqualTo(question.getId());
    assertThat(saved.getObjectType()).isEqualTo(ObjectType.EXAM.getId());
    assertThat(saved.getObjectId()).isEqualTo(OBJECT_ID_1);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
    assertThat(saved.getAnswerOptions()).hasSize(1);
    assertThat(saved.getAnswerOptions().get(0).getAnswerOption().getId())
        .isEqualTo(answerOption.getId());
  }

  @Test
  @DisplayName("Given saved answers when findByQuestionnaireAndObjectTypeAndObjectId then return only matches")
  void givenSavedAnswers_whenFindByQuestionnaireAndObjectTypeAndObjectId_thenReturnOnlyMatches() {
    Questionnaire questionnaire = loadQuestionnaire();
    Question question = persistQuestion();
    AnswerOption answerOption = AnswerOption.builder()
        .id(ANSWER_OPTION_ID)
        .text(ANSWER_OPTION_TEXT)
        .active(Boolean.TRUE)
        .build();
    questionnaireAnswerRepository.save(
        createAnswer(questionnaire, question, OBJECT_ID_1, answerOption));
    questionnaireAnswerRepository.save(
        createAnswer(questionnaire, question, OBJECT_ID_2, answerOption));

    List<QuestionnaireAnswer> result = questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire,
            ObjectType.EXAM.getId(), OBJECT_ID_1);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getObjectId()).isEqualTo(OBJECT_ID_1);
    assertThat(result.get(0).getQuestion().getId()).isEqualTo(question.getId());
  }

  @Test
  @DisplayName("Given no answers when findByQuestionnaireAndObjectTypeAndObjectId then return empty")
  void givenNoAnswers_whenFindByQuestionnaireAndObjectTypeAndObjectId_thenReturnEmpty() {
    Questionnaire questionnaire = loadQuestionnaire();

    List<QuestionnaireAnswer> result = questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire,
            ObjectType.EXAM.getId(), OBJECT_ID_1);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Given saved answer when existsByQuestionnaireAndObjectTypeAndObjectId then return true")
  void givenSavedAnswer_whenExistsByQuestionnaireAndObjectTypeAndObjectId_thenReturnTrue() {
    Questionnaire questionnaire = loadQuestionnaire();
    Question question = persistQuestion();
    questionnaireAnswerRepository.save(
        createAnswer(questionnaire, question, OBJECT_ID_1,
            AnswerOption.builder().id(ANSWER_OPTION_ID).text(ANSWER_OPTION_TEXT).active(Boolean.TRUE).build()));

    boolean result = questionnaireAnswerRepository
        .existsByQuestionnaireAndObjectTypeAndObjectId(questionnaire,
            ObjectType.EXAM.getId(), OBJECT_ID_1);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given saved answer when existsByQuestionnaire then return true")
  void givenSavedAnswer_whenExistsByQuestionnaire_thenReturnTrue() {
    Questionnaire questionnaire = loadQuestionnaire();
    Question question = persistQuestion();
    questionnaireAnswerRepository.save(
        createAnswer(questionnaire, question, OBJECT_ID_1,
            AnswerOption.builder().id(ANSWER_OPTION_ID).text(ANSWER_OPTION_TEXT).active(Boolean.TRUE).build()));

    boolean result = questionnaireAnswerRepository.existsByQuestionnaire(questionnaire);

    assertThat(result).isTrue();
  }

  private Questionnaire loadQuestionnaire() {
    return testEntityManager.find(Questionnaire.class, QUESTIONNAIRE_ID);
  }

  private Question persistQuestion() {
    Question question = Question.builder()
        .text(QUESTION_TEXT)
        .questionType(QuestionType.SINGLE_SELECT)
        .active(Boolean.TRUE)
        .build();
    return testEntityManager.persistAndFlush(question);
  }

  private QuestionnaireAnswer createAnswer(Questionnaire questionnaire, Question question,
      Long objectId, AnswerOption answerOption) {

    QuestionnaireAnswer answer = QuestionnaireAnswer.builder()
        .questionnaire(questionnaire)
        .question(question)
        .objectType(ObjectType.EXAM.getId())
        .objectId(objectId)
        .freeText(FREE_TEXT)
        .build();
    QuestionnaireAnswerOption option = QuestionnaireAnswerOption.builder()
        .questionnaireAnswer(answer)
        .answerOption(answerOption)
        .build();
    answer.setAnswerOptions(List.of(option));
    return answer;
  }
}