package br.com.ufu.ppgeb.eeg.config;

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
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

/**
 * Security configuration for the application.
 */
@Configuration
public class SecurityConfig {

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

    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .exceptionHandling(e -> e
            .defaultAuthenticationEntryPointFor(apiUnauthorizedEntryPoint(),
                PathPatternRequestMatcher.pathPattern("/api/**"))
            .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"),
                PathPatternRequestMatcher.pathPattern("/**")))
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout"));

    return http.build();
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
        .roles("USER").build();
    UserDetails teste = User.withUsername("teste")
        .password(passwordEncoder.encode(testePassword))
        .roles("USER").build();
    UserDetails user = User.withUsername("user")
        .password(passwordEncoder.encode(userPassword))
        .roles("USER").build();

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
