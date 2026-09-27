package br.com.ufu.ppgeb.eeg.model;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * Represents an exam request.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "EXAM_REQUEST", indexes = {
    @Index(name = "EXAM_REQUEST_PATIENT_IDX", columnList = "PATIENT_ID")})
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@ToString(callSuper = true)
public class ExamRequest extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
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

  @ManyToOne(optional = false)
  @JoinColumn(name = "PATIENT_ID", nullable = false)
  private Patient patient;

  @Column(name = "REQUEST_DATE")
  @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
  private ZonedDateTime requestDate;

  @Column(name = "ACHIEVEMENT_DATE")
  @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
  private ZonedDateTime achievementDate;
}
