package br.com.ufu.ppgeb.eeg.model;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Represents an exam.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "EXAM", indexes = {
    @Index(name = "EXAM_PATIENT_IDX", columnList = "PATIENT_ID"),
    @Index(name = "EXAM_REQUEST_ID_IDX", columnList = "EXAM_REQUEST_ID")})
@EqualsAndHashCode(callSuper = false, exclude = {"examMedicaments", "examEquipments"})
@ToString(callSuper = true)
public class Exam extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
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

  /**
   * Constructs an Exam with the given id.
   *
   * @param id the exam id
   */
  public Exam(Long id) {

    this.id = id;
  }
}
