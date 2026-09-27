package br.com.ufu.ppgeb.eeg.model;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
 * Represents a questionnaire applied to an object (e.g., an exam).
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"questionnaireQuestions",
    "createdAt", "createdBy", "updatedAt", "updatedBy"})
@ToString(exclude = {"createdAt", "createdBy", "updatedAt", "updatedBy"})
@Entity
@Table(name = "QUESTIONNAIRE")
@EntityListeners(AuditingEntityListener.class)
public class Questionnaire {

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