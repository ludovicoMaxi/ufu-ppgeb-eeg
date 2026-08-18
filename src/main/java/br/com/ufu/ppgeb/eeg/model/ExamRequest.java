package br.com.ufu.ppgeb.eeg.model;

import java.util.Date;
import java.util.Objects;

import br.com.ufu.ppgeb.eeg.utils.CompareDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
 * Represents an exam request.
 */
@Getter
@Setter
@Entity
@Table(name = "EXAM_REQUEST")
@EntityListeners(AuditingEntityListener.class)
public class ExamRequest {

  @Id
  @SequenceGenerator(
      name = "EXAM_REQUEST_SQ",
      sequenceName = "EXAM_REQUEST_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "EXAM_REQUEST_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "MEDICAL_RECORD")
  private Long medicalRecord;

  @Column(name = "MEDICAL_REQUEST")
  private Long medicalRequest;

  @Column(name = "SECTOR")
  private String sector;

  @Column(name = "AGREEMENT")
  private String agreement;

  @Column(name = "DOCTOR_REQUESTANT")
  private String doctorRequestant;

  @Column(name = "USER_REQUEST")
  private String user;

  @Column(name = "CLINIC_ORIGIN")
  private String clinicOrigin;

  @Column(name = "CITY_ORIGIN")
  private String cityOrigin;

  @ManyToOne
  @JoinColumn(name = "PATIENT_ID", nullable = false)
  private Patient patient;

  @Column(name = "REQUEST_DATE")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date requestDate;

  @Column(name = "ACHIEVEMENT_DATE")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date achievementDate;

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
    if (!(o instanceof ExamRequest examRequest)) {
      return false;
    }
    return Objects.equals(getMedicalRecord(),
            examRequest.getMedicalRecord())
        && Objects.equals(getMedicalRequest(),
            examRequest.getMedicalRequest())
        && Objects.equals(getSector(), examRequest.getSector())
        && Objects.equals(getAgreement(),
            examRequest.getAgreement())
        && Objects.equals(getDoctorRequestant(),
            examRequest.getDoctorRequestant())
        && Objects.equals(getUser(), examRequest.getUser())
        && Objects.equals(getClinicOrigin(),
            examRequest.getClinicOrigin())
        && Objects.equals(getCityOrigin(),
            examRequest.getCityOrigin())
        && Objects.equals(getPatient(), examRequest.getPatient())
        && CompareDate.compareDates(getRequestDate(),
            examRequest.getRequestDate())
        && CompareDate.compareDates(getAchievementDate(),
            examRequest.getAchievementDate());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getMedicalRecord(),
        getMedicalRequest(),
        getSector(),
        getAgreement(),
        getDoctorRequestant(),
        getUser(),
        getClinicOrigin(),
        getCityOrigin(),
        getPatient(),
        getRequestDate(),
        getAchievementDate());
  }

  @Override
  public String toString() {

    return "ExamRequest{"
        + "id=" + id
        + ", medicalRecord=" + medicalRecord
        + ", medicalRequest=" + medicalRequest
        + ", sector='" + sector + '\''
        + ", agreement='" + agreement + '\''
        + ", doctorRequestant='"
        + doctorRequestant + '\''
        + ", user='" + user + '\''
        + ", clinicOrigin='"
        + clinicOrigin + '\''
        + ", cityOrigin='"
        + cityOrigin + '\''
        + ", patient=" + patient
        + ", requestDate=" + requestDate
        + ", achievementDate=" + achievementDate
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
