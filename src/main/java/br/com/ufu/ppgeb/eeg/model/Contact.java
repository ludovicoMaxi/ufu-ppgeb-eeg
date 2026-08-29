package br.com.ufu.ppgeb.eeg.model;

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
 * Represents a contact.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "CONTACT")
@EntityListeners(AuditingEntityListener.class)
public class Contact {

  @Id
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

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof Contact contact)) {
      return false;
    }
    return Objects.equals(id, contact.id)
        && Objects.equals(name, contact.name)
        && Objects.equals(active, contact.active)
        && Objects.equals(objectId, contact.objectId)
        && Objects.equals(objectType, contact.objectType)
        && Objects.equals(phone, contact.phone)
        && Objects.equals(cellphone, contact.cellphone)
        && Objects.equals(whatsapp, contact.whatsapp)
        && Objects.equals(facebook, contact.facebook)
        && Objects.equals(instagram, contact.instagram)
        && Objects.equals(email, contact.email)
        && Objects.equals(main, contact.main);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, name, active, objectId,
        objectType, phone, cellphone, whatsapp,
        facebook, instagram, email, main);
  }

  @Override
  public String toString() {

    return "Contact{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", active=" + active
        + ", objectId=" + objectId
        + ", objectType=" + objectType
        + ", phone='" + phone + '\''
        + ", cellphone='" + cellphone + '\''
        + ", whatsapp='" + whatsapp + '\''
        + ", facebook='" + facebook + '\''
        + ", instagram='" + instagram + '\''
        + ", email='" + email + '\''
        + ", main=" + main
        + ", createdAt=" + createdAt
        + ", createdBy='" + createdBy + '\''
        + ", updatedAt=" + updatedAt
        + ", updatedBy='" + updatedBy + '\''
        + '}';
  }
}
