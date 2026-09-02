package br.com.ufu.ppgeb.eeg.config;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Security configuration for the application.
 */
@Configuration
public class SecurityConfig {

  private static final String ROLE_USER = "USER";

  private static final String CONTENT_SECURITY_POLICY =
      "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; "
          + "img-src 'self' data:; font-src 'self' data:; connect-src 'self'; "
          + "object-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'self'";

  private static final String PERMISSIONS_POLICY = "camera=(), microphone=(), geolocation=()";

  /**
   * Configures the security filter chain.
   *
   * @param http the HttpSecurity to configure
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http)
      throws Exception {

    http.csrf(csrf -> csrf.spa())
        .headers(headers -> headers
            .contentSecurityPolicy(csp -> csp.policyDirectives(CONTENT_SECURITY_POLICY))
            .referrerPolicy(referrer -> referrer
                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            .permissionsPolicyHeader(permissions -> permissions.policy(PERMISSIONS_POLICY)))
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
            .expiredUrl(ApiPaths.LOGIN))
        .exceptionHandling(e -> e
            .defaultAuthenticationEntryPointFor(apiUnauthorizedEntryPoint(),
                PathPatternRequestMatcher.pathPattern(ApiPaths.API_ROOT))
            .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint(ApiPaths.LOGIN),
                PathPatternRequestMatcher.pathPattern(ApiPaths.ANY)))
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .formLogin(form -> form
            .loginPage(ApiPaths.LOGIN)
            .defaultSuccessUrl(ApiPaths.HOME, true)
            .permitAll())
        .logout(logout -> logout
            .logoutUrl(ApiPaths.LOGOUT)
            .logoutSuccessUrl(ApiPaths.LOGIN_LOGOUT));

    return http.build();
  }

  /**
   * Publishes HTTP session lifecycle events to the session registry used by concurrent-session
   * control.
   *
   * @return the HTTP session event publisher
   */
  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {

    return new HttpSessionEventPublisher();
  }

  private static AuthenticationEntryPoint apiUnauthorizedEntryPoint() {

    return (request, response, authException) -> {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
    };
  }

  /**
   * Configures the user details service with in-memory users.
   *
   * @param passwordEncoder the password encoder
   * @param joaoPassword password for joaol user
   * @param testePassword password for teste user
   * @param userPassword password for user
   * @return the configured UserDetailsService
   */
  @Bean
  public UserDetailsService userDetailsService(
      PasswordEncoder passwordEncoder,
      @Value("${app.security.users.joaol.password}") String joaoPassword,
      @Value("${app.security.users.teste.password}") String testePassword,
      @Value("${app.security.users.user.password}") String userPassword) {

    UserDetails joao = User.withUsername("joaol")
        .password(passwordEncoder.encode(joaoPassword))
        .roles(ROLE_USER).build();
    UserDetails teste = User.withUsername("teste")
        .password(passwordEncoder.encode(testePassword))
        .roles(ROLE_USER).build();
    UserDetails user = User.withUsername("user")
        .password(passwordEncoder.encode(userPassword))
        .roles(ROLE_USER).build();

    return new InMemoryUserDetailsManager(joao, teste, user);
  }

  /**
   * Configures the password encoder.
   *
   * @return the password encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {

    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * Security configuration for H2 console in dev profile.
   */
  @Configuration
  @Profile("dev")
  static class H2ConsoleSecurityConfig {

    /**
     * Configures the H2 console security filter chain.
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    @Order(1)
    public SecurityFilterChain h2ConsoleFilterChain(HttpSecurity http)
        throws Exception {

      http.securityMatcher("/h2/**")
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
          .headers(headers ->
              headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

      return http.build();
    }
  }
}
