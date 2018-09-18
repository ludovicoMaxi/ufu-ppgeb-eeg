package br.com.ufu.ppgeb.eeg.repository.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.repository.ContactRepositoryCustom;


/**
 * Created by joaol on 17/09/18.
 */
@Repository
public class ContactRepositoryImpl implements ContactRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List< Contact > findByFilter( Long objectType, Long objectId ) {

        Session session = em.unwrap( Session.class );

        Criteria criteria = session.createCriteria( Contact.class );

        if ( objectType != null ) {
            criteria.add( Restrictions.eq( "objectType", objectType ) );
        }
        if ( objectId != null ) {
            criteria.add( Restrictions.eq( "objectId", objectId ) );
        }

        List< Contact > list = criteria.list();

        return list;
    }
}
