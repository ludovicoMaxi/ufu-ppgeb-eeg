package br.com.ufu.ppgeb.eeg.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.model.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for idempotency records.
 */
public interface IdempotencyRecordRepository
    extends JpaRepository<IdempotencyRecord, Long> {

  /**
   * Finds a record by resource name and idempotency key.
   *
   * @param idempotencyResourceName the resource name
   * @param idempotencyKey the idempotency key
   * @return the optional record
   */
  Optional<IdempotencyRecord> findByIdempotencyResourceNameAndIdempotencyKey(
      String idempotencyResourceName, String idempotencyKey);

  /**
   * Deletes expired records.
   *
   * @param expiresAt the expiration bound
   */
  void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}