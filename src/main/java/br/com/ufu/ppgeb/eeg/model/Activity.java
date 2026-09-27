package br.com.ufu.ppgeb.eeg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

/**
 * Represents an activity in an exam.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "ACTIVITY")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Activity extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "ACTIVITY_SQ",
      sequenceName = "ACTIVITY_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "ACTIVITY_SQ",
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
}
