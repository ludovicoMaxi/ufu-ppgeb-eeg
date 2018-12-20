package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.service.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.service.ContactService;


/**
 * Created by joaol on 16/09/18.
 */
@Controller
@RequestMapping( "/api/patient" )
public class PatientController {

    private static final Logger logger = LogManager.getLogger( PatientController.class );

    @Autowired
    private PatientService patientService;


    @GetMapping
    @ResponseBody
    public List< Patient > list(
            @RequestParam( value = "name", required = false ) String name,
            @RequestParam( value = "documentNumber", required = false ) String documentNumber

    ) {

        logger.info( "Parametros{ name=" + name + ", documentNumber=" + documentNumber + "}" );
        return patientService.findByFilter( name, documentNumber );

    }


    @GetMapping( "/{id}" )
    public @ResponseBody Patient findById( @PathVariable( value = "id" ) Long id ) {

        return this.patientService.findById( id );
    }


    @PostMapping
    public @ResponseBody
    Patient save(@RequestBody Patient patient ) {

        logger.info( "Saving " + patient );
        return this.patientService.save( patient );
    }


    // @DeleteMapping( "/{id}" )
    // public @ResponseBody String delete( @PathVariable( value = "id" ) Long id ) {
    //
    // this.patientService.delete( id );
    // return "redirect:/";
    // }

    @PutMapping
    public @ResponseBody Patient update( @RequestBody Patient patient )
            throws Exception {

        logger.info( "Updating " + patient );
        return this.patientService.update( patient );
    }
}
