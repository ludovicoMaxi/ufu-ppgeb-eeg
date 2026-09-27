package br.com.ufu.ppgeb.eeg.model;

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
 * Represents a unit.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "UNIT")
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@ToString(callSuper = false)
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
}
