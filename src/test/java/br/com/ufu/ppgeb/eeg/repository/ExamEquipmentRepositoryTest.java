package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.CivilStatus;
import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
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
class ExamEquipmentRepositoryTest {

  private static final Long NONEXISTENT_ID = 999L;
  private static final Long AMOUNT_1 = 2L;
  private static final Long AMOUNT_2 = 4L;
  private static final String PATIENT_NAME = "Paciente Exame Equipamento";
  private static final String EQUIPMENT_NAME_1 = "Equipamento Um";
  private static final String EQUIPMENT_NAME_2 = "Equipamento Dois";
  private static final String UNIT_NAME = "unidade";
  private static final String USERNAME = "joaol";

  @Autowired
  private ExamEquipmentRepository examEquipmentRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void clearTables() {
    EntityManager em = testEntityManager.getEntityManager();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
    em.createNativeQuery("DELETE FROM EXAM_EQUIPMENT").executeUpdate();
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
  @DisplayName("Given valid examEquipment when save then generate id and fill auditing fields")
  void givenValidExamEquipment_whenSave_thenGenerateIdAndFillAuditingFields() {
    Exam exam = persistExam();
    Equipment equipment = persistEquipment(EQUIPMENT_NAME_1);
    Unit unit = persistUnit();
    ExamEquipment examEquipment = createExamEquipment(exam, equipment, unit);
    assertThat(examEquipment.getId()).isNull();

    ExamEquipment saved = examEquipmentRepository.save(examEquipment);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getExam().getId()).isEqualTo(exam.getId());
    assertThat(saved.getEquipment().getId()).isEqualTo(equipment.getId());
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing examEquipment when update then fill updated auditing fields and preserve "
      + "creation fields")
  void givenExistingExamEquipment_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Exam exam = persistExam();
    Equipment equipment = persistEquipment(EQUIPMENT_NAME_1);
    Unit unit = persistUnit();
    ExamEquipment saved = examEquipmentRepository.save(createExamEquipment(exam, equipment, unit));

    saved.setAmount(AMOUNT_2);
    examEquipmentRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    ExamEquipment result = testEntityManager.find(ExamEquipment.class, saved.getId());

    assertThat(result.getAmount()).isEqualTo(AMOUNT_2);
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved examEquipment when findById then return the examEquipment")
  void givenSavedExamEquipment_whenFindById_thenReturnTheExamEquipment() {
    Exam exam = persistExam();
    Equipment equipment = persistEquipment(EQUIPMENT_NAME_1);
    Unit unit = persistUnit();
    ExamEquipment saved = examEquipmentRepository.save(createExamEquipment(exam, equipment, unit));

    var result = examEquipmentRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getExam().getId()).isEqualTo(exam.getId());
  }

  @Test
  @DisplayName("Given saved examEquipments when findAll then return all examEquipments")
  void givenSavedExamEquipments_whenFindAll_thenReturnAllExamEquipments() {
    Exam exam = persistExam();
    Equipment equipment1 = persistEquipment(EQUIPMENT_NAME_1);
    Equipment equipment2 = persistEquipment(EQUIPMENT_NAME_2);
    Unit unit = persistUnit();
    ExamEquipment examEquipment1 = examEquipmentRepository.save(createExamEquipment(exam, equipment1, unit));
    ExamEquipment examEquipment2 = examEquipmentRepository.save(
        createExamEquipment(exam, equipment2, unit));

    List<ExamEquipment> result = examEquipmentRepository.findAll();

    assertThat(result).containsOnly(examEquipment1, examEquipment2);
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Exam exam = persistExam();
    Equipment equipment = persistEquipment(EQUIPMENT_NAME_1);
    Unit unit = persistUnit();
    ExamEquipment saved = examEquipmentRepository.save(createExamEquipment(exam, equipment, unit));

    boolean result = examEquipmentRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = examEquipmentRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved examEquipment when deleteById then examEquipment is removed")
  void givenSavedExamEquipment_whenDeleteById_thenExamEquipmentIsRemoved() {
    Exam exam = persistExam();
    Equipment equipment = persistEquipment(EQUIPMENT_NAME_1);
    Unit unit = persistUnit();
    ExamEquipment saved = examEquipmentRepository.save(createExamEquipment(exam, equipment, unit));

    examEquipmentRepository.deleteById(saved.getId());

    assertThat(examEquipmentRepository.existsById(saved.getId())).isFalse();
    assertThat(examEquipmentRepository.findAll()).doesNotContain(saved);
  }

  @Test
  @DisplayName("Given saved examEquipments when findByExam then return only matches")
  void givenSavedExamEquipments_whenFindByExam_thenReturnOnlyMatches() {
    Exam exam = persistExam();
    Equipment equipment1 = persistEquipment(EQUIPMENT_NAME_1);
    Equipment equipment2 = persistEquipment(EQUIPMENT_NAME_2);
    Unit unit = persistUnit();
    ExamEquipment examEquipment1 = examEquipmentRepository.save(createExamEquipment(exam, equipment1, unit));
    ExamEquipment examEquipment2 = examEquipmentRepository.save(
        createExamEquipment(exam, equipment2, unit));

    List<ExamEquipment> result = examEquipmentRepository.findByExam(exam);

    assertThat(result).extracting(ExamEquipment::getId)
        .containsOnly(examEquipment1.getId(), examEquipment2.getId());
  }

  @Test
  @DisplayName("Given exam without equipments when findByExam then return empty")
  void givenExamWithoutEquipments_whenFindByExam_thenReturnEmpty() {
    Exam exam = persistExam();

    List<ExamEquipment> result = examEquipmentRepository.findByExam(exam);

    assertThat(result).isEmpty();
  }

  private Patient persistPatient() {
    Patient patient = new Patient();
    patient.setName(PATIENT_NAME);
    patient.setDocumentNumber("123.456.789-00");
    patient.setSex(Sex.MALE);
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

  private Equipment persistEquipment(String name) {
    Equipment equipment = new Equipment();
    equipment.setName(name);
    equipment.setDescription("Equipamento para exame");
    return testEntityManager.persistAndFlush(equipment);
  }

  private Unit persistUnit() {
    Unit unit = new Unit();
    unit.setName(UNIT_NAME);
    unit.setDescription("Unidade de medida");
    return testEntityManager.persistAndFlush(unit);
  }

  private ExamEquipment createExamEquipment(Exam exam, Equipment equipment, Unit unit) {
    ExamEquipment examEquipment = new ExamEquipment();
    examEquipment.setExam(exam);
    examEquipment.setEquipment(equipment);
    examEquipment.setUnit(unit);
    examEquipment.setAmount(AMOUNT_1);
    return examEquipment;
  }
}