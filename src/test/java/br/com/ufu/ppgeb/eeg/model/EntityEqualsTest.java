package br.com.ufu.ppgeb.eeg.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntityEqualsTest {

  private static final Long ID = 1L;
  private static final Long OTHER_ID = 2L;
  private static final String TEXT = "text";
  private static final String OTHER_TEXT = "other";
  private static final Long AMOUNT = 10L;

  @Test
  @DisplayName("Given patients with the same business fields and different ids when comparing "
      + "then be equal and share the hash code")
  void givenPatientsWithSameBusinessFieldsAndDifferentIds_whenComparing_thenBeEqualAndShare() {
    Patient first = Patient.builder().id(ID).name(TEXT).documentNumber(TEXT).build();
    Patient second =
        Patient.builder().id(OTHER_ID).name(TEXT).documentNumber(TEXT).build();

    assertThat(first).isEqualTo(second);
    assertThat(first).hasSameHashCodeAs(second);
  }

  @Test
  @DisplayName("Given medicaments with the same name and different ids when comparing then be "
      + "equal and share the hash code")
  void givenMedicamentsWithSameNameAndDifferentIds_whenComparing_thenBeEqualAndShare() {
    Medicament first = Medicament.builder().id(ID).name(TEXT).build();
    Medicament second = Medicament.builder().id(OTHER_ID).name(TEXT).build();

    assertThat(first).isEqualTo(second);
    assertThat(first).hasSameHashCodeAs(second);
  }

  @Test
  @DisplayName("Given units with the same name and different ids when comparing then be equal "
      + "and share the hash code")
  void givenUnitsWithSameNameAndDifferentIds_whenComparing_thenBeEqualAndShare() {
    Unit first = Unit.builder().id(ID).name(TEXT).build();
    Unit second = Unit.builder().id(OTHER_ID).name(TEXT).build();

    assertThat(first).isEqualTo(second);
    assertThat(first).hasSameHashCodeAs(second);
  }

  @Test
  @DisplayName("Given exams with the same id and different conclusions when comparing then not "
      + "be equal")
  void givenExamsWithSameIdAndDifferentConclusions_whenComparing_thenNotBeEqual() {
    Exam first = Exam.builder().id(ID).conclusion(TEXT).build();
    Exam second = Exam.builder().id(ID).conclusion(OTHER_TEXT).build();

    assertThat(first).isNotEqualTo(second);
  }

  @Test
  @DisplayName("Given exam medicaments pointing to distinct exams with the same id when "
      + "comparing then be equal and share the hash code")
  void givenExamMedicamentsPointingToDistinctExamsWithSameId_whenComparing_thenBeEqualAndShare() {
    ExamMedicament first = examMedicament(exam(ID, TEXT));
    ExamMedicament second = examMedicament(exam(ID, OTHER_TEXT));

    assertThat(first).isEqualTo(second);
    assertThat(first).hasSameHashCodeAs(second);
  }

  @Test
  @DisplayName("Given exam equipments pointing to distinct exams with the same id when "
      + "comparing then be equal and share the hash code")
  void givenExamEquipmentsPointingToDistinctExamsWithSameId_whenComparing_thenBeEqualAndShare() {
    ExamEquipment first = examEquipment(exam(ID, TEXT));
    ExamEquipment second = examEquipment(exam(ID, OTHER_TEXT));

    assertThat(first).isEqualTo(second);
    assertThat(first).hasSameHashCodeAs(second);
  }

  @Test
  @DisplayName("Given object types with the same fields and different ids when comparing then "
      + "not be equal")
  void givenObjectTypesWithSameFieldsAndDifferentIds_whenComparing_thenNotBeEqual() {
    ObjectType first = new ObjectType(ID);
    first.setName(TEXT);
    ObjectType second = new ObjectType(OTHER_ID);
    second.setName(TEXT);

    assertThat(first).isNotEqualTo(second);
  }

  @Test
  @DisplayName("Given entities with all null fields when comparing then be equal and share "
      + "the hash code")
  void givenEntitiesWithAllNullFields_whenComparing_thenBeEqualAndShareTheHashCode() {
    assertThat(Exam.builder().build()).isEqualTo(Exam.builder().build());
    assertThat(Exam.builder().build()).hasSameHashCodeAs(Exam.builder().build());
    assertThat(Contact.builder().build()).isEqualTo(Contact.builder().build());
    assertThat(Contact.builder().build()).hasSameHashCodeAs(Contact.builder().build());
    assertThat(Activity.builder().build()).isEqualTo(Activity.builder().build());
    assertThat(Activity.builder().build()).hasSameHashCodeAs(Activity.builder().build());
  }

  private static Exam exam(Long id, String conclusion) {
    return Exam.builder().id(id).conclusion(conclusion).build();
  }

  private static ExamMedicament examMedicament(Exam exam) {
    ExamMedicament examMedicament = new ExamMedicament();
    examMedicament.setExam(exam);
    examMedicament.setMedicament(Medicament.builder().name(TEXT).build());
    examMedicament.setAmount(AMOUNT);
    examMedicament.setUnit(Unit.builder().name(TEXT).build());
    return examMedicament;
  }

  private static ExamEquipment examEquipment(Exam exam) {
    ExamEquipment examEquipment = new ExamEquipment();
    examEquipment.setExam(exam);
    examEquipment.setEquipment(Equipment.builder().name(TEXT).build());
    examEquipment.setAmount(AMOUNT);
    examEquipment.setUnit(Unit.builder().name(TEXT).build());
    return examEquipment;
  }
}
