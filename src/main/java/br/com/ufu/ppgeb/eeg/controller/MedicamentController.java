package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;


/**
 * Created by joaol on 10/01/19.
 */
@Controller
@RequestMapping( "/api/medicament" )
public class MedicamentController {

    private static final Logger logger = LogManager.getLogger( MedicamentController.class );

    @Autowired
    private MedicamentService medicamentService;


    @GetMapping
    @ResponseBody
    public List< Medicament > list() {

        return medicamentService.findAll();

    }

}
