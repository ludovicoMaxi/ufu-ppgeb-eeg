package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.model.Unit;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UnitRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final String NAME_1 = "litro";
  private static final String NAME_2 = "centimetro";

  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM UNIT").executeUpdate();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
  }

  @Test
  @DisplayName("Given valid unit when save then generate id")
  void givenValidUnit_whenSave_thenGenerateId() {
    Unit unit = createUnit(NAME_1);

    Unit saved = unitRepository.save(unit);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo(NAME_1);
  }

  @Test
  @DisplayName("Given saved unit when findById then return the unit")
  void givenSavedUnit_whenFindById_thenReturnTheUnit() {
    Unit saved = unitRepository.save(createUnit(NAME_1));

    Optional<Unit> result = unitRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(NAME_1);
  }

  @Test
  @DisplayName("Given saved units when findAll then return all units")
  void givenSavedUnits_whenFindAll_thenReturnAllUnits() {
    Unit unit1 = unitRepository.save(createUnit(NAME_1));
    Unit unit2 = unitRepository.save(createUnit(NAME_2));

    List<Unit> result = unitRepository.findAll();

    assertThat(result).containsOnly(unit1, unit2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Unit saved = unitRepository.save(createUnit(NAME_1));

    boolean result = unitRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = unitRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved unit when deleteById then unit is removed")
  void givenSavedUnit_whenDeleteById_thenUnitIsRemoved() {
    Unit saved = unitRepository.save(createUnit(NAME_1));

    unitRepository.deleteById(saved.getId());

    assertThat(unitRepository.existsById(saved.getId())).isFalse();
    assertThat(unitRepository.findAll()).doesNotContain(saved);
  }

  private Unit createUnit(String name) {
    return Unit.builder()
        .name(name)
        .build();
  }
}