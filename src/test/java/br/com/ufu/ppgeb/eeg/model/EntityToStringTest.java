package br.com.ufu.ppgeb.eeg.model;

import static java.time.ZonedDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntityToStringTest {

  private static final ZonedDateTime DATE = parse("2024-01-15T10:30:00-03:00");
  private static final String NAME = "name";
  private static final String OTHER_NAME = "other";

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
    Exam exam = Instancio.create(Exam.class);

    assertThat(exam.getExamRequest()).isNotNull();
    assertThat(exam.getPatient()).isNotNull();
    assertThat(exam.getExamMedicaments()).isNotEmpty();
    assertThat(exam.getExamEquipments()).isNotEmpty();

    String result = exam.toString();

    assertThat(result).startsWith("Exam(")
        .contains("examRequest=")
        .contains("patient=")
        .contains("examMedicaments=[")
        .contains("examEquipments=[");
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
