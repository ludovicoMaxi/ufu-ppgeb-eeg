package br.com.ufu.ppgeb.eeg.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Represents a questionnaire applied to an object (e.g., an exam).
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, exclude = {"questionnaireQuestions"})
@ToString(callSuper = true)
@Entity
@Table(name = "QUESTIONNAIRE")
public class Questionnaire extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "QUESTIONNAIRE_SQ",
      sequenceName = "QUESTIONNAIRE_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "QUESTIONNAIRE_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "TITLE", length = 256, nullable = false)
  private String title;

  @Column(name = "DESCRIPTION", length = 1024)
  private String description;

  @OneToMany(mappedBy = "questionnaire", fetch = FetchType.EAGER,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  private List<QuestionnaireQuestion> questionnaireQuestions;
}
