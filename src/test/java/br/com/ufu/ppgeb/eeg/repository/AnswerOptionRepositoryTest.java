package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
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
class AnswerOptionRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long SEEDED_ID = 1001L;
  private static final String SEEDED_TEXT = "Lúcido";
  private static final String TEXT = "Irritado";
  private static final String USERNAME = "joaol";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

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
  @DisplayName("Given valid answer option when save then generate id and fill auditing fields")
  void givenValidAnswerOption_whenSave_thenGenerateIdAndFillAuditingFields() {
    AnswerOption answerOption = AnswerOption.builder()
        .text(TEXT)
        .active(Boolean.TRUE)
        .build();
    assertThat(answerOption.getId()).isNull();

    AnswerOption saved = answerOptionRepository.save(answerOption);

    assertThat(saved).hasNoNullFieldsOrPropertiesExcept(UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getText()).isEqualTo(TEXT);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing id when findById then return the answer option")
  void givenExistingId_whenFindById_thenReturnTheAnswerOption() {
    var result = answerOptionRepository.findById(SEEDED_ID);

    assertThat(result).isPresent();
    assertThat(result.get().getText()).isEqualTo(SEEDED_TEXT);
  }

  @Test
  @DisplayName("Given seeded answer options when findAll then return them")
  void givenSeededAnswerOptions_whenFindAll_thenReturnThem() {
    List<AnswerOption> result = answerOptionRepository.findAll();

    assertThat(result).isNotEmpty();
    assertThat(result).extracting(AnswerOption::getId).contains(SEEDED_ID);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    boolean result = answerOptionRepository.existsById(SEEDED_ID);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = answerOptionRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }
}