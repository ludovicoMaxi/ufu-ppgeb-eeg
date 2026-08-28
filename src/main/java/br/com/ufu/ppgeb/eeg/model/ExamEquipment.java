package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.nonNull;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents exam equipment association.
 */
@Getter
@Setter
@Entity
@Table(name = "EXAM_EQUIPMENT", indexes = {
    @Index(name = "EXAM_EQUIPMENT_EXAM_IDX", columnList = "EXAM_ID"),
    @Index(name = "EXAM_EQUIPMENT_EQUIPMENT_IDX", columnList = "EQUIPMENT_ID"),
    @Index(name = "EXAM_EQUIPMENT_UNIT_IDX", columnList = "UNIT_ID")})
@EntityListeners(AuditingEntityListener.class)
public class ExamEquipment {

  @Id
  @SequenceGenerator(
      name = "EXAM_EQUIPMENT_SQ",
      sequenceName = "EXAM_EQUIPMENT_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "EXAM_EQUIPMENT_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "EXAM_ID", nullable = false)
  @JsonBackReference
  private Exam exam;

  @ManyToOne(optional = false)
  @JoinColumn(name = "EQUIPMENT_ID", nullable = false)
  private Equipment equipment;

  @Column(name = "AMOUNT", nullable = false)
  private Long amount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "UNIT_ID", nullable = false)
  private Unit unit;

  @CreatedDate
  @Column(name = "CREATED_AT", nullable = false,
      updatable = false)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private ZonedDateTime createdAt;

  @CreatedBy
  @Column(name = "CREATED_BY", nullable = false,
      updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private ZonedDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "UPDATED_BY", length = 20)
  private String updatedBy;

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof ExamEquipment that)) {
      return false;
    }
    return Objects.equals(getExamId(), that.getExamId())
        && Objects.equals(getEquipment(), that.getEquipment())
        && Objects.equals(getAmount(), that.getAmount())
        && Objects.equals(getUnit(), that.getUnit());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getExam(), getEquipment(), getAmount());
  }

  /**
   * Gets the exam id.
   *
   * @return the exam id
   */
  public Long getExamId() {

    if (nonNull(exam)) {
      return exam.getId();
    }
    return null;
  }

  @Override
  public String toString() {

    return "ExamEquipment{"
        + "id=" + id
        + ", examId=" + getExamId()
        + ", equipment=" + equipment
        + ", amount=" + amount
        + ", unit=" + unit
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
