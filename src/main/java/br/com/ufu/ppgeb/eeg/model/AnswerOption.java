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
 * Represents an answer option of the questionnaire catalog.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "ANSWER_OPTION")
public class AnswerOption extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "ANSWER_OPTION_SQ",
      sequenceName = "ANSWER_OPTION_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "ANSWER_OPTION_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "TEXT", length = 512, nullable = false)
  private String text;

  @Column(name = "ACTIVE")
  private Boolean active;
}
