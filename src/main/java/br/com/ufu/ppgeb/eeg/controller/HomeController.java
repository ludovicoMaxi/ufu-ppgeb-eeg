package br.com.ufu.ppgeb.eeg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Created by joaol on 15/04/18.
 */
@Controller
public class HomeController {

    @GetMapping( value = { "/", "/login", "/customers/*" } )
    public String index() {

        return "index";
    }
}
