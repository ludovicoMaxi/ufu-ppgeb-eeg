package br.com.ufu.ppgeb.eeg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by joaol on 08/09/17.
 */
@Controller
public class HomeController {

    @RequestMapping( value = { "/", "/login", "/cadastro" } )
    public String index() {

        return "index.html";
    }
}
