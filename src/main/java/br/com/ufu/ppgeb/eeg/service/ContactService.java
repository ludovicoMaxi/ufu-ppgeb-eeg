package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.model.ObjectType;


public interface ContactService {

    Contact save( Contact customer );


    void saveContactList( List< Contact > contactList, ObjectType objectType, Long objectId );


    Contact findById( Long id );


    List< Contact > findByFilter( Long objectType, Long objectId );


    List< Contact > findAll();


    void delete( Long id );


    Contact update( Contact customer );


    void validateContactList( List< Contact > contactList );

}
