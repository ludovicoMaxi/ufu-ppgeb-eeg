package br.com.ufu.ppgeb.eeg.model;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a question of the questionnaire catalog.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"questionAnswerOptions",
    "createdAt", "createdBy", "updatedAt", "updatedBy"})
@ToString(exclude = {"createdAt", "createdBy", "updatedAt", "updatedBy"})
@Entity
@Table(name = "QUESTION")
@EntityListeners(AuditingEntityListener.class)
public class Question {

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
}