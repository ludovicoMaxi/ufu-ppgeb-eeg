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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http )
        throws Exception {

        http.csrf( AbstractHttpConfigurer::disable )
            .sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.IF_REQUIRED ) )
            .exceptionHandling( e -> e
                .defaultAuthenticationEntryPointFor(
                    apiUnauthorizedEntryPoint(), PathPatternRequestMatcher.pathPattern( "/api/**" ) )
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint( "/login" ),
                    PathPatternRequestMatcher.pathPattern( "/**" ) ) )
            .authorizeHttpRequests( auth -> auth.anyRequest().authenticated() )
            .httpBasic( Customizer.withDefaults() )
            .formLogin( form -> form
                .loginPage( "/login" )
                .defaultSuccessUrl( "/", true )
                .permitAll() )
            .logout( logout -> logout
                .logoutUrl( "/logout" )
                .logoutSuccessUrl( "/login?logout" ) );

        return http.build();
    }

    private static AuthenticationEntryPoint apiUnauthorizedEntryPoint() {

        return ( request, response, authException ) -> {
            response.setStatus( HttpStatus.UNAUTHORIZED.value() );
            response.setHeader( "WWW-Authenticate", "Basic realm=\"Realm\"" );
        };
    }


    @Bean
    public UserDetailsService userDetailsService( PasswordEncoder passwordEncoder,
                                                  @Value( "${app.security.users.joaol.password}" ) String joaoPassword,
                                                  @Value( "${app.security.users.teste.password}" ) String testePassword,
                                                  @Value( "${app.security.users.user.password}" ) String userPassword ) {

        UserDetails joao = User.withUsername( "joaol" ).password( passwordEncoder.encode( joaoPassword ) ).roles( "USER" ).build();
        UserDetails teste = User.withUsername( "teste" ).password( passwordEncoder.encode( testePassword ) ).roles( "USER" ).build();
        UserDetails user = User.withUsername( "user" ).password( passwordEncoder.encode( userPassword ) ).roles( "USER" ).build();

        return new InMemoryUserDetailsManager( joao, teste, user );
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Configuration
    @Profile( "dev" )
    static class H2ConsoleSecurityConfig {

        @Bean
        @Order( 1 )
        public SecurityFilterChain h2ConsoleFilterChain( HttpSecurity http )
            throws Exception {

            http.securityMatcher( "/h2/**" )
                .csrf( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests( auth -> auth.anyRequest().permitAll() )
                .headers( headers -> headers.frameOptions( HeadersConfigurer.FrameOptionsConfig::sameOrigin ) );

            return http.build();
        }
    }
}
