package br.com.ufu.ppgeb.eeg.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
 * Represents an epoch in an exam.
 */
@Getter
@Setter
@Entity
@Table(name = "EPOCH")
@EntityListeners(AuditingEntityListener.class)
public class Epoch {

  @Id
  @SequenceGenerator(name = "EPOCH_SQ",
      sequenceName = "EPOCH_SQ",
      allocationSize = 1)
  @GeneratedValue(generator = "EPOCH_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "EXAM_ID", nullable = false)
  private Long examId;

  @Column(name = "START_TIME", nullable = false)
  private Long startTime;

  @Column(name = "DURATION", nullable = false)
  private Long duration;

  @Column(name = "DESCRIPTION", length = 1024, nullable = false)
  private String description;

  @CreatedDate
  @Column(name = "CREATED_AT", nullable = false,
      updatable = false)
  @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
  private ZonedDateTime createdAt;

  @CreatedBy
  @Column(name = "CREATED_BY", length = 20,
      nullable = false, updatable = false)
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
    if (!(o instanceof Epoch epoch)) {
      return false;
    }
    return Objects.equals(getId(), epoch.getId())
        && Objects.equals(getExamId(), epoch.getExamId())
        && Objects.equals(getStartTime(), epoch.getStartTime())
        && Objects.equals(getDuration(), epoch.getDuration())
        && Objects.equals(getDescription(), epoch.getDescription());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId(), getExamId(),
        getStartTime(), getDuration(), getDescription(),
        getCreatedAt(), getCreatedBy(),
        getUpdatedAt(), getUpdatedBy());
  }

  @Override
  public String toString() {

    return "Epoch{"
        + "id=" + id
        + ", examId=" + examId
        + ", startTime=" + startTime
        + ", duration=" + duration
        + ", description=" + description
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
