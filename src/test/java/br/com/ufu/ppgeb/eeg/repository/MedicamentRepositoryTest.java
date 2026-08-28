package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.Medicament;
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
class MedicamentRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final String NAME_1 = "MEDICAMENTO ALFA";
  private static final String NAME_2 = "MEDICAMENTO BETA";
  private static final String UNREGISTERED_NAME = "Nao Existe";
  private static final String USERNAME = "joaol";

  @Autowired
  private MedicamentRepository medicamentRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM MEDICAMENT").executeUpdate();
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
  @DisplayName("Given valid medicament when save then generate id and fill auditing fields")
  void givenValidMedicament_whenSave_thenGenerateIdAndFillAuditingFields() {
    Medicament medicament = createMedicament(NAME_1);
    assertThat(medicament.getId()).isNull();

    Medicament saved = medicamentRepository.save(medicament);

    assertThat(saved.getName()).isEqualTo(NAME_1);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing medicament when update then fill updated auditing fields and preserve creation fields")
  void givenExistingMedicament_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Medicament saved = medicamentRepository.save(createMedicament(NAME_1));

    saved.setName(NAME_2);
    medicamentRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    Medicament result = testEntityManager.find(Medicament.class, saved.getId());

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo(NAME_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved medicament when findById then return the medicament")
  void givenSavedMedicament_whenFindById_thenReturnTheMedicament() {
    Medicament saved = medicamentRepository.save(createMedicament(NAME_1));

    var result = medicamentRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(NAME_1);
  }

  @Test
  @DisplayName("Given saved medicaments when findAll then return all medicaments")
  void givenSavedMedicaments_whenFindAll_thenReturnAllMedicaments() {
    Medicament medicament1 = medicamentRepository.save(createMedicament(NAME_1));
    Medicament medicament2 = medicamentRepository.save(createMedicament(NAME_2));

    List<Medicament> result = medicamentRepository.findAll();

    assertThat(result).containsOnly(medicament1, medicament2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Medicament saved = medicamentRepository.save(createMedicament(NAME_1));

    boolean result = medicamentRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = medicamentRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved medicament when deleteById then medicament is removed")
  void givenSavedMedicament_whenDeleteById_thenMedicamentIsRemoved() {
    Medicament saved = medicamentRepository.save(createMedicament(NAME_1));

    medicamentRepository.deleteById(saved.getId());

    assertThat(medicamentRepository.existsById(saved.getId())).isFalse();
    assertThat(medicamentRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given registered name when findByName then return matching medicament")
  void givenRegisteredName_whenFindByName_thenReturnMatchingMedicament() {
    medicamentRepository.save(createMedicament(NAME_1));
    medicamentRepository.save(createMedicament(NAME_2));

    var result = medicamentRepository.findByName(NAME_2);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo(NAME_2);
  }

  @Test
  @DisplayName("Given unregistered name when findByName then return empty")
  void givenUnregisteredName_whenFindByName_thenReturnEmpty() {
    List<Medicament> result = medicamentRepository.findByName(UNREGISTERED_NAME);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Given registered name when existsByName then return true")
  void givenRegisteredName_whenExistsByName_thenReturnTrue() {
    medicamentRepository.save(createMedicament(NAME_1));

    boolean result = medicamentRepository.existsByName(NAME_1);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given unregistered name when existsByName then return false")
  void givenUnregisteredName_whenExistsByName_thenReturnFalse() {
    boolean result = medicamentRepository.existsByName(UNREGISTERED_NAME);

    assertThat(result).isFalse();
  }

  private Medicament createMedicament(String name) {
    Medicament medicament = new Medicament();
    medicament.setName(name);
    medicament.setDescription("Medicamento para exame");
    return medicament;
  }
}