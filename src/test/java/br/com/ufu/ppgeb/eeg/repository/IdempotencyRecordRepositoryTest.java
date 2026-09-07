package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.IdempotencyRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(AuditingConfig.class)
class IdempotencyRecordRepositoryTest {

  private static final String RESOURCE = "PATIENT";
  private static final String KEY = "key-1";
  private static final String USERNAME = "testuser";
  private static final int STATUS_CREATED = 201;

  @Autowired
  private IdempotencyRecordRepository repository;

  @BeforeEach
  void setUpAuthentication() {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new UsernamePasswordAuthenticationToken(USERNAME, "123",
        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    SecurityContextHolder.setContext(context);
  }

  @AfterEach
  void tearDownAuthentication() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Given a valid record when save then persist and query by resource and key")
  void givenValidRecord_whenSave_thenReturnRecord() {
    IdempotencyRecord record = createRecord(KEY, LocalDateTime.now().plusHours(1));

    IdempotencyRecord saved = repository.save(record);
    repository.flush();

    Optional<IdempotencyRecord> found = repository
        .findByIdempotencyResourceNameAndIdempotencyKey(RESOURCE, KEY);
    assertThat(found).isPresent();
    assertThat(found.get().getId()).isEqualTo(saved.getId());
    assertThat(found.get().getResponseStatus()).isEqualTo(STATUS_CREATED);
  }

  @Test
  @DisplayName("Given expired record when delete by expires at before then record removed")
  void givenExpiredRecord_whenDeleteByExpiresAtBefore_thenRecordRemoved() {
    repository.save(createRecord(KEY, LocalDateTime.now().minusHours(1)));
    repository.flush();

    repository.deleteByExpiresAtBefore(LocalDateTime.now());
    repository.flush();

    assertThat(repository.findByIdempotencyResourceNameAndIdempotencyKey(RESOURCE, KEY))
        .isEmpty();
  }

  @Test
  @DisplayName("Given current record when delete by expires at before then record kept")
  void givenCurrentRecord_whenDeleteByExpiresAtBefore_thenRecordKept() {
    repository.save(createRecord(KEY, LocalDateTime.now().plusHours(1)));
    repository.flush();

    repository.deleteByExpiresAtBefore(LocalDateTime.now());
    repository.flush();

    assertThat(repository.findByIdempotencyResourceNameAndIdempotencyKey(RESOURCE, KEY))
        .isPresent();
  }

  private IdempotencyRecord createRecord(String key, LocalDateTime expiresAt) {
    return IdempotencyRecord.builder()
        .idempotencyResourceName(RESOURCE)
        .idempotencyKey(key)
        .responseStatus(STATUS_CREATED)
        .responseBody("{}")
        .createdAt(LocalDateTime.now())
        .expiresAt(expiresAt)
        .build();
  }
}
