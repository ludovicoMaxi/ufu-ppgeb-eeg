package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufu.ppgeb.eeg.model.Unit;
import br.com.ufu.ppgeb.eeg.service.UnitService;


@RestController
@RequestMapping( "/api/unit" )
@AllArgsConstructor
public class UnitController {

    private static final Logger logger = LoggerFactory.getLogger( UnitController.class );

    private final UnitService unitService;


    @GetMapping
    public List< Unit > list() {

        logger.info( "Consultando unidades" );
        return unitService.findAll();
    }
}
