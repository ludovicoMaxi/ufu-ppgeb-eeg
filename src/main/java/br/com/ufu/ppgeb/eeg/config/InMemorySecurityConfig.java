package br.com.ufu.ppgeb.eeg.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;


/**
 * Created by joaol on 15/04/18.
 */
@Configuration
public class InMemorySecurityConfig {

    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder builder )
        throws Exception {

        builder.inMemoryAuthentication().withUser( "joaol" ).password( "123" ).roles( "USER" ).and().withUser( "teste" ).password( "123" )
            .roles( "USER" ).and().withUser( "user" ).password( "123" ).roles( "USER" );
    }
}
