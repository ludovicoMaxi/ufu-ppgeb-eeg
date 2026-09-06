package br.com.ufu.ppgeb.eeg.model;

import java.util.Objects;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a unit.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "UNIT")
public class Unit {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "UNIT_SQ",
      sequenceName = "UNIT_SQ",
      allocationSize = 1,
      initialValue = 100)
  @GeneratedValue(
      generator = "UNIT_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof Unit unit)) {
      return false;
    }
    return Objects.equals(getName(), unit.getName())
        && Objects.equals(getDescription(), unit.getDescription());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getName(), getDescription());
  }

  @Override
  public String toString() {

    return "Unit{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", description='" + description + '\''
        + '}';
  }
}
