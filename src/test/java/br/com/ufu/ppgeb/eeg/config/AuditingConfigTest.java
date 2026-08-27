package br.com.ufu.ppgeb.eeg.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class AuditingConfigTest {

  private static final String USERNAME = "joaol";

  private final AuditingConfig auditingConfig = new AuditingConfig();

  @Test
  @DisplayName("Given authenticated user when getCurrentAuditor then return username")
  void givenAuthenticatedUser_whenGetCurrentAuditor_thenReturnUsername() {
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getName()).thenReturn(USERNAME);

    try (MockedStatic<SecurityContextHolder> holder = mockStatic(SecurityContextHolder.class)) {
      holder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
      when(securityContext.getAuthentication()).thenReturn(authentication);

      AuditorAware<String> auditorProvider = auditingConfig.auditorProvider();

      Optional<String> result = auditorProvider.getCurrentAuditor();

      assertThat(result).contains(USERNAME);
    }
  }

  @Test
  @DisplayName("Given context without authentication when getCurrentAuditor then return empty")
  void givenContextWithoutAuthentication_whenGetCurrentAuditor_thenReturnEmpty() {
    try (MockedStatic<SecurityContextHolder> holder = mockStatic(SecurityContextHolder.class)) {
      holder.when(SecurityContextHolder::getContext).thenReturn(mock(SecurityContext.class));

      AuditorAware<String> auditorProvider = auditingConfig.auditorProvider();

      Optional<String> result = auditorProvider.getCurrentAuditor();

      assertThat(result).isEmpty();
    }
  }

  @Test
  @DisplayName("Given not authenticated user when getCurrentAuditor then return empty")
  void givenNotAuthenticatedUser_whenGetCurrentAuditor_thenReturnEmpty() {
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false);

    try (MockedStatic<SecurityContextHolder> holder = mockStatic(SecurityContextHolder.class)) {
      holder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
      when(securityContext.getAuthentication()).thenReturn(authentication);

      AuditorAware<String> auditorProvider = auditingConfig.auditorProvider();

      Optional<String> result = auditorProvider.getCurrentAuditor();

      assertThat(result).isEmpty();
    }
  }
}