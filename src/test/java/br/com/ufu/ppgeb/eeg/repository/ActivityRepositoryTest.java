package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.Activity;
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
class ActivityRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long EXAM_ID_1 = 5001L;
  private static final Long EXAM_ID_2 = 5002L;
  private static final Long DURATION_1 = 100L;
  private static final Long DURATION_2 = 200L;
  private static final String DESCRIPTION_1 = "Atividade alfa";
  private static final String DESCRIPTION_2 = "Atividade beta";
  private static final String USERNAME = "joaol";

  @Autowired
  private ActivityRepository activityRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM ACTIVITY").executeUpdate();
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
  @DisplayName("Given valid activity when save then generate id and fill auditing fields")
  void givenValidActivity_whenSave_thenGenerateIdAndFillAuditingFields() {
    Activity activity = createActivity(EXAM_ID_1, DURATION_1, DESCRIPTION_1);
    assertThat(activity.getId()).isNull();

    Activity saved = activityRepository.save(activity);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getExamId()).isEqualTo(EXAM_ID_1);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing activity when update then fill updated auditing fields and preserve creation fields")
  void givenExistingActivity_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Activity saved = activityRepository.save(createActivity(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    saved.setDescription(DESCRIPTION_2);
    activityRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    Activity result = testEntityManager.find(Activity.class, saved.getId());

    assertThat(result.getDescription()).isEqualTo(DESCRIPTION_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved activity when findById then return the activity")
  void givenSavedActivity_whenFindById_thenReturnTheActivity() {
    Activity saved = activityRepository.save(createActivity(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    var result = activityRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getExamId()).isEqualTo(EXAM_ID_1);
  }

  @Test
  @DisplayName("Given saved activities when findAll then return all activities")
  void givenSavedActivities_whenFindAll_thenReturnAllActivities() {
    Activity activity1 = activityRepository.save(createActivity(EXAM_ID_1, DURATION_1, DESCRIPTION_1));
    Activity activity2 = activityRepository.save(createActivity(EXAM_ID_2, DURATION_2, DESCRIPTION_2));

    List<Activity> result = activityRepository.findAll();

    assertThat(result).containsOnly(activity1, activity2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Activity saved = activityRepository.save(createActivity(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    boolean result = activityRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = activityRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved activity when deleteById then activity is removed")
  void givenSavedActivity_whenDeleteById_thenActivityIsRemoved() {
    Activity saved = activityRepository.save(createActivity(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    activityRepository.deleteById(saved.getId());

    assertThat(activityRepository.existsById(saved.getId())).isFalse();
    assertThat(activityRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given saved activities when findByExamId then return only matches")
  void givenSavedActivities_whenFindByExamId_thenReturnOnlyMatches() {
    activityRepository.save(createActivity(EXAM_ID_1, DURATION_1, DESCRIPTION_1));
    activityRepository.save(createActivity(EXAM_ID_2, DURATION_2, DESCRIPTION_2));

    List<Activity> result = activityRepository.findByExamId(EXAM_ID_2);

    assertThat(result).extracting(Activity::getExamId).containsExactly(EXAM_ID_2);
  }

  @Test
  @DisplayName("Given no activities for exam when findByExamId then return empty")
  void givenNoActivitiesForExam_whenFindByExamId_thenReturnEmpty() {
    List<Activity> result = activityRepository.findByExamId(EXAM_ID_1);

    assertThat(result).isEmpty();
  }

  private Activity createActivity(Long examId, Long duration, String description) {
    return Activity.builder()
        .examId(examId)
        .startTime(System.currentTimeMillis())
        .duration(duration)
        .description(description)
        .build();
  }
}