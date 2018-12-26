package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.service.ExamRequestService;


/**
 * Created by joaol on 16/09/18.
 */
@Controller
@RequestMapping( "/api/exam-request" )
public class ExamRequestController {

    private static final Logger logger = LogManager.getLogger( ExamRequestController.class );

    @Autowired
    private ExamRequestService examRequestService;


    @GetMapping
    @ResponseBody
    public List< ExamRequest > list(
        @RequestParam( value = "medicalRecord", required = false ) Long medicalRecord,
        @RequestParam( value = "medicalRequest", required = false ) Long medicalRequest,
        @RequestParam( value = "doctorRequestant", required = false ) String doctorRequestant

    ) {

        logger.info( "Parametros{ medicalRecord=" + medicalRecord + ", medicalRequest=" + medicalRequest + ", doctorRequestant=" + doctorRequestant + "}" );
        return examRequestService.findByFilter( medicalRecord, medicalRequest, doctorRequestant );

    }


    @GetMapping( "/{id}" )
    public @ResponseBody ExamRequest findById( @PathVariable( value = "id" ) Long id ) {

        return this.examRequestService.findById( id );
    }


    @PostMapping
    public @ResponseBody ExamRequest save( @RequestBody ExamRequest examRequest ) {

        logger.info( "Saving " + examRequest );
        return this.examRequestService.save( examRequest );
    }


    @PutMapping
    public @ResponseBody ExamRequest update( @RequestBody ExamRequest examRequest )
        throws Exception {

        logger.info( "Updating " + examRequest );
        return this.examRequestService.update( examRequest );
    }
}
