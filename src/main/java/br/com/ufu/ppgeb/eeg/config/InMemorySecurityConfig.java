package br.com.ufu.ppgeb.eeg.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;


/**
 * Created by joaol on 08/09/17.
 */
@Configuration
public class InMemorySecurityConfig {

    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder builder )
        throws Exception {

        builder.inMemoryAuthentication().withUser( "joaol" ).password( "123" ).roles( "USER" ).and().withUser( "destro" ).password( "123" )
            .roles( "USER" ).and().withUser( "gaspar" ).password( "123" ).roles( "USER" );
    }
}
