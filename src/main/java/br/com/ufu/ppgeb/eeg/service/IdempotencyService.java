package br.com.ufu.ppgeb.eeg.service;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import br.com.ufu.ppgeb.eeg.model.IdempotencyRecord;
import br.com.ufu.ppgeb.eeg.repository.IdempotencyRecordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * Stores and replays responses of idempotent create operations.
 */
@Service
@Slf4j
@AllArgsConstructor
public class IdempotencyService {

  public static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";
  public static final String IDEMPOTENCY_REPLAYED_HEADER = "Idempotency-Key-Replayed";

  private static final long RECORD_TTL_HOURS = 24L;
  private static final String REPLAY_LOG =
      "Resposta idempotente reutilizada; resourceName={}, idempotencyKey={}";

  private final IdempotencyRecordRepository idempotencyRecordRepository;
  private final ObjectMapper objectMapper;

  /**
   * Executes the given operation, or replays the stored response for the key.
   *
   * @param resourceName the resource name used to scope the key
   * @param idempotencyKey the idempotency key value
   * @param responseType the response body type
   * @param operation the operation to execute on the first request
   * @return the response
   */
  public <T> ResponseEntity<T> execute(String resourceName, String idempotencyKey,
      Class<T> responseType, Supplier<ResponseEntity<T>> operation) {

    if (StringUtils.isBlank(idempotencyKey)) {
      return operation.get();
    }

    Optional<IdempotencyRecord> stored = idempotencyRecordRepository
        .findByIdempotencyResourceNameAndIdempotencyKey(resourceName, idempotencyKey);
    if (stored.isPresent()) {
      IdempotencyRecord idempotencyRecord = stored.get();
      if (nonNull(idempotencyRecord.getExpiresAt())
          && idempotencyRecord.getExpiresAt().isAfter(LocalDateTime.now())) {
        log.info(REPLAY_LOG, resourceName, idempotencyKey);
        return replay(idempotencyRecord, responseType);
      }
      idempotencyRecordRepository.delete(idempotencyRecord);
    }

    ResponseEntity<T> response = operation.get();
    persist(resourceName, idempotencyKey, response);
    return response;
  }

  private <T> ResponseEntity<T> replay(IdempotencyRecord idempotencyRecord,
      Class<T> responseType) {

    try {
      T body = objectMapper.readValue(idempotencyRecord.getResponseBody(), responseType);
      return ResponseEntity.status(idempotencyRecord.getResponseStatus())
          .header(IDEMPOTENCY_REPLAYED_HEADER, Boolean.TRUE.toString())
          .body(body);
    } catch (JacksonException exception) {
      throw new IllegalStateException(
          "Falha ao reutilizar resposta idempotente.", exception);
    }
  }

  private <T> void persist(String resourceName, String idempotencyKey,
      ResponseEntity<T> response) {

    LocalDateTime now = LocalDateTime.now();
    IdempotencyRecord idempotencyRecord = IdempotencyRecord.builder()
        .idempotencyResourceName(resourceName)
        .idempotencyKey(idempotencyKey)
        .responseStatus(response.getStatusCode().value())
        .responseBody(writeBody(response.getBody()))
        .createdAt(now)
        .expiresAt(now.plusHours(RECORD_TTL_HOURS))
        .build();
    idempotencyRecordRepository.save(idempotencyRecord);
    log.info("Resposta idempotente gravada; resourceName={}, idempotencyKey={}",
        resourceName, idempotencyKey);
  }

  private String writeBody(Object body) {

    try {
      return objectMapper.writeValueAsString(body);
    } catch (JacksonException exception) {
      throw new IllegalStateException("Falha ao serializar resposta idempotente.", exception);
    }
  }
}