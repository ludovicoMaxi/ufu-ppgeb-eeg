package br.com.ufu.ppgeb.eeg.model;

import java.util.List;

import jakarta.persistence.CascadeType;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Represents an answer given to a questionnaire question for an object (e.g., an exam).
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"answerOptions"})
@ToString(callSuper = true)
@Entity
@Table(name = "QUESTIONNAIRE_ANSWER", indexes = {
    @Index(name = "QUESTIONNAIRE_ANSWER_OBJ_CTX_IDX",
        columnList = "QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID"),
    @Index(name = "QUESTIONNAIRE_ANSWER_QUESTION_IDX", columnList = "QUESTION_ID")},
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_QUESTIONNAIRE_ANSWER",
            columnNames = {"QUESTIONNAIRE_ID", "QUESTION_ID",
                "OBJECT_TYPE", "OBJECT_ID"})})
public class QuestionnaireAnswer extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "QUESTIONNAIRE_ANSWER_SQ",
      sequenceName = "QUESTIONNAIRE_ANSWER_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "QUESTIONNAIRE_ANSWER_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "QUESTIONNAIRE_ID", nullable = false)
  private Questionnaire questionnaire;

  @ManyToOne(optional = false)
  @JoinColumn(name = "QUESTION_ID", nullable = false)
  private Question question;

  @Column(name = "OBJECT_TYPE", nullable = false)
  private Long objectType;

  @Column(name = "OBJECT_ID", nullable = false)
  private Long objectId;

  @Column(name = "FREE_TEXT", length = 2048)
  private String freeText;

  @OneToMany(mappedBy = "questionnaireAnswer", fetch = FetchType.EAGER,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  private List<QuestionnaireAnswerOption> answerOptions;
}
