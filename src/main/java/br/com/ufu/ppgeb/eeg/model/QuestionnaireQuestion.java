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
 * Represents the link between a questionnaire and a question.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"questionnaire",
    "createdAt", "createdBy", "updatedAt", "updatedBy"})
@ToString(exclude = {"questionnaire",
    "createdAt", "createdBy", "updatedAt", "updatedBy"})
@Entity
@Table(name = "QUESTIONNAIRE_QUESTION", indexes = {
    @Index(name = "QUESTIONNAIRE_QUESTION_QUESTIONNAIRE_IDX",
        columnList = "QUESTIONNAIRE_ID"),
    @Index(name = "QUESTIONNAIRE_QUESTION_QUESTION_IDX", columnList = "QUESTION_ID")},
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_QUESTIONNAIRE_QUESTION",
            columnNames = {"QUESTIONNAIRE_ID", "QUESTION_ID"})})
@EntityListeners(AuditingEntityListener.class)
public class QuestionnaireQuestion {

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