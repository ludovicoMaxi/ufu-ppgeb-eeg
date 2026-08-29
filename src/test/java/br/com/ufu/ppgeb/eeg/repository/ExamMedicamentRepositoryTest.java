package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.CivilStatus;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.model.Sex;
import br.com.ufu.ppgeb.eeg.model.Unit;
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
class ExamMedicamentRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long AMOUNT_1 = 10L;
  private static final Long AMOUNT_2 = 20L;
  private static final String PATIENT_NAME = "Paciente Exame Medicamento";
  private static final String MEDICAMENT_NAME_1 = "Medicamento Um";
  private static final String MEDICAMENT_NAME_2 = "Medicamento Dois";
  private static final String UNIT_NAME = "mililitro";
  private static final String USERNAME = "joaol";

  @Autowired
  private ExamMedicamentRepository examMedicamentRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM EXAM_MEDICAMENT").executeUpdate();
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
  @DisplayName("Given valid examMedicament when save then generate id and fill auditing fields")
  void givenValidExamMedicament_whenSave_thenGenerateIdAndFillAuditingFields() {
    Exam exam = persistExam();
    Medicament medicament = persistMedicament(MEDICAMENT_NAME_1);
    Unit unit = persistUnit();
    ExamMedicament examMedicament = createExamMedicament(exam, medicament, unit);
    assertThat(examMedicament.getId()).isNull();

    ExamMedicament saved = examMedicamentRepository.save(examMedicament);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getExam().getId()).isEqualTo(exam.getId());
    assertThat(saved.getMedicament().getId()).isEqualTo(medicament.getId());
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing examMedicament when update then fill updated auditing fields and preserve "
      + "creation fields")
  void givenExistingExamMedicament_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Exam exam = persistExam();
    Medicament medicament = persistMedicament(MEDICAMENT_NAME_1);
    Unit unit = persistUnit();
    ExamMedicament saved = examMedicamentRepository.save(createExamMedicament(exam, medicament, unit));

    saved.setAmount(AMOUNT_2);
    examMedicamentRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    ExamMedicament result = testEntityManager.find(ExamMedicament.class, saved.getId());

    assertThat(result.getAmount()).isEqualTo(AMOUNT_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved examMedicament when findById then return the examMedicament")
  void givenSavedExamMedicament_whenFindById_thenReturnTheExamMedicament() {
    Exam exam = persistExam();
    Medicament medicament = persistMedicament(MEDICAMENT_NAME_1);
    Unit unit = persistUnit();
    ExamMedicament saved = examMedicamentRepository.save(createExamMedicament(exam, medicament, unit));

    var result = examMedicamentRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getExam().getId()).isEqualTo(exam.getId());
  }

  @Test
  @DisplayName("Given saved examMedicaments when findAll then return all examMedicaments")
  void givenSavedExamMedicaments_whenFindAll_thenReturnAllExamMedicaments() {
    Exam exam = persistExam();
    Medicament medicament1 = persistMedicament(MEDICAMENT_NAME_1);
    Medicament medicament2 = persistMedicament(MEDICAMENT_NAME_2);
    Unit unit = persistUnit();
    ExamMedicament examMedicament1 = examMedicamentRepository.save(createExamMedicament(exam, medicament1, unit));
    ExamMedicament examMedicament2 = examMedicamentRepository.save(
        createExamMedicament(exam, medicament2, unit));

    List<ExamMedicament> result = examMedicamentRepository.findAll();

    assertThat(result).containsOnly(examMedicament1, examMedicament2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Exam exam = persistExam();
    Medicament medicament = persistMedicament(MEDICAMENT_NAME_1);
    Unit unit = persistUnit();
    ExamMedicament saved = examMedicamentRepository.save(createExamMedicament(exam, medicament, unit));

    boolean result = examMedicamentRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = examMedicamentRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved examMedicament when deleteById then examMedicament is removed")
  void givenSavedExamMedicament_whenDeleteById_thenExamMedicamentIsRemoved() {
    Exam exam = persistExam();
    Medicament medicament = persistMedicament(MEDICAMENT_NAME_1);
    Unit unit = persistUnit();
    ExamMedicament saved = examMedicamentRepository.save(createExamMedicament(exam, medicament, unit));

    examMedicamentRepository.deleteById(saved.getId());

    assertThat(examMedicamentRepository.existsById(saved.getId())).isFalse();
    assertThat(examMedicamentRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given saved examMedicaments when findByExam then return only matches")
  void givenSavedExamMedicaments_whenFindByExam_thenReturnOnlyMatches() {
    Exam exam = persistExam();
    Medicament medicament1 = persistMedicament(MEDICAMENT_NAME_1);
    Medicament medicament2 = persistMedicament(MEDICAMENT_NAME_2);
    Unit unit = persistUnit();
    ExamMedicament examMedicament1 = examMedicamentRepository.save(createExamMedicament(exam, medicament1, unit));
    ExamMedicament examMedicament2 = examMedicamentRepository.save(
        createExamMedicament(exam, medicament2, unit));

    List<ExamMedicament> result = examMedicamentRepository.findByExam(exam);

    assertThat(result).extracting(ExamMedicament::getId)
        .containsOnly(examMedicament1.getId(), examMedicament2.getId());
  }

  @Test
  @DisplayName("Given exam without medicaments when findByExam then return empty")
  void givenExamWithoutMedicaments_whenFindByExam_thenReturnEmpty() {
    Exam exam = persistExam();

    List<ExamMedicament> result = examMedicamentRepository.findByExam(exam);

    assertThat(result).isEmpty();
  }

  private Patient persistPatient() {
    Patient patient = new Patient();
    patient.setName(PATIENT_NAME);
    patient.setDocumentNumber("123.456.789-00");
    patient.setSex(Sex.FEMALE);
    patient.setNationality("BRASILEIRA");
    patient.setCivilStatus(CivilStatus.SINGLE);
    patient.setJob("ANALISTA");
    patient.setBirthDate(LocalDate.now());
    return testEntityManager.persistAndFlush(patient);
  }

  private Exam persistExam() {
    Exam exam = new Exam(null);
    exam.setPatient(persistPatient());
    exam.setAchievementDate(ZonedDateTime.now());
    exam.setBed("Leito Central");
    return testEntityManager.persistAndFlush(exam);
  }

  private Medicament persistMedicament(String name) {
    Medicament medicament = new Medicament();
    medicament.setName(name);
    medicament.setDescription("Medicamento para exame");
    return testEntityManager.persistAndFlush(medicament);
  }

  private Unit persistUnit() {
    Unit unit = new Unit();
    unit.setName(UNIT_NAME);
    unit.setDescription("Unidade de medida");
    return testEntityManager.persistAndFlush(unit);
  }

  private ExamMedicament createExamMedicament(Exam exam, Medicament medicament, Unit unit) {
    ExamMedicament examMedicament = new ExamMedicament();
    examMedicament.setExam(exam);
    examMedicament.setMedicament(medicament);
    examMedicament.setUnit(unit);
    examMedicament.setAmount(AMOUNT_1);
    return examMedicament;
  }
}