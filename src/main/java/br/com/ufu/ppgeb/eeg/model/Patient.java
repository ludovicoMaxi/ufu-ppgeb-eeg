package br.com.ufu.ppgeb.eeg.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a patient.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "PATIENT")
@EntityListeners(AuditingEntityListener.class)
public class Patient {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "PATIENT_SQ",
      sequenceName = "PATIENT_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "PATIENT_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NAME", length = 512, nullable = false)
  private String name;

  @Column(name = "DOCUMENT_NUMBER", length = 20,
      nullable = false)
  private String documentNumber;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "SEX", length = 10)
  private Sex sex;

  @Column(name = "BIRTHDATE", nullable = false)
  @JsonFormat(pattern = DateFormats.ISO_DATE)
  private LocalDate birthDate;

  @Column(name = "NATIONALITY", length = 20)
  private String nationality;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "CIVIL_STATUS", length = 25)
  private CivilStatus civilStatus;

  @Column(name = "JOB", length = 256)
  private String job;

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
    if (!(o instanceof Patient patient)) {
      return false;
    }
    return Objects.equals(getSex(), patient.getSex())
        && Objects.equals(getName(), patient.getName())
        && Objects.equals(getDocumentNumber(), patient.getDocumentNumber())
        && Objects.equals(getBirthDate(), patient.getBirthDate())
        && Objects.equals(getNationality(), patient.getNationality())
        && Objects.equals(getCivilStatus(), patient.getCivilStatus())
        && Objects.equals(getJob(), patient.getJob());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getName(),
        getDocumentNumber(),
        getSex(),
        getBirthDate(),
        getNationality(),
        getCivilStatus(),
        getJob());
  }

  @Override
  public String toString() {

    return "Patient{"
        + "name='" + name + '\''
        + ", documentNumber='" + documentNumber + '\''
        + ", sex=" + sex
        + ", birthDate=" + birthDate
        + ", nationality='" + nationality + '\''
        + ", civilStatus='" + civilStatus + '\''
        + ", job='" + job + '\''
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
