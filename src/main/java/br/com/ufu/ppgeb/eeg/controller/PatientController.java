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

import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.service.PatientService;


@RestController
@RequestMapping( "/api/patient" )
@AllArgsConstructor
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger( PatientController.class );

    private final PatientService patientService;


    @GetMapping
    public List< Patient > list( @RequestParam( value = "name", required = false ) String name,
                                 @RequestParam( value = "documentNumber", required = false ) String documentNumber ) {

        logger.info( "Consultando pacientes; nameInformado={}, documentNumberInformado={}", name != null && !name.isBlank(),
            documentNumber != null && !documentNumber.isBlank() );
        return patientService.findByFilter( name, documentNumber );
    }


    @GetMapping( "/{id}" )
    public Patient findById( @PathVariable( value = "id" ) Long id ) {

        logger.info( "Consultando paciente id={}", id );
        return patientService.findById( id );
    }


    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Patient save( @RequestBody Patient patient ) {

        logger.info( "Recebendo criação de paciente" );
        return patientService.save( patient );
    }


    @PutMapping
    public Patient update( @RequestBody Patient patient ) {

        logger.info( "Recebendo atualização de paciente id={}", patient.getId() );
        return patientService.update( patient );
    }
}
