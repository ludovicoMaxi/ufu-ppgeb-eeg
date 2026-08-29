package br.com.ufu.ppgeb.eeg.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import br.com.ufu.ppgeb.eeg.utils.CompareDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents an exam.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "EXAM", indexes = {
    @Index(name = "EXAM_PATIENT_IDX", columnList = "PATIENT_ID"),
    @Index(name = "EXAM_REQUEST_ID_IDX", columnList = "EXAM_REQUEST_ID")})
@EntityListeners(AuditingEntityListener.class)
public class Exam {

  @Id
  @SequenceGenerator(
      name = "EXAM_SQ",
      sequenceName = "EXAM_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "EXAM_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @OneToOne
  @JoinColumn(name = "EXAM_REQUEST_ID")
  private ExamRequest examRequest;

  @ManyToOne(optional = false)
  @JoinColumn(name = "PATIENT_ID", nullable = false)
  private Patient patient;

  @Column(name = "ACHIEVEMENT_DATE", nullable = false)
  @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
  private ZonedDateTime achievementDate;

  @Column(name = "MEDICAL_REPORT", length = 256)
  private String medicalReport;

  @Column(name = "CONCLUSION", length = 256)
  private String conclusion;

  @Column(name = "BED", length = 256)
  private String bed;

  @Column(name = "HEIGHT")
  private Long height;

  @Column(name = "WEIGHT")
  private Double weight;

  @Column(name = "CLINICAL_DATA", length = 256)
  private String clinicalData;

  @OneToMany(mappedBy = "exam", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @JsonManagedReference
  private List<ExamMedicament> examMedicaments;

  @OneToMany(mappedBy = "exam", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @JsonManagedReference
  private List<ExamEquipment> examEquipments;

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

  /**
   * Constructs an Exam with the given id.
   *
   * @param id the exam id
   */
  public Exam(Long id) {

    this.id = id;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof Exam exam)) {
      return false;
    }
    return Objects.equals(getId(), exam.getId())
        && Objects.equals(getExamRequest(), exam.getExamRequest())
        && Objects.equals(getPatient(), exam.getPatient())
        && CompareDate.compareDates(getAchievementDate(), exam.getAchievementDate())
        && Objects.equals(getMedicalReport(), exam.getMedicalReport())
        && Objects.equals(getConclusion(), exam.getConclusion())
        && Objects.equals(getBed(), exam.getBed())
        && Objects.equals(getHeight(), exam.getHeight())
        && Objects.equals(getWeight(), exam.getWeight())
        && Objects.equals(getClinicalData(), exam.getClinicalData());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId(),
        getPatient(),
        getAchievementDate(),
        getMedicalReport(),
        getConclusion(),
        getBed(),
        getHeight(),
        getWeight(),
        getClinicalData());
  }

  @Override
  public String toString() {

    return "Exam{"
        + "id=" + id
        + ", examRequest=" + examRequest
        + ", patient=" + patient
        + ", achievementDate=" + achievementDate
        + ", medicalReport='" + medicalReport + '\''
        + ", conclusion='" + conclusion + '\''
        + ", bed='" + bed + '\''
        + ", height=" + height
        + ", weight=" + weight
        + ", clinicalData='" + clinicalData + '\''
        + ", examMedicaments=" + examMedicaments
        + ", examEquipments=" + examEquipments
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
