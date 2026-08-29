package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.CivilStatus;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.model.Sex;
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(AuditingConfig.class)
class PatientRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final String NAME_1 = "Paciente Alfa";
  private static final String NAME_2 = "Paciente Beta";
  private static final String NAME_FILTER = "Alfa";
  private static final String DOCUMENT_1 = "111.111.111-11";
  private static final String DOCUMENT_2 = "222.222.222-22";
  private static final String USERNAME = "joaol";
  private static final String MSG_FILTER = "Informe pelo menos um campo para consultar!";

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM PATIENT").executeUpdate();
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
  @DisplayName("Given valid patient when save then generate id and fill auditing fields")
  void givenValidPatient_whenSave_thenGenerateIdAndFillAuditingFields() {
    Patient patient = createPatient(NAME_1, DOCUMENT_1);
    assertThat(patient.getId()).isNull();

    Patient saved = patientRepository.save(patient);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo(NAME_1);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing patient when update then fill updated auditing fields and preserve creation fields")
  void givenExistingPatient_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Patient saved = patientRepository.save(createPatient(NAME_1, DOCUMENT_1));

    saved.setName(NAME_2);
    patientRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    Optional<Patient> result = patientRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(NAME_2);
    assertThat(result.get().getUpdatedAt()).isNotNull();
    assertThat(result.get().getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.get().getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved patient when findById then return the patient")
  void givenSavedPatient_whenFindById_thenReturnThePatient() {
    Patient saved = patientRepository.save(createPatient(NAME_1, DOCUMENT_1));

    Optional<Patient> result = patientRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(NAME_1);
  }

  @Test
  @DisplayName("Given saved patients when findAll then return all patients")
  void givenSavedPatients_whenFindAll_thenReturnAllPatients() {
    Patient patient1 = patientRepository.save(createPatient(NAME_1, DOCUMENT_1));
    Patient patient2 = patientRepository.save(createPatient(NAME_2, DOCUMENT_2));

    List<Patient> result = patientRepository.findAll();

    assertThat(result).containsOnly(patient1, patient2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Patient saved = patientRepository.save(createPatient(NAME_1, DOCUMENT_1));

    boolean result = patientRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = patientRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved patient when deleteById then patient is removed")
  void givenSavedPatient_whenDeleteById_thenPatientIsRemoved() {
    Patient saved = patientRepository.save(createPatient(NAME_1, DOCUMENT_1));

    patientRepository.deleteById(saved.getId());

    assertThat(patientRepository.existsById(saved.getId())).isFalse();
    assertThat(patientRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given registered document number when existsByDocumentNumber then return true")
  void givenRegisteredDocumentNumber_whenExistsByDocumentNumber_thenReturnTrue() {
    patientRepository.save(createPatient(NAME_1, DOCUMENT_1));

    boolean result = patientRepository.existsByDocumentNumber(DOCUMENT_1);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given unregistered document number when existsByDocumentNumber then return false")
  void givenUnregisteredDocumentNumber_whenExistsByDocumentNumber_thenReturnFalse() {
    patientRepository.save(createPatient(NAME_1, DOCUMENT_1));

    boolean result = patientRepository.existsByDocumentNumber("999.999.999-99");

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved patients when findByFilter name then return only matches")
  void givenSavedPatients_whenFindByFilterName_thenReturnOnlyMatches() {
    patientRepository.save(createPatient(NAME_1, DOCUMENT_1));
    patientRepository.save(createPatient(NAME_2, DOCUMENT_2));

    List<Patient> result = patientRepository.findByFilter(NAME_FILTER, null);

    assertThat(result).extracting(Patient::getName).containsExactly(NAME_1);
  }

  @Test
  @DisplayName("Given saved patients when findByFilter documentNumber then return only matches")
  void givenSavedPatients_whenFindByFilterDocumentNumber_thenReturnOnlyMatches() {
    patientRepository.save(createPatient(NAME_1, DOCUMENT_1));
    patientRepository.save(createPatient(NAME_2, DOCUMENT_2));

    List<Patient> result = patientRepository.findByFilter(null, DOCUMENT_2);

    assertThat(result).extracting(Patient::getName).containsExactly(NAME_2);
  }

  @Test
  @DisplayName("Given saved patients when findByFilter name and documentNumber then return only matches")
  void givenSavedPatients_whenFindByFilterNameAndDocumentNumber_thenReturnOnlyMatches() {
    patientRepository.save(createPatient(NAME_1, DOCUMENT_1));
    patientRepository.save(createPatient(NAME_2, DOCUMENT_2));

    List<Patient> result = patientRepository.findByFilter(NAME_FILTER, DOCUMENT_1);

    assertThat(result).extracting(Patient::getName).containsExactly(NAME_1);
  }

  @Test
  @DisplayName("Given blank filters when findByFilter then throw exception")
  void givenBlankFilters_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> patientRepository.findByFilter(null, "  "))
        .isInstanceOf(InvalidDataAccessApiUsageException.class)
        .hasMessage(MSG_FILTER);
  }

  private Patient createPatient(String name, String documentNumber) {
    Patient patient = new Patient();
    patient.setName(name);
    patient.setDocumentNumber(documentNumber);
    patient.setSex(Sex.MALE);
    patient.setNationality("BRASILEIRA");
    patient.setCivilStatus(CivilStatus.SINGLE);
    patient.setJob("ANALISTA");
    patient.setBirthDate(LocalDate.now());
    return patient;
  }
}