package br.com.ufu.ppgeb.eeg.model;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents an answer option of the questionnaire catalog.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude = {"createdAt", "createdBy", "updatedAt", "updatedBy"})
@ToString(exclude = {"createdAt", "createdBy", "updatedAt", "updatedBy"})
@Entity
@Table(name = "ANSWER_OPTION")
@EntityListeners(AuditingEntityListener.class)
public class AnswerOption {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "ANSWER_OPTION_SQ",
      sequenceName = "ANSWER_OPTION_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "ANSWER_OPTION_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "TEXT", length = 512, nullable = false)
  private String text;

  @Column(name = "ACTIVE")
  private Boolean active;

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