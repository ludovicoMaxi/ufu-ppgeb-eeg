package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents exam equipment association.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
@Table(name = "EXAM_EQUIPMENT", indexes = {
    @Index(name = "EXAM_EQUIPMENT_EXAM_IDX", columnList = "EXAM_ID"),
    @Index(name = "EXAM_EQUIPMENT_EQUIPMENT_IDX", columnList = "EQUIPMENT_ID"),
    @Index(name = "EXAM_EQUIPMENT_UNIT_IDX", columnList = "UNIT_ID")})
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class ExamEquipment extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "EXAM_EQUIPMENT_SQ",
      sequenceName = "EXAM_EQUIPMENT_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "EXAM_EQUIPMENT_SQ",
      strategy = GenerationType.SEQUENCE)
  @ToString.Include
  @EqualsAndHashCode.Exclude
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "EXAM_ID", nullable = false)
  @JsonBackReference
  @EqualsAndHashCode.Exclude
  private Exam exam;

  @ManyToOne(optional = false)
  @JoinColumn(name = "EQUIPMENT_ID", nullable = false)
  @ToString.Include
  private Equipment equipment;

  @Column(name = "AMOUNT", nullable = false)
  @ToString.Include
  private Long amount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "UNIT_ID", nullable = false)
  @ToString.Include
  private Unit unit;

  /**
   * Gets the exam id.
   *
   * @return the exam id
   */
  @EqualsAndHashCode.Include
  @ToString.Include
  public Long getExamId() {

    if (nonNull(exam)) {
      return exam.getId();
    }
    return null;
  }
}
