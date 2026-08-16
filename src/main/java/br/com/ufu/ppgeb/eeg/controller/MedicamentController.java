package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;


@RestController
@RequestMapping( "/api/medicament" )
@AllArgsConstructor
public class MedicamentController {

    private static final Logger logger = LoggerFactory.getLogger( MedicamentController.class );

    private final MedicamentService medicamentService;


    @GetMapping
    public List< Medicament > list() {

        logger.info( "Consultando medicamentos" );
        return medicamentService.findAll();
    }
}
