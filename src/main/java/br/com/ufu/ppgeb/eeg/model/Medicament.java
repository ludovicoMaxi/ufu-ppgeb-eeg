package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.isNull;

import java.time.ZonedDateTime;
import java.util.Objects;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a medicament.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "MEDICAMENT")
@EntityListeners(AuditingEntityListener.class)
public class Medicament {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "MEDICAMENT_SQ",
      sequenceName = "MEDICAMENT_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "MEDICAMENT_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

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
   * Sets the name in uppercase.
   *
   * @param name the name to set
   */
  public void setName(String name) {

    this.name = isNull(name) ? null : name.toUpperCase();
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof Medicament that)) {
      return false;
    }
    return Objects.equals(getName(), that.getName())
        && Objects.equals(getDescription(), that.getDescription());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getName(), getDescription());
  }

  @Override
  public String toString() {

    return "Medicament{"
        + "id=" + id
        + ", name=" + name
        + ", description=" + description
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
