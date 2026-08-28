package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.model.Patient;
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
class ExamRequestRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long MEDICAL_RECORD_1 = 555L;
  private static final Long MEDICAL_RECORD_2 = 666L;
  private static final Long MEDICAL_REQUEST_1 = 777L;
  private static final String PATIENT_NAME = "Paciente Requisicao";
  private static final String PATIENT_DOC_1 = "123.456.789-00";
  private static final String DOCTOR_1 = "DRA ALICE";
  private static final String DOCTOR_2 = "DR CARLOS";
  private static final String SECTOR_1 = "NEURO";
  private static final String SECTOR_2 = "PEDIATRIA";
  private static final String USERNAME = "joaol";
  private static final String MSG_FILTER = "Informe pelo menos um campo para consultar!";

  @Autowired
  private ExamRequestRepository examRequestRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM EXAM_REQUEST").executeUpdate();
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
  @DisplayName("Given valid examRequest when save then generate id and fill auditing fields")
  void givenValidExamRequest_whenSave_thenGenerateIdAndFillAuditingFields() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    ExamRequest examRequest = createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1);
    assertThat(examRequest.getId()).isNull();

    ExamRequest saved = examRequestRepository.save(examRequest);

    assertThat(saved.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing examRequest when update then fill updated auditing fields and preserve creation fields")
  void givenExistingExamRequest_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    ExamRequest saved = examRequestRepository.save(createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));

    saved.setSector(SECTOR_2);
    examRequestRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    ExamRequest result = testEntityManager.find(ExamRequest.class, saved.getId());

    assertThat(result).isNotNull();
    assertThat(result.getSector()).isEqualTo(SECTOR_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().getTime()).isEqualTo(saved.getCreatedAt().getTime());
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved examRequest when findById then return the examRequest")
  void givenSavedExamRequest_whenFindById_thenReturnTheExamRequest() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    ExamRequest saved = examRequestRepository.save(createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));

    Optional<ExamRequest> result = examRequestRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getPatient().getId()).isEqualTo(patient.getId());
  }

  @Test
  @DisplayName("Given saved examRequests when findAll then return all examRequests")
  void givenSavedExamRequests_whenFindAll_thenReturnAllExamRequests() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    ExamRequest examRequest1 = examRequestRepository.save(
        createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));
    ExamRequest examRequest2 = examRequestRepository.save(
        createExamRequest(patient, MEDICAL_RECORD_2, DOCTOR_2, SECTOR_2));

    List<ExamRequest> result = examRequestRepository.findAll();

    assertThat(result).containsOnly(examRequest1, examRequest2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    ExamRequest saved = examRequestRepository.save(
        createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));

    boolean result = examRequestRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = examRequestRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved examRequest when deleteById then examRequest is removed")
  void givenSavedExamRequest_whenDeleteById_thenExamRequestIsRemoved() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    ExamRequest saved = examRequestRepository.save(
        createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));

    examRequestRepository.deleteById(saved.getId());

    assertThat(examRequestRepository.existsById(saved.getId())).isFalse();
    assertThat(examRequestRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given saved examRequests when findByFilter medicalRecord then return only matches")
  void givenSavedExamRequests_whenFindByFilterMedicalRecord_thenReturnOnlyMatches() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    examRequestRepository.save(createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));
    ExamRequest second = examRequestRepository.save(
        createExamRequest(patient, MEDICAL_RECORD_2, DOCTOR_2, SECTOR_2));

    List<ExamRequest> result = examRequestRepository.findByFilter(MEDICAL_RECORD_2, null, null, null);

    assertThat(result).extracting(ExamRequest::getId).containsExactly(second.getId());
  }

  @Test
  @DisplayName("Given saved examRequests when findByFilter doctorRequestant then return only matches")
  void givenSavedExamRequests_whenFindByFilterDoctorRequestant_thenReturnOnlyMatches() {
    Patient patient = persistPatient(PATIENT_DOC_1);
    examRequestRepository.save(createExamRequest(patient, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));
    examRequestRepository.save(createExamRequest(patient, MEDICAL_RECORD_2, DOCTOR_2, SECTOR_2));

    List<ExamRequest> result = examRequestRepository.findByFilter(null, null, null, "ALICE");

    assertThat(result).extracting(ExamRequest::getDoctorRequestant).containsExactly(DOCTOR_1);
  }

  @Test
  @DisplayName("Given saved examRequests when findByFilter patientId then return only matches")
  void givenSavedExamRequests_whenFindByFilterPatientId_thenReturnOnlyMatches() {
    Patient patient1 = persistPatient(PATIENT_DOC_1);
    Patient patient2 = persistPatient("987.654.321-00");
    examRequestRepository.save(createExamRequest(patient1, MEDICAL_RECORD_1, DOCTOR_1, SECTOR_1));
    ExamRequest second = examRequestRepository.save(
        createExamRequest(patient2, MEDICAL_RECORD_2, DOCTOR_2, SECTOR_2));

    List<ExamRequest> result = examRequestRepository.findByFilter(null, null, patient2.getId(), null);

    assertThat(result).extracting(ExamRequest::getId).containsExactly(second.getId());
  }

  @Test
  @DisplayName("Given blank filters when findByFilter then throw exception")
  void givenBlankFilters_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> examRequestRepository.findByFilter(null, null, null, "  "))
        .isInstanceOf(InvalidDataAccessApiUsageException.class)
        .hasMessage(MSG_FILTER);
  }

  private Patient persistPatient(String documentNumber) {
    Patient patient = new Patient();
    patient.setName(PATIENT_NAME);
    patient.setDocumentNumber(documentNumber);
    patient.setSex('F');
    patient.setNacionality("BRASILEIRA");
    patient.setCivilStatus("SOLTEIRA");
    patient.setJob("ANALISTA");
    patient.setBirthDate(new Date());
    return testEntityManager.persistAndFlush(patient);
  }

  private ExamRequest createExamRequest(Patient patient, Long medicalRecord, String doctor, String sector) {
    ExamRequest examRequest = new ExamRequest();
    examRequest.setPatient(patient);
    examRequest.setMedicalRecord(medicalRecord);
    examRequest.setMedicalRequest(MEDICAL_REQUEST_1);
    examRequest.setDoctorRequestant(doctor);
    examRequest.setSector(sector);
    examRequest.setRequestDate(new Date());
    examRequest.setUser("USUARIO_TESTE");
    return examRequest;
  }
}