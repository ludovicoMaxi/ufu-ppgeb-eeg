package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a selected answer option (catalog reference) of a questionnaire answer.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"questionnaireAnswer"})
@ToString(callSuper = true, exclude = {"questionnaireAnswer"})
@Entity
@Table(name = "QUESTIONNAIRE_ANSWER_OPTION", indexes = {
    @Index(name = "QUESTIONNAIRE_ANSWER_OPTION_ANSWER_IDX",
        columnList = "QUESTIONNAIRE_ANSWER_ID"),
    @Index(name = "QUESTIONNAIRE_ANSWER_OPTION_OPTION_IDX",
        columnList = "ANSWER_OPTION_ID")},
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_QUESTIONNAIRE_ANSWER_OPTION",
            columnNames = {"QUESTIONNAIRE_ANSWER_ID", "ANSWER_OPTION_ID"})})
public class QuestionnaireAnswerOption extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "QUESTIONNAIRE_ANSWER_OPTION_SQ",
      sequenceName = "QUESTIONNAIRE_ANSWER_OPTION_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "QUESTIONNAIRE_ANSWER_OPTION_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "QUESTIONNAIRE_ANSWER_ID", nullable = false)
  @JsonBackReference
  private QuestionnaireAnswer questionnaireAnswer;

  @ManyToOne(optional = false)
  @JoinColumn(name = "ANSWER_OPTION_ID", nullable = false)
  private AnswerOption answerOption;

  /**
   * Gets the questionnaire answer id.
   *
   * @return the questionnaire answer id
   */
  public Long getQuestionnaireAnswerId() {

    if (nonNull(questionnaireAnswer)) {
      return questionnaireAnswer.getId();
    }
    return null;
  }
}
