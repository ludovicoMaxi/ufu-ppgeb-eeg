package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.service.ContactService;


@RestController
@RequestMapping( "/api/contact" )
@AllArgsConstructor
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger( ContactController.class );

    private final ContactService contactService;


    @GetMapping
    public List< Contact > list( @RequestParam( value = "objectType", required = false ) Long objectType,
                                 @RequestParam( value = "objectId", required = false ) Long objectId ) {

        logger.info( "Consultando contatos; objectType={}, objectId={}", objectType, objectId );
        return contactService.findByFilter( objectType, objectId );
    }


    @GetMapping( "/{id}" )
    public Contact findById( @PathVariable( value = "id" ) Long id ) {

        logger.info( "Consultando contato id={}", id );
        return contactService.findById( id );
    }


    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Contact save( @RequestBody Contact contact ) {

        logger.info( "Recebendo criação de contato" );
        return contactService.save( contact );
    }


    @DeleteMapping( "/{id}" )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void delete( @PathVariable( value = "id" ) Long id ) {

        logger.info( "Recebendo remoção de contato id={}", id );
        contactService.delete( id );
    }


    @PutMapping
    public Contact update( @RequestBody Contact contact ) {

        logger.info( "Recebendo atualização de contato id={}", contact.getId() );
        return contactService.update( contact );
    }
}
