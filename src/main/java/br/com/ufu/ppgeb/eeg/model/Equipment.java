package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.isNull;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents an equipment.
 */
@Getter
@Setter
@Entity
@Table(name = "EQUIPMENT")
@EntityListeners(AuditingEntityListener.class)
public class Equipment {

  @Id
  @SequenceGenerator(
      name = "EQUIPMENT_SQ",
      sequenceName = "EQUIPMENT_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "EQUIPMENT_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @CreatedDate
  @Column(name = "CREATED_AT", nullable = false,
      updatable = false)
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date createdAt;

  @CreatedBy
  @Column(name = "CREATED_BY", nullable = false,
      updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date updatedAt;

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
    if (!(o instanceof Equipment equipment)) {
      return false;
    }
    return Objects.equals(getName(), equipment.getName())
        && Objects.equals(getDescription(), equipment.getDescription());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getName(), getDescription());
  }

  @Override
  public String toString() {

    return "Equipment{"
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
