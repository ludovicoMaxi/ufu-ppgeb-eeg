package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.nonNull;

import java.time.ZonedDateTime;
import java.util.Objects;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents exam medicament association.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
@Table(name = "EXAM_MEDICAMENT", indexes = {
    @Index(name = "EXAM_MEDICAMENT_EXAM_IDX", columnList = "EXAM_ID"),
    @Index(name = "EXAM_MEDICAMENT_MEDICAMENT_IDX", columnList = "MEDICAMENT_ID"),
    @Index(name = "EXAM_MEDICAMENT_UNIT_IDX", columnList = "UNIT_ID")})
@EntityListeners(AuditingEntityListener.class)
public class ExamMedicament {

  @Id
  @SequenceGenerator(
      name = "EXAM_MEDICAMENT_SQ",
      sequenceName = "EXAM_MEDICAMENT_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "EXAM_MEDICAMENT_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "EXAM_ID", nullable = false)
  @JsonBackReference
  private Exam exam;

  @ManyToOne(optional = false)
  @JoinColumn(name = "MEDICAMENT_ID", nullable = false)
  private Medicament medicament;

  @Column(name = "AMOUNT", nullable = false)
  private Long amount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "UNIT_ID", nullable = false)
  private Unit unit;

  @CreatedDate
  @Column(name = "CREATED_AT", nullable = false,
      updatable = false)
  @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
  private ZonedDateTime createdAt;

  @CreatedBy
  @Column(name = "CREATED_BY", nullable = false,
      updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
  private ZonedDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "UPDATED_BY", length = 20)
  private String updatedBy;

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof ExamMedicament that)) {
      return false;
    }
    return Objects.equals(getExamId(), that.getExamId())
        && Objects.equals(getMedicament(), that.getMedicament())
        && Objects.equals(getAmount(), that.getAmount())
        && Objects.equals(getUnit(), that.getUnit());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getExam(), getMedicament(),
        getAmount(), getUnit());
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

    return "ExamMedicament{"
        + "id=" + id
        + ", examId=" + getExamId()
        + ", medicament=" + medicament
        + ", amount=" + amount
        + ", unit=" + unit
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
