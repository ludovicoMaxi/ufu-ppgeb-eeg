package br.com.ufu.ppgeb.eeg.model;

import static java.util.Objects.isNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@ToString(callSuper = true)
public class Medicament extends AuditableEntity {

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

  /**
   * Sets the name in uppercase.
   *
   * @param name the name to set
   */
  public void setName(String name) {

    this.name = isNull(name) ? null : name.toUpperCase();
  }
}
