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
 * Represents a contact.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "CONTACT")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Contact extends AuditableEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "CONTACT_SQ",
      sequenceName = "CONTACT_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "CONTACT_SQ",
      strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NAME", length = 256, nullable = false)
  private String name;

  @Column(name = "FLAG_ACTIVE", nullable = false)
  private Boolean active;

  @Column(name = "OBJECT_ID", nullable = false)
  private Long objectId;

  @Column(name = "OBJECT_TYPE", nullable = false)
  private Long objectType;

  @Column(name = "PHONE", length = 20)
  private String phone;

  @Column(name = "CELLPHONE", length = 20)
  private String cellphone;

  @Column(name = "WHATSAPP", length = 20)
  private String whatsapp;

  @Column(name = "FACEBOOK", length = 200)
  private String facebook;

  @Column(name = "INSTAGRAM", length = 200)
  private String instagram;

  @Column(name = "EMAIL", length = 100)
  private String email;

  @Column(name = "MAIN")
  private Boolean main;
}
