package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.Equipment;
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
class EquipmentRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final String NAME_1 = "EQUIPAMENTO ALFA";
  private static final String NAME_2 = "EQUIPAMENTO BETA";
  private static final String UNREGISTERED_NAME = "Nao Existe";
  private static final String USERNAME = "joaol";

  @Autowired
  private EquipmentRepository equipmentRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM EQUIPMENT").executeUpdate();
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
  @DisplayName("Given valid equipment when save then generate id and fill auditing fields")
  void givenValidEquipment_whenSave_thenGenerateIdAndFillAuditingFields() {
    Equipment equipment = createEquipment(NAME_1);
    assertThat(equipment.getId()).isNull();

    Equipment saved = equipmentRepository.save(equipment);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo(NAME_1);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing equipment when update then fill updated auditing fields and preserve creation fields")
  void givenExistingEquipment_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Equipment saved = equipmentRepository.save(createEquipment(NAME_1));

    saved.setName(NAME_2);
    equipmentRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    Equipment result = testEntityManager.find(Equipment.class, saved.getId());

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo(NAME_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved equipment when findById then return the equipment")
  void givenSavedEquipment_whenFindById_thenReturnTheEquipment() {
    Equipment saved = equipmentRepository.save(createEquipment(NAME_1));

    var result = equipmentRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(NAME_1);
  }

  @Test
  @DisplayName("Given saved equipments when findAll then return all equipments")
  void givenSavedEquipments_whenFindAll_thenReturnAllEquipments() {
    Equipment equipment1 = equipmentRepository.save(createEquipment(NAME_1));
    Equipment equipment2 = equipmentRepository.save(createEquipment(NAME_2));

    List<Equipment> result = equipmentRepository.findAll();

    assertThat(result).containsOnly(equipment1, equipment2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Equipment saved = equipmentRepository.save(createEquipment(NAME_1));

    boolean result = equipmentRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = equipmentRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved equipment when deleteById then equipment is removed")
  void givenSavedEquipment_whenDeleteById_thenEquipmentIsRemoved() {
    Equipment saved = equipmentRepository.save(createEquipment(NAME_1));

    equipmentRepository.deleteById(saved.getId());

    assertThat(equipmentRepository.existsById(saved.getId())).isFalse();
    assertThat(equipmentRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given registered name when findByName then return matching equipment")
  void givenRegisteredName_whenFindByName_thenReturnMatchingEquipment() {
    equipmentRepository.save(createEquipment(NAME_1));
    equipmentRepository.save(createEquipment(NAME_2));

    List<Equipment> result = equipmentRepository.findByName(NAME_2);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo(NAME_2);
  }

  @Test
  @DisplayName("Given unregistered name when findByName then return empty")
  void givenUnregisteredName_whenFindByName_thenReturnEmpty() {
    List<Equipment> result = equipmentRepository.findByName(UNREGISTERED_NAME);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Given registered name when existsByName then return true")
  void givenRegisteredName_whenExistsByName_thenReturnTrue() {
    equipmentRepository.save(createEquipment(NAME_1));

    boolean result = equipmentRepository.existsByName(NAME_1);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given unregistered name when existsByName then return false")
  void givenUnregisteredName_whenExistsByName_thenReturnFalse() {
    boolean result = equipmentRepository.existsByName(UNREGISTERED_NAME);

    assertThat(result).isFalse();
  }

  private Equipment createEquipment(String name) {
    return Equipment.builder()
        .name(name)
        .description("Equipamento para exame")
        .build();
  }
}