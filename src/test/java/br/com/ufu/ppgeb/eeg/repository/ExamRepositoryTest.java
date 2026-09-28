package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.EntityFactory;
import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.repository.ExamSpecifications;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  private static final String BED_1 = "Leito Norte";
  private static final String BED_2 = "Leito Sul";
  private static final String USERNAME = "joaol";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Autowired
  private ExamRepository examRepository;

  @Autowired
  private ExamRequestRepository examRequestRepository;

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
    Patient patient = persistPatient();
    Exam exam = createExam(patient, BED_1);
    assertThat(exam.getId()).isNull();

    Exam saved = examRepository.save(exam);

    assertThat(saved).hasNoNullFieldsOrPropertiesExcept(UPDATED_AT_FIELD, UPDATED_BY_FIELD);
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
    Patient patient = persistPatient();
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
    Patient patient = persistPatient();
    Exam saved = examRepository.save(createExam(patient, BED_1));

    Optional<Exam> result = examRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getPatient().getId()).isEqualTo(patient.getId());
  }

  @Test
  @DisplayName("Given saved exams when findAll then return all exams")
  void givenSavedExams_whenFindAll_thenReturnAllExams() {
    Patient patient = persistPatient();
    Exam exam1 = examRepository.save(createExam(patient, BED_1));
    Exam exam2 = examRepository.save(createExam(patient, BED_1));

    List<Exam> result = examRepository.findAll();

    assertThat(result).containsOnly(exam1, exam2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Patient patient = persistPatient();
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
    Patient patient = persistPatient();
    Exam saved = examRepository.save(createExam(patient, BED_1));

    examRepository.deleteById(saved.getId());

    assertThat(examRepository.existsById(saved.getId())).isFalse();
    assertThat(examRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given saved exams when findAll with id filter then return only matches")
  void givenSavedExams_whenFindAllWithIdFilter_thenReturnOnlyMatches() {
    Patient patient = persistPatient();
    Exam exam1 = examRepository.save(createExam(patient, BED_1));
    examRepository.save(createExam(patient, BED_1));

    Page<Exam> result = examRepository.findAll(
        ExamSpecifications.withFilters(exam1.getId(), null, null, null), PAGEABLE);

    assertThat(result.getContent()).extracting(Exam::getId).containsExactly(exam1.getId());
  }

  @Test
  @DisplayName("Given saved exams when findAll with bed filter then return only matches")
  void givenSavedExams_whenFindAllWithBedFilter_thenReturnOnlyMatches() {
    Patient patient = persistPatient();
    Exam saved = examRepository.save(createExam(patient, BED_1));
    examRepository.save(createExam(patient, BED_2));

    Page<Exam> result = examRepository.findAll(
        ExamSpecifications.withFilters(null, BED_1, null, null), PAGEABLE);

    assertThat(result.getContent()).extracting(Exam::getId).containsExactly(saved.getId());
  }

  @Test
  @DisplayName("Given saved exams when findAll with patient id filter then return only matches")
  void givenSavedExams_whenFindAllWithPatientIdFilter_thenReturnOnlyMatches() {
    Patient patient1 = persistPatient();
    Patient patient2 = persistPatient();
    Exam saved = examRepository.save(createExam(patient1, BED_1));
    examRepository.save(createExam(patient2, BED_1));

    Page<Exam> result = examRepository.findAll(
        ExamSpecifications.withFilters(null, null, patient1.getId(), null), PAGEABLE);

    assertThat(result.getContent()).extracting(Exam::getId).containsExactly(saved.getId());
  }

  @Test
  @DisplayName("Given saved exams when findAll without filters then return all exams")
  void givenSavedExams_whenFindAllWithoutFilters_thenReturnAllExams() {
    Patient patient = persistPatient();
    Exam exam1 = examRepository.save(createExam(patient, BED_1));
    Exam exam2 = examRepository.save(createExam(patient, BED_1));

    Page<Exam> result = examRepository.findAll(
        ExamSpecifications.withFilters(null, null, null, null), PAGEABLE);

    assertThat(result.getContent()).extracting(Exam::getId).containsExactlyInAnyOrder(
        exam1.getId(), exam2.getId());
  }

  private Patient persistPatient() {
    Patient patient = EntityFactory.audited(Patient.class)
        .set(field(Patient::getName), PATIENT_NAME)
        .create();
    return testEntityManager.persistAndFlush(patient);
  }

  private Exam createExam(Patient patient, String bed) {
    return EntityFactory.audited(Exam.class)
        .set(field(Exam::getPatient), patient)
        .set(field(Exam::getBed), bed)
        .set(field(Exam::getExamRequest), persistExamRequest(patient))
        .set(field(Exam::getExamMedicaments), new ArrayList<>())
        .set(field(Exam::getExamEquipments), new ArrayList<>())
        .create();
  }

  private ExamRequest persistExamRequest(Patient patient) {
    ExamRequest examRequest = EntityFactory.audited(ExamRequest.class)
        .set(field(ExamRequest::getPatient), patient)
        .create();
    return examRequestRepository.save(examRequest);
  }
}