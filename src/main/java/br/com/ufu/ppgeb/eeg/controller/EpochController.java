package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.service.EpochService;
import br.com.ufu.ppgeb.eeg.view.EpochList;


@RestController
@RequestMapping( "/api/epoch" )
@AllArgsConstructor
public class EpochController {

    private static final Logger logger = LoggerFactory.getLogger( EpochController.class );

    private final EpochService epochService;


    @GetMapping
    public List< Epoch > list( @RequestParam( value = "examId" ) Long examId ) {

        logger.info( "Consultando épocas do exame id={}", examId );
        return epochService.findByFilter( examId );
    }


    @GetMapping( "/{id}" )
    public Epoch findById( @PathVariable( value = "id" ) Long id ) {

        logger.info( "Consultando época id={}", id );
        return epochService.findById( id );
    }


    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Epoch save( @RequestBody Epoch epoch ) {

        logger.info( "Recebendo criação de época" );
        return epochService.save( epoch );
    }


    @PutMapping
    public EpochList updateList( @RequestBody EpochList epochList ) {

        logger.info( "Recebendo atualização de épocas do exame id={}", epochList.getExamId() );
        epochList.setEpochs( epochService.updateList( epochList ) );
        return epochList;
    }
}
