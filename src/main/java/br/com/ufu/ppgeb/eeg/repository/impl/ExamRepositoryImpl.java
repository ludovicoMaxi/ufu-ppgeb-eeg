package br.com.ufu.ppgeb.eeg.repository.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.repository.ExamRepositoryCustom;


/**
 * Created by joaol on 13/10/18.
 */
@Repository
public class ExamRepositoryImpl implements ExamRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List< Exam > findByFilter( Long id, String bed, Long patientId, Long examRequestId ) {

        Session session = em.unwrap( Session.class );

        Criteria criteria = session.createCriteria( Exam.class );

        if ( id == null && StringUtils.isBlank( bed ) && patientId == null && examRequestId == null ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        }

        if ( StringUtils.isNotBlank( bed ) ) {
            criteria.add( Restrictions.like( "bed", "%" + bed + "%" ) );
        }
        if ( id != null ) {
            criteria.add( Restrictions.eq( "id", id ) );
        }
        if ( patientId != null ) {
            criteria.add( Restrictions.eq( "patient.id", patientId ) );
        }
        if ( examRequestId != null ) {
            criteria.add( Restrictions.eq( "examRequest.id", examRequestId ) );
        }

        List< Exam > list = criteria.list();

        return list;
    }
}
