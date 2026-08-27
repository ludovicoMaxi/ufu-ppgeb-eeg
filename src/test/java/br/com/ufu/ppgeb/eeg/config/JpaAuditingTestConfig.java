package br.com.ufu.ppgeb.eeg.config;

import java.util.Optional;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Test configuration that enables JPA auditing with a fixed auditor.
 *
 * <p>Used by repository tests so that auditing fields (createdBy, createdAt,
 * updatedBy, updatedAt) are populated by the auditing listener instead of
 * being set manually, validating that the auditing mechanism works.</p>
 */
@TestConfiguration
@EnableJpaAuditing(auditorAwareRef = "testAuditorProvider")
public class JpaAuditingTestConfig {

  /**
   * Fixed auditor name used by the auditing listener in tests.
   */
  public static final String TEST_AUDITOR = "SYSTEM";

  /**
   * Provides the fixed auditor used in tests.
   *
   * @return the auditor aware instance
   */
  @Bean
  public AuditorAware<String> testAuditorProvider() {

    return () -> Optional.of(TEST_AUDITOR);
  }
}
