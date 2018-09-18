package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import br.com.ufu.ppgeb.eeg.service.ContactService;
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


/**
 * Created by joaol on 16/09/18.
 */
@Controller
@RequestMapping( "/api/contact" )
public class ContactController {

    private static final Logger logger = LogManager.getLogger( ContactController.class );

    @Autowired
    private ContactService contactService;


    @GetMapping
    @ResponseBody
    public List< Contact > list(
        @RequestParam( value = "objectType", required = false ) Long objectType,
        @RequestParam( value = "objectId", required = false ) Long objectId ) {

        logger.info( "Parametros{ objectType=" + objectType + ", objectId=" + objectId + "}" );
        return contactService.findByFilter( objectType, objectId );
    }


    @GetMapping( "/{id}" )
    public @ResponseBody Contact findById( @PathVariable( value = "id" ) Long id ) {

        return this.contactService.findById( id );
    }


    @PostMapping
    public @ResponseBody Contact save( @RequestBody Contact contact ) {

        logger.info( "Saving " + contact );
        return this.contactService.save( contact );
    }


    @DeleteMapping( "/{id}" )
    public @ResponseBody String delete( @PathVariable( value = "id" ) Long id ) {

        this.contactService.delete( id );
        return "redirect:/";
    }


    @PutMapping
    public @ResponseBody Contact update( @RequestBody Contact customer )
        throws Exception {

        logger.info( "Updating " + customer );
        return this.contactService.update( customer );
    }
}
