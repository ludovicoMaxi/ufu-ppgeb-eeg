package br.com.ufu.ppgeb.eeg.model;

import br.com.ufu.ppgeb.eeg.utils.CompareDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
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
@Entity
@Table(name = "PATIENT")
@EntityListeners(AuditingEntityListener.class)
public class Patient {

  @Id
  @Column(name = "ID")
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

  @Column(name = "SEX")
  private char sex;

  @Column(name = "BIRTHDATE", nullable = false)
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date birthDate;

  @Column(name = "NACIONALITY", length = 20)
  private String nacionality;

  @Column(name = "CIVIL_STATUS", length = 20)
  private String civilStatus;

  @Column(name = "JOB", length = 256)
  private String job;

  @CreatedDate
  @Column(name = "CREATED_AT", nullable = false,
      updatable = false)
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date createdAt;

  @CreatedBy
  @Column(name = "CREATED_BY", nullable = false,
      updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date updatedAt;

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
    return getSex() == patient.getSex()
        && Objects.equals(
            getName(), patient.getName())
        && Objects.equals(getDocumentNumber(),
            patient.getDocumentNumber())
        && CompareDate.compareDates(
            getBirthDate(), patient.getBirthDate())
        && Objects.equals(getNacionality(),
            patient.getNacionality())
        && Objects.equals(getCivilStatus(),
            patient.getCivilStatus())
        && Objects.equals(getJob(), patient.getJob());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getName(),
        getDocumentNumber(),
        getSex(),
        getBirthDate(),
        getNacionality(),
        getCivilStatus(),
        getJob());
  }

  @Override
  public String toString() {

    return "Patient{"
        + "name='" + name + '\''
        + ", documentNumber='"
        + documentNumber + '\''
        + ", sex=" + sex
        + ", birthDate=" + birthDate
        + ", nacionality='"
        + nacionality + '\''
        + ", civilStatus='"
        + civilStatus + '\''
        + ", job='" + job + '\''
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
