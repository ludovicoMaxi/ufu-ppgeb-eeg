package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.nonNull;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents the link between a question and a catalog answer option.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"question",
    "createdAt", "createdBy", "updatedAt", "updatedBy"})
@ToString(exclude = {"question",
    "createdAt", "createdBy", "updatedAt", "updatedBy"})
@Entity
@Table(name = "QUESTION_ANSWER_OPTION", indexes = {
    @Index(name = "QUESTION_ANSWER_OPTION_QUESTION_IDX", columnList = "QUESTION_ID"),
    @Index(name = "QUESTION_ANSWER_OPTION_OPTION_IDX", columnList = "ANSWER_OPTION_ID")},
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_QUESTION_ANSWER_OPTION",
            columnNames = {"QUESTION_ID", "ANSWER_OPTION_ID"})})
@EntityListeners(AuditingEntityListener.class)
public class QuestionAnswerOption {

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