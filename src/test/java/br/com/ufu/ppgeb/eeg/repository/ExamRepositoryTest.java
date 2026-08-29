package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.CivilStatus;
import br.com.ufu.ppgeb.eeg.model.Exam;
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
class ExamRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final String PATIENT_NAME = "Paciente Exame";
  private static final String PATIENT_DOC_1 = "123.456.789-00";
  private static final String BED_1 = "Leito Norte";
  private static final String BED_2 = "Leito Sul";
  private static final String USERNAME = "joaol";
  private static final String MSG_FILTER = "Informe pelo menos um campo para consultar!";

  @Autowired
  private ExamRepository examRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM EXAM").executeUpdate();
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
  @DisplayName("Given valid exam when save then generate id and fill auditing fields")
  void givenValidExam_whenSave_thenGenerateIdAndFillAuditingFields() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam exam = createExam(patient, BED_1);
    assertThat(exam.getId()).isNull();

    Exam saved = examRepository.save(exam);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing exam when update then fill updated auditing fields and preserve creation fields")
  void givenExistingExam_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam saved = examRepository.save(createExam(patient, BED_1));

    saved.setBed(BED_2);
    examRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    Exam result = testEntityManager.find(Exam.class, saved.getId());

    assertThat(result.getBed()).isEqualTo(BED_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved exam when findById then return the exam")
  void givenSavedExam_whenFindById_thenReturnTheExam() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam saved = examRepository.save(createExam(patient, BED_1));

    Optional<Exam> result = examRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getPatient().getId()).isEqualTo(patient.getId());
  }

  @Test
  @DisplayName("Given saved exams when findAll then return all exams")
  void givenSavedExams_whenFindAll_thenReturnAllExams() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam exam1 = examRepository.save(createExam(patient, BED_1));
    Exam exam2 = examRepository.save(createExam(patient, BED_2));

    List<Exam> result = examRepository.findAll();

    assertThat(result).containsOnly(exam1, exam2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam saved = examRepository.save(createExam(patient, BED_1));

    boolean result = examRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = examRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved exam when deleteById then exam is removed")
  void givenSavedExam_whenDeleteById_thenExamIsRemoved() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam saved = examRepository.save(createExam(patient, BED_1));

    examRepository.deleteById(saved.getId());

    assertThat(examRepository.existsById(saved.getId())).isFalse();
    assertThat(examRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given saved exams when findByFilter id then return only matches")
  void givenSavedExams_whenFindByFilterId_thenReturnOnlyMatches() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam exam1 = examRepository.save(createExam(patient, BED_1));
    examRepository.save(createExam(patient, BED_2));

    List<Exam> result = examRepository.findByFilter(exam1.getId(), null, null, null);

    assertThat(result).extracting(Exam::getId).containsExactly(exam1.getId());
  }

  @Test
  @DisplayName("Given saved exams when findByFilter bed then return only matches")
  void givenSavedExams_whenFindByFilterBed_thenReturnOnlyMatches() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    Exam saved = examRepository.save(createExam(patient, BED_1));
    examRepository.save(createExam(patient, BED_2));

    List<Exam> result = examRepository.findByFilter(null, BED_1, null, null);

    assertThat(result).extracting(Exam::getId).containsExactly(saved.getId());
  }

  @Test
  @DisplayName("Given saved exams when findByFilter patientId then return only matches")
  void givenSavedExams_whenFindByFilterPatientId_thenReturnOnlyMatches() {
    Patient patient1 = persistPatient(PATIENT_DOC_1);
    Patient patient2 = persistPatient("987.654.321-00");
    Exam saved = examRepository.save(createExam(patient1, BED_1));
    examRepository.save(createExam(patient2, BED_2));

    List<Exam> result = examRepository.findByFilter(null, null, patient1.getId(), null);

    assertThat(result).extracting(Exam::getId).containsExactly(saved.getId());
  }

  @Test
  @DisplayName("Given blank filters when findByFilter then throw exception")
  void givenBlankFilters_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> examRepository.findByFilter(null, null, null, null))
        .isInstanceOf(InvalidDataAccessApiUsageException.class)
        .hasMessage(MSG_FILTER);
  }

  private Patient persistPatient(String documentNumber) {
    Patient patient = new Patient();
    patient.setName(PATIENT_NAME);
    patient.setDocumentNumber(documentNumber);
    patient.setSex(Sex.MALE);
    patient.setNationality("BRASILEIRA");
    patient.setCivilStatus(CivilStatus.SINGLE);
    patient.setJob("ANALISTA");
    patient.setBirthDate(LocalDate.now());
    return testEntityManager.persistAndFlush(patient);
  }

  private Exam createExam(Patient patient, String bed) {
    Exam exam = new Exam(null);
    exam.setPatient(patient);
    exam.setAchievementDate(ZonedDateTime.now());
    exam.setBed(bed);
    exam.setClinicalData("Dados clínicos");
    return exam;
  }
}