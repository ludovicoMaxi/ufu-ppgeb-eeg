package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.Exame;
import br.com.ufu.ppgeb.eeg.repository.ExameRepository;


/**
 * Created by joaol on 08/09/17.
 */
@Controller
@RequestMapping( "/exames" )
public class ExameController {

    @Autowired
    private ExameRepository exameRepository;


    @GetMapping
    @ResponseBody
    public List< Exame > listar() {

        return exameRepository.findAll();

    }


    @PostMapping
    public String salvar( Exame exame ) {

        this.exameRepository.save( exame );
        return "redirect:/exames";
    }


    @DeleteMapping( "/{id}" )
    public String delete( @PathVariable( value = "id" ) Long id ) {

        this.exameRepository.delete( id );
        return "redirect:/exames";
    }
}
