package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.Date;
import java.util.List;

import br.com.ufu.ppgeb.eeg.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import br.com.ufu.ppgeb.eeg.service.ContactService;


@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;


    public Contact save( Contact contact ) {

        validateContact( contact );

        contact.setCreatedAt( new Date() );
        // TODO: pegar o usuario logado
        contact.setCreatedBy( "system" );

        contact.setUpdatedAt( null );
        contact.setUpdatedBy( null );

        return contactRepository.save( contact );

    }


    public void saveContactList( List< Contact > contactList, ObjectType objectType, Long objectId ) {

        Assert.notEmpty( contactList, "contactList cannot be empty." );
        Assert.notNull( objectType, "objectType cannot be null." );
        Assert.notNull( objectId, "objectId cannot be empty." );

        for ( Contact contact : contactList ) {
            contact.setObjectId( objectId );
            contact.setObjectType( objectType.getId() );
            save( contact );
        }
    }


    @Override
    public List< Contact > findAll() {

        return contactRepository.findAll();
    }


    @Override
    public Contact findById( Long id ) {

        return contactRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< Contact > findByFilter( Long objectType, Long objectId ) {

        List< Contact > list = null;
        if ( objectType == null && objectId == null ) {
            list = findAll();

        } else {
            validateSearchContact( objectType, objectId );
            list = contactRepository.findByFilter( objectType, objectId );
        }
        return list;
    }


    private void validateSearchContact( Long objectType, Long objectId ) {

        if ( objectType == null && objectId != null ) {
            throw new IllegalArgumentException( "ObjectType deve ser informado!" );
        } else if ( objectType != null && objectId == null ) {
            throw new IllegalArgumentException( "ObjectId deve ser informado!" );
        }
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        contactRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Contact update( Contact contact ) {

        validateContact( contact );
        Assert.notNull( contact.getId(), "id cannot be null." );

        Contact oldContact = contactRepository.getOne( contact.getId() );

        if ( oldContact == null ) {
            throw new IllegalArgumentException( "Not exist contact with this Id=" + contact.getId() );
        }

        oldContact.setName( contact.getName() );
        oldContact.setActive( contact.getActive() );
        oldContact.setCellphone( contact.getCellphone() );
        oldContact.setEmail( contact.getEmail() );
        oldContact.setFacebook( contact.getFacebook() );
        oldContact.setInstagram( contact.getInstagram() );
        oldContact.setMain( contact.getMain() );
        oldContact.setPhone( contact.getPhone() );
        oldContact.setWhatsapp( contact.getWhatsapp() );

        oldContact.setUpdatedAt( new Date() );
        // TODO: pegar o usuario logado
        oldContact.setUpdatedBy( "system" );

        contact = contactRepository.save( oldContact );

        return contact;

    }


    @Override
    public void validateContactList( List< Contact > contactList ) {

        Assert.notEmpty( contactList, "contactList cannot be empty." );

        Boolean hasMain = Boolean.FALSE;
        for ( Contact contact : contactList ) {
            validateContact( contact );
            if ( contact.getMain() != null && contact.getMain() ) {
                if ( hasMain ) {
                    throw new IllegalArgumentException( "Allowed only one main." );
                } else {
                    hasMain = Boolean.TRUE;
                }
            }
        }

        if ( !hasMain ) {
            throw new IllegalArgumentException( "Must have a principal." );
        }
    }


    public void validateContact( Contact contact ) {

        Assert.notNull( contact, "contact cannot be null." );
        Assert.hasText( contact.getName(), "name cannot be empty." );
        Assert.hasText( contact.getCellphone(), "cellphone cannot be empty." );
    }

}
