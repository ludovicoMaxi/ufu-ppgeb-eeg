package br.com.ufu.ppgeb.eeg.repository.impl;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery< Contact > cq = cb.createQuery( Contact.class );
        Root< Contact > root = cq.from( Contact.class );

        List< Predicate > predicates = new ArrayList<>();
        if ( objectType != null ) {
            predicates.add( cb.equal( root.get( "objectType" ), objectType ) );
        }
        if ( objectId != null ) {
            predicates.add( cb.equal( root.get( "objectId" ), objectId ) );
        }

        cq.where( predicates.toArray( new Predicate[ 0 ] ) );

        return em.createQuery( cq ).getResultList();
    }
}
