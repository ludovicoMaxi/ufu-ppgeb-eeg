package br.com.ufu.ppgeb.eeg.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Created by joaol on 15/04/18.
 */
@Configuration
public class InMemorySecurityConfig {

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http )
        throws Exception {

        http.csrf( AbstractHttpConfigurer::disable )
            .authorizeHttpRequests( auth -> auth
                .requestMatchers( "/h2/**" ).permitAll()
                .anyRequest().authenticated() )
            .headers( headers -> headers.frameOptions( frame -> frame.sameOrigin() ) )
            .httpBasic( Customizer.withDefaults() );

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails joao = User.withUsername( "joaol" ).password( "{noop}123" ).roles( "USER" ).build();
        UserDetails teste = User.withUsername( "teste" ).password( "{noop}123" ).roles( "USER" ).build();
        UserDetails user = User.withUsername( "user" ).password( "{noop}123" ).roles( "USER" ).build();

        return new InMemoryUserDetailsManager( joao, teste, user );
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
