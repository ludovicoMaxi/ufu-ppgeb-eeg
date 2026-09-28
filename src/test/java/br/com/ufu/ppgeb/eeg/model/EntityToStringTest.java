package br.com.ufu.ppgeb.eeg.model;

import static java.time.ZonedDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntityToStringTest {

  private static final ZonedDateTime DATE = parse("2024-01-15T10:30:00-03:00");
  private static final String NAME = "name";
  private static final String OTHER_NAME = "other";
  private static final Long AMOUNT = 10L;
  private static final Long EXAM_ID = 100L;

  @Test
  @DisplayName("Given an audited entity when building the string representation then include "
      + "the auditing fields")
  void givenAuditedEntity_whenBuildingStringRepresentation_thenIncludeAuditingFields() {
    Medicament medicament = Medicament.builder().name(NAME).build();
    medicament.setCreatedAt(DATE);
    medicament.setCreatedBy(NAME);
    medicament.setUpdatedAt(DATE);
    medicament.setUpdatedBy(OTHER_NAME);

    assertThat(medicament.toString())
        .contains("createdAt=" + DATE)
        .contains("createdBy=" + NAME)
        .contains("updatedAt=" + DATE)
        .contains("updatedBy=" + OTHER_NAME);
  }

  @Test
  @DisplayName("Given a fully wired exam when building the string representation then not "
      + "overflow the stack")
  void givenFullyWiredExam_whenBuildingStringRepresentation_thenNotOverflowTheStack() {
    Patient patient = Patient.builder().name(NAME).build();
    ExamRequest examRequest = ExamRequest.builder().patient(patient).build();
    Unit unit = Unit.builder().name(NAME).build();
    Exam exam = Exam.builder()
        .id(EXAM_ID)
        .examRequest(examRequest)
        .patient(patient)
        .examMedicaments(List.of(ExamMedicament.builder()
            .exam(Exam.builder().id(EXAM_ID).build())
            .medicament(Medicament.builder().name(NAME).build())
            .amount(AMOUNT)
            .unit(unit)
            .build()))
        .examEquipments(List.of(ExamEquipment.builder()
            .exam(Exam.builder().id(EXAM_ID).build())
            .equipment(Equipment.builder().name(NAME).build())
            .amount(AMOUNT)
            .unit(unit)
            .build()))
        .build();

    String result = exam.toString();

    assertThat(result).startsWith("Exam(").contains("examMedicaments=");
  }

  @Test
  @DisplayName("Given an entity without auditing when building the string representation then "
      + "not print the auditable base class")
  void givenEntityWithoutAuditing_whenBuildingStringRepresentation_thenNotPrintBaseClass() {
    String result = Unit.builder().name(NAME).build().toString();

    assertThat(result).doesNotContain("super=")
        .isEqualTo("Unit(id=null, name=%s, description=null)".formatted(NAME));
  }
}
