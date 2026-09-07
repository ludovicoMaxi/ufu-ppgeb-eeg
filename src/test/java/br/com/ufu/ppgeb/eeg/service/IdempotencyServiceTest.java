package br.com.ufu.ppgeb.eeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import br.com.ufu.ppgeb.eeg.model.IdempotencyRecord;
import br.com.ufu.ppgeb.eeg.repository.IdempotencyRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class IdempotencyServiceTest {

  private static final String RESOURCE = "PATIENT";
  private static final String KEY = "key-1";
  private static final Long ID_1 = 1L;
  private static final Long ID_2 = 2L;
  private static final Long ID_3 = 3L;
  private static final long EXPIRED_HOURS_AGO = 2L;
  private static final String NAME_1 = "John";
  private static final String NAME_2 = "Jane";
  private static final String NAME_3 = "Zero";

  record DemoResponse(Long id, String name) {}

  @Mock
  private IdempotencyRecordRepository repository;

  @Spy
  private ObjectMapper objectMapper = new ObjectMapper();

  @InjectMocks
  private IdempotencyService service;

  @Test
  @DisplayName("Given blank key when executing operation then return operation result without accessing repository")
  void givenBlankKey_whenExecutingOperation_thenReturnOperationResultWithoutAccessingRepository() {
    DemoResponse body = new DemoResponse(ID_1, NAME_1);
    ResponseEntity<DemoResponse> operationResult = ResponseEntity.ok(body);

    ResponseEntity<DemoResponse> response =
        service.execute(RESOURCE, KEY, DemoResponse.class, () -> operationResult);

    assertThat(response).isSameAs(operationResult);
    verify(repository, never()).findByIdempotencyResourceNameAndIdempotencyKey(any(), any());
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing unexpired record when executing operation then replay response without re-executing")
  void givenExistingRecord_whenExecutingOperation_thenReplayResponseWithoutReExecuting()
      throws Exception {
    DemoResponse body = new DemoResponse(ID_1, NAME_1);
    IdempotencyRecord record = IdempotencyRecord.builder()
        .idempotencyResourceName(RESOURCE)
        .idempotencyKey(KEY)
        .responseStatus(HttpStatus.CREATED.value())
        .responseBody(objectMapper.writeValueAsString(body))
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusHours(1))
        .build();
    when(repository.findByIdempotencyResourceNameAndIdempotencyKey(RESOURCE, KEY))
        .thenReturn(Optional.of(record));

    AtomicBoolean operationCalled = new AtomicBoolean(false);
    ResponseEntity<DemoResponse> response =
        service.execute(RESOURCE, KEY, DemoResponse.class, () -> {
          operationCalled.set(true);
          return ResponseEntity.status(HttpStatus.CREATED).body(new DemoResponse(ID_2, NAME_2));
        });

    assertThat(operationCalled).isFalse();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getHeaders().getFirst(IdempotencyService.IDEMPOTENCY_REPLAYED_HEADER))
        .isEqualTo(Boolean.TRUE.toString());
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().id()).isEqualTo(ID_1);
    assertThat(response.getBody().name()).isEqualTo(NAME_1);
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("Given expired record when executing operation then execute operation and delete old record")
  void givenExpiredRecord_whenExecutingOperation_thenExecuteOperationAndDeleteOldRecord()
      throws Exception {
    IdempotencyRecord expiredRecord = IdempotencyRecord.builder()
        .idempotencyResourceName(RESOURCE)
        .idempotencyKey(KEY)
        .responseStatus(HttpStatus.OK.value())
        .responseBody("{}")
        .createdAt(LocalDateTime.now().minusHours(EXPIRED_HOURS_AGO))
        .expiresAt(LocalDateTime.now().minusHours(1))
        .build();
    when(repository.findByIdempotencyResourceNameAndIdempotencyKey(RESOURCE, KEY))
        .thenReturn(Optional.of(expiredRecord));

    DemoResponse body = new DemoResponse(ID_2, NAME_2);
    ResponseEntity<DemoResponse> response =
        service.execute(RESOURCE, KEY, DemoResponse.class,
            () -> ResponseEntity.status(HttpStatus.CREATED).body(body));

    verify(repository).delete(expiredRecord);
    ArgumentCaptor<IdempotencyRecord> captor = ArgumentCaptor.forClass(IdempotencyRecord.class);
    verify(repository).save(captor.capture());
    IdempotencyRecord saved = captor.getValue();
    assertThat(saved.getIdempotencyResourceName()).isEqualTo(RESOURCE);
    assertThat(saved.getIdempotencyKey()).isEqualTo(KEY);
    assertThat(saved.getResponseStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isEqualTo(body);
  }

  @Test
  @DisplayName("Given no stored record when executing operation then execute operation and persist record")
  void givenNoStoredRecord_whenExecutingOperation_thenExecuteOperationAndPersistRecord()
      throws Exception {
    when(repository.findByIdempotencyResourceNameAndIdempotencyKey(RESOURCE, KEY))
        .thenReturn(Optional.empty());

    DemoResponse body = new DemoResponse(ID_3, NAME_3);
    ResponseEntity<DemoResponse> response =
        service.execute(RESOURCE, KEY, DemoResponse.class, () -> ResponseEntity.ok(body));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(body);
    ArgumentCaptor<IdempotencyRecord> captor = ArgumentCaptor.forClass(IdempotencyRecord.class);
    verify(repository).save(captor.capture());
    IdempotencyRecord saved = captor.getValue();
    assertThat(saved.getIdempotencyResourceName()).isEqualTo(RESOURCE);
    assertThat(saved.getIdempotencyKey()).isEqualTo(KEY);
    assertThat(saved.getResponseStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(saved.getResponseBody()).isEqualTo(objectMapper.writeValueAsString(body));
  }
}
