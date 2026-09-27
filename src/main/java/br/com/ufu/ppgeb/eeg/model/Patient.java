package br.com.ufu.ppgeb.eeg.model;

import java.time.LocalDate;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@ToString(callSuper = true, exclude = {"id"})
public class Patient extends AuditableEntity {

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
}
