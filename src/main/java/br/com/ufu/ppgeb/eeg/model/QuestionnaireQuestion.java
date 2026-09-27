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
 * Represents the link between a questionnaire and a question.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"questionnaire"})
@ToString(callSuper = true, exclude = {"questionnaire"})
@Entity
@Table(name = "QUESTIONNAIRE_QUESTION", indexes = {
    @Index(name = "QUESTIONNAIRE_QUESTION_QUESTIONNAIRE_IDX",
        columnList = "QUESTIONNAIRE_ID"),
    @Index(name = "QUESTIONNAIRE_QUESTION_QUESTION_IDX", columnList = "QUESTION_ID")},
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_QUESTIONNAIRE_QUESTION",
            columnNames = {"QUESTIONNAIRE_ID", "QUESTION_ID"})})
public class QuestionnaireQuestion extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "QUESTIONNAIRE_QUESTION_SQ",
      sequenceName = "QUESTIONNAIRE_QUESTION_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "QUESTIONNAIRE_QUESTION_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "QUESTIONNAIRE_ID", nullable = false)
  @JsonBackReference
  private Questionnaire questionnaire;

  @ManyToOne(optional = false)
  @JoinColumn(name = "QUESTION_ID", nullable = false)
  private Question question;

  @Column(name = "SORT_ORDER")
  private Long sortOrder;

  /**
   * Gets the questionnaire id.
   *
   * @return the questionnaire id
   */
  public Long getQuestionnaireId() {

    if (nonNull(questionnaire)) {
      return questionnaire.getId();
    }
    return null;
  }
}
