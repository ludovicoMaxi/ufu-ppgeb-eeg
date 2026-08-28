package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.Epoch;
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
class EpochRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long EXAM_ID_1 = 5001L;
  private static final Long EXAM_ID_2 = 5002L;
  private static final Long DURATION_1 = 100L;
  private static final Long DURATION_2 = 200L;
  private static final String DESCRIPTION_1 = "Epoch alfa";
  private static final String DESCRIPTION_2 = "Epoch beta";
  private static final String USERNAME = "joaol";

  @Autowired
  private EpochRepository epochRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM EPOCH").executeUpdate();
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
  @DisplayName("Given valid epoch when save then generate id and fill auditing fields")
  void givenValidEpoch_whenSave_thenGenerateIdAndFillAuditingFields() {
    Epoch epoch = createEpoch(EXAM_ID_1, DURATION_1, DESCRIPTION_1);
    assertThat(epoch.getId()).isNull();

    Epoch saved = epochRepository.save(epoch);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getExamId()).isEqualTo(EXAM_ID_1);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing epoch when update then fill updated auditing fields and preserve creation fields")
  void givenExistingEpoch_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Epoch saved = epochRepository.save(createEpoch(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    saved.setDescription(DESCRIPTION_2);
    epochRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    Epoch result = testEntityManager.find(Epoch.class, saved.getId());

    assertThat(result.getDescription()).isEqualTo(DESCRIPTION_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().getTime()).isEqualTo(saved.getCreatedAt().getTime());
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved epoch when findById then return the epoch")
  void givenSavedEpoch_whenFindById_thenReturnTheEpoch() {
    Epoch saved = epochRepository.save(createEpoch(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    var result = epochRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getExamId()).isEqualTo(EXAM_ID_1);
  }

  @Test
  @DisplayName("Given saved epochs when findAll then return all epochs")
  void givenSavedEpochs_whenFindAll_thenReturnAllEpochs() {
    Epoch epoch1 = epochRepository.save(createEpoch(EXAM_ID_1, DURATION_1, DESCRIPTION_1));
    Epoch epoch2 = epochRepository.save(createEpoch(EXAM_ID_2, DURATION_2, DESCRIPTION_2));

    List<Epoch> result = epochRepository.findAll();

    assertThat(result).containsOnly(epoch1, epoch2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Epoch saved = epochRepository.save(createEpoch(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    boolean result = epochRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = epochRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved epoch when deleteById then epoch is removed")
  void givenSavedEpoch_whenDeleteById_thenEpochIsRemoved() {
    Epoch saved = epochRepository.save(createEpoch(EXAM_ID_1, DURATION_1, DESCRIPTION_1));

    epochRepository.deleteById(saved.getId());

    assertThat(epochRepository.existsById(saved.getId())).isFalse();
    assertThat(epochRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given saved epochs when findByExamId then return only matches")
  void givenSavedEpochs_whenFindByExamId_thenReturnOnlyMatches() {
    epochRepository.save(createEpoch(EXAM_ID_1, DURATION_1, DESCRIPTION_1));
    epochRepository.save(createEpoch(EXAM_ID_2, DURATION_2, DESCRIPTION_2));

    List<Epoch> result = epochRepository.findByExamId(EXAM_ID_2);

    assertThat(result).extracting(Epoch::getExamId).containsExactly(EXAM_ID_2);
  }

  @Test
  @DisplayName("Given no epochs for exam when findByExamId then return empty")
  void givenNoEpochsForExam_whenFindByExamId_thenReturnEmpty() {
    List<Epoch> result = epochRepository.findByExamId(EXAM_ID_1);

    assertThat(result).isEmpty();
  }

  private Epoch createEpoch(Long examId, Long duration, String description) {
    Epoch epoch = new Epoch();
    epoch.setExamId(examId);
    epoch.setStartTime(new Date().getTime());
    epoch.setDuration(duration);
    epoch.setDescription(description);
    return epoch;
  }
}