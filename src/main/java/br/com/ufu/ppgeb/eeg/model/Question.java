package br.com.ufu.ppgeb.eeg.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Represents a question of the questionnaire catalog.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, exclude = {"questionAnswerOptions"})
@ToString(callSuper = true)
@Entity
@Table(name = "QUESTION")
public class Question extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "QUESTION_SQ",
      sequenceName = "QUESTION_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "QUESTION_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "TEXT", length = 512, nullable = false)
  private String text;

  @Column(name = "DESCRIPTION", length = 1024)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "QUESTION_TYPE", length = 32, nullable = false)
  private QuestionType questionType;

  @Column(name = "ACTIVE")
  private Boolean active;

  @OneToMany(mappedBy = "question", fetch = FetchType.EAGER,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  private List<QuestionAnswerOption> questionAnswerOptions;
}
