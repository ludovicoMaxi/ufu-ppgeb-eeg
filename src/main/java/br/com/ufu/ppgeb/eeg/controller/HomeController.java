package br.com.ufu.ppgeb.eeg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Created by joaol on 15/04/18.
 */
@Controller
public class HomeController {

    private final String cacheVersion = Long.toString( System.currentTimeMillis() );

    @GetMapping( value = { "/", "/customers/*" } )
    public String index( Model model ) {

        model.addAttribute( "cacheVersion", cacheVersion );
        return "index";
    }

    @GetMapping( "/login" )
    public String login() {

        return "login";
    }
}
