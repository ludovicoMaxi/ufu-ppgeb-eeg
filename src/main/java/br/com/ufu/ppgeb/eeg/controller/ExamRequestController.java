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

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.service.ExamRequestService;


@RestController
@RequestMapping( "/api/exam-request" )
@AllArgsConstructor
public class ExamRequestController {

    private static final Logger logger = LoggerFactory.getLogger( ExamRequestController.class );

    private final ExamRequestService examRequestService;


    @GetMapping
    public List< ExamRequest > list( @RequestParam( value = "medicalRecord", required = false ) Long medicalRecord,
                                     @RequestParam( value = "medicalRequest", required = false ) Long medicalRequest,
                                     @RequestParam( value = "patientId", required = false ) Long patientId,
                                     @RequestParam( value = "doctorRequestant", required = false ) String doctorRequestant ) {

        logger.info( "Consultando solicitações; medicalRecord={}, medicalRequest={}, patientId={}, doctorRequestant={}", medicalRecord, medicalRequest, patientId,
            doctorRequestant );
        return examRequestService.findByFilter( medicalRecord, medicalRequest, patientId, doctorRequestant );
    }


    @GetMapping( "/{id}" )
    public ExamRequest findById( @PathVariable( value = "id" ) Long id ) {

        logger.info( "Consultando solicitação de exame id={}", id );
        return examRequestService.findById( id );
    }


    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public ExamRequest save( @RequestBody ExamRequest examRequest ) {

        logger.info( "Recebendo criação de solicitação de exame" );
        return examRequestService.save( examRequest );
    }


    @PutMapping
    public ExamRequest update( @RequestBody ExamRequest examRequest ) {

        logger.info( "Recebendo atualização de solicitação de exame id={}", examRequest.getId() );
        return examRequestService.update( examRequest );
    }
}
