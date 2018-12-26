package br.com.ufu.ppgeb.eeg.repository.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.repository.ExamRequestRepositoryCustom;


/**
 * Created by joaol on 13/10/18.
 */
@Repository
public class ExamRequestRepositoryImpl implements ExamRequestRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List< ExamRequest > findByFilter( Long patientId ) {

        Assert.notNull( patientId, "patientId cannot be null." );

        Session session = em.unwrap( Session.class );

        Criteria criteria = session.createCriteria( Patient.class );

        // if ( StringUtils.isBlank( name ) && StringUtils.isBlank( documentNumber ) ) {
        // throw new IllegalArgumentException( "Informe pelo menos um campo para
        // consultar!" );
        // }

        criteria.add( Restrictions.eq( "patient.id", patientId ) );

        List< ExamRequest > list = criteria.list();

        return list;
    }
}
