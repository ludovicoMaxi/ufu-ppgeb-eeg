package br.com.ufu.ppgeb.eeg.repository;

import static br.com.ufu.ppgeb.eeg.repository.QuestionnaireSpecifications.withFilters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.EntityFactory;
import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
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
class QuestionnaireRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long SEEDED_ID = 1001L;
  private static final String TITLE_PARTIAL = "anamnese";
  private static final String TITLE = "Questionário de follow-up";
  private static final String QUESTION_TEXT = "Como está o paciente após 30 dias?";
  private static final String USERNAME = "joaol";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @Autowired
  private QuestionRepository questionRepository;

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
  @DisplayName("Given valid questionnaire when save then generate id and fill auditing fields")
  void givenValidQuestionnaire_whenSave_thenGenerateIdAndFillAuditingFields() {
    Question question = EntityFactory.audited(Question.class)
        .set(field(Question::getText), QUESTION_TEXT)
        .set(field(Question::getQuestionAnswerOptions), new ArrayList<>())
        .create();
    questionRepository.save(question);
    Questionnaire questionnaire = createQuestionnaire(question.getId());

    assertThat(questionnaire.getId()).isNull();

    Questionnaire saved = questionnaireRepository.save(questionnaire);

    assertThat(saved).hasNoNullFieldsOrPropertiesExcept(UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getTitle()).isEqualTo(TITLE);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
    assertThat(saved.getQuestionnaireQuestions()).hasSize(1);
  }

  @Test
  @DisplayName("Given existing id when findById then return the questionnaire")
  void givenExistingId_whenFindById_thenReturnTheQuestionnaire() {
    var result = questionnaireRepository.findById(SEEDED_ID);

    assertThat(result).isPresent();
    assertThat(result.get().getTitle()).isEqualTo("Anamnese do exame");
  }

  @Test
  @DisplayName("Given id filter when findAll with filters then return matching questionnaire")
  void givenIdFilter_whenFindAllWithFilters_thenReturnMatchingQuestionnaire() {
    List<Questionnaire> result = questionnaireRepository.findAll(
        withFilters(SEEDED_ID, null));

    assertThat(result).extracting(Questionnaire::getId).containsOnly(SEEDED_ID);
  }

  @Test
  @DisplayName("Given title filter when findAll with filters then return matching questionnaires ignoring case")
  void givenTitleFilter_whenFindAllWithFilters_thenReturnMatchingQuestionnairesIgnoringCase() {
    List<Questionnaire> result = questionnaireRepository.findAll(
        withFilters(null, TITLE_PARTIAL));

    assertThat(result).extracting(Questionnaire::getId).containsOnly(SEEDED_ID);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    boolean result = questionnaireRepository.existsById(SEEDED_ID);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = questionnaireRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  private Questionnaire createQuestionnaire(Long questionId) {
    QuestionnaireQuestion link = QuestionnaireQuestion.builder()
        .question(Question.builder().id(questionId).build())
        .sortOrder(1L)
        .build();
    return EntityFactory.audited(Questionnaire.class)
        .set(field(Questionnaire::getTitle), TITLE)
        .set(field(Questionnaire::getQuestionnaireQuestions), List.of(link))
        .create();
  }
}