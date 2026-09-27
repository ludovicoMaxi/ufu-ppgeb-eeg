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
 * Represents the link between a question and a catalog answer option.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"question"})
@ToString(callSuper = true, exclude = {"question"})
@Entity
@Table(name = "QUESTION_ANSWER_OPTION", indexes = {
    @Index(name = "QUESTION_ANSWER_OPTION_QUESTION_IDX", columnList = "QUESTION_ID"),
    @Index(name = "QUESTION_ANSWER_OPTION_OPTION_IDX", columnList = "ANSWER_OPTION_ID")},
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_QUESTION_ANSWER_OPTION",
            columnNames = {"QUESTION_ID", "ANSWER_OPTION_ID"})})
public class QuestionAnswerOption extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "QUESTION_ANSWER_OPTION_SQ",
      sequenceName = "QUESTION_ANSWER_OPTION_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "QUESTION_ANSWER_OPTION_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "QUESTION_ID", nullable = false)
  @JsonBackReference
  private Question question;

  @ManyToOne(optional = false)
  @JoinColumn(name = "ANSWER_OPTION_ID", nullable = false)
  private AnswerOption answerOption;

  @Column(name = "SORT_ORDER")
  private Long sortOrder;

  /**
   * Gets the question id.
   *
   * @return the question id
   */
  public Long getQuestionId() {

    if (nonNull(question)) {
      return question.getId();
    }
    return null;
  }
}
