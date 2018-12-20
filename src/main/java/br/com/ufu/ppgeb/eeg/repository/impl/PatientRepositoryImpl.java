package br.com.ufu.ppgeb.eeg.repository.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.repository.PatientRepositoryCustom;


/**
 * Created by joaol on 13/10/18.
 */
@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List< Patient > findByFilter( String name, String documentNumber ) {

        Session session = em.unwrap( Session.class );

        Criteria criteria = session.createCriteria( Patient.class );

        if ( StringUtils.isBlank( name ) && StringUtils.isBlank( documentNumber ) ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        }

        if ( StringUtils.isNotBlank( name ) ) {
            criteria.add( Restrictions.like( "name", "%" + name + "%" ) );
        }
        if ( StringUtils.isNotBlank( documentNumber ) ) {
            criteria.add( Restrictions.eq( "documentNumber", documentNumber ) );
        }

        List< Patient > list = criteria.list();

        return list;
    }
}
