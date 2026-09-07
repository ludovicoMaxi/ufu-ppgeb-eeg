package br.com.ufu.ppgeb.eeg.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an idempotency record used to replay create operations.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "IDEMPOTENCY_RECORDS", uniqueConstraints = {
    @UniqueConstraint(name = "UK_IDEMPOTENCY_RESOURCE_KEY",
        columnNames = {"IDEMPOTENCY_RESOURCE_NAME", "IDEMPOTENCY_KEY"})})
public class IdempotencyRecord {

  @Id
  @Column(name = "ID", nullable = false)
  @SequenceGenerator(
      name = "IDEMPOTENCY_RECORD_SQ",
      sequenceName = "IDEMPOTENCY_RECORD_SQ",
      allocationSize = 1)
  @GeneratedValue(
      generator = "IDEMPOTENCY_RECORD_SQ",
      strategy = GenerationType.SEQUENCE)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "IDEMPOTENCY_RESOURCE_NAME", nullable = false, length = 100)
  @EqualsAndHashCode.Include
  private String idempotencyResourceName;

  @Column(name = "IDEMPOTENCY_KEY", nullable = false, length = 255)
  @EqualsAndHashCode.Include
  private String idempotencyKey;

  @Column(name = "RESPONSE_STATUS", nullable = false)
  private int responseStatus;

  @Column(name = "RESPONSE_BODY", nullable = false, length = 4096)
  private String responseBody;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "EXPIRES_AT", nullable = false)
  private LocalDateTime expiresAt;
}