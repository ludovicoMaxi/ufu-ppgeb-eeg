package br.com.ufu.ppgeb.eeg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an object type for contacts.
 */
@Getter
@Setter
@Entity
@Table(name = "OBJECT_TYPE")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class ObjectType {

  public static final ObjectType PATIENT =
      new ObjectType(1L);

  public static final ObjectType EXAM =
      new ObjectType(2L);

  @Id
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NAME", length = 256, nullable = false)
  private String name;

  @Column(name = "DESCRIPTION")
  private Long description;

  /**
   * Default constructor.
   */
  public ObjectType() {

  }

  /**
   * Constructs an ObjectType with the given id.
   *
   * @param id the object type id
   */
  public ObjectType(Long id) {

    this.id = id;
  }
}
