package br.com.ufu.ppgeb.eeg.repository;

import static br.com.ufu.ppgeb.eeg.repository.QuestionSpecifications.withFilters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;

import br.com.ufu.ppgeb.eeg.EntityFactory;
import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
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
class QuestionRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long SEEDED_ID = 1001L;
  private static final Long SEEDED_COLLABORATE_ID = 1002L;
  private static final String TEXT_PARTIAL = "CONSCIÊNCIA";
  private static final String TEXT_PARTIAL_COLLABORATE = "colabor";
  private static final String TEXT = "O paciente apresentou convulsão?";
  private static final String USERNAME = "joaol";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private AnswerOptionRepository answerOptionRepository;

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
  @DisplayName("Given valid question when save then generate id and fill auditing fields")
  void givenValidQuestion_whenSave_thenGenerateIdAndFillAuditingFields() {
    AnswerOption answerOption = EntityFactory.audited(AnswerOption.class)
        .set(field(AnswerOption::getText), "Sim")
        .create();
    answerOptionRepository.save(answerOption);
    Question question = createQuestion(answerOption.getId());

    assertThat(question.getId()).isNull();

    Question saved = questionRepository.save(question);

    assertThat(saved).hasNoNullFieldsOrPropertiesExcept(UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getText()).isEqualTo(TEXT);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
    assertThat(saved.getQuestionAnswerOptions()).hasSize(1);
  }

  @Test
  @DisplayName("Given existing id when findById then return the question")
  void givenExistingId_whenFindById_thenReturnTheQuestion() {
    var result = questionRepository.findById(SEEDED_ID);

    assertThat(result).isPresent();
    assertThat(result.get().getQuestionType()).isEqualTo(QuestionType.SINGLE_SELECT);
  }

  @Test
  @DisplayName("Given id filter when findAll with filters then return matching question")
  void givenIdFilter_whenFindAllWithFilters_thenReturnMatchingQuestion() {
    List<Question> result = questionRepository.findAll(
        withFilters(SEEDED_ID, null, null, null));

    assertThat(result).extracting(Question::getId).containsOnly(SEEDED_ID);
  }

  @Test
  @DisplayName("Given text filter when findAll with filters then return matching questions ignoring case")
  void givenTextFilter_whenFindAllWithFilters_thenReturnMatchingQuestionsIgnoringCase() {
    List<Question> result = questionRepository.findAll(
        withFilters(null, TEXT_PARTIAL, null, null));

    assertThat(result).extracting(Question::getId).containsOnly(SEEDED_ID);
  }

  @Test
  @DisplayName("Given question type filter when findAll with filters then return only matching questions")
  void givenQuestionTypeFilter_whenFindAllWithFilters_thenReturnOnlyMatchingQuestions() {
    List<Question> result = questionRepository.findAll(
        withFilters(null, null, QuestionType.SINGLE_SELECT, null));

    assertThat(result).extracting(Question::getId)
        .containsExactly(SEEDED_ID, SEEDED_COLLABORATE_ID);
  }

  @Test
  @DisplayName("Given text and question type filters when findAll with filters then combine predicates")
  void givenTextAndQuestionTypeFilters_whenFindAllWithFilters_thenCombinePredicates() {
    List<Question> result = questionRepository.findAll(
        withFilters(null, TEXT_PARTIAL_COLLABORATE, QuestionType.SINGLE_SELECT, null));

    assertThat(result).extracting(Question::getId).containsOnly(SEEDED_COLLABORATE_ID);
  }

  @Test
  @DisplayName("Given active filter when findAll with filters then return only matching questions")
  void givenActiveFilter_whenFindAllWithFilters_thenReturnOnlyMatchingQuestions() {
    List<Question> result = questionRepository.findAll(
        withFilters(null, null, null, Boolean.FALSE));

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    boolean result = questionRepository.existsById(SEEDED_ID);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = questionRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  private Question createQuestion(Long answerOptionId) {
    QuestionAnswerOption option = QuestionAnswerOption.builder()
        .answerOption(AnswerOption.builder().id(answerOptionId).build())
        .sortOrder(1L)
        .build();
    return EntityFactory.audited(Question.class)
        .set(field(Question::getText), TEXT)
        .set(field(Question::getQuestionType), QuestionType.SINGLE_SELECT)
        .set(field(Question::getQuestionAnswerOptions), List.of(option))
        .create();
  }
}