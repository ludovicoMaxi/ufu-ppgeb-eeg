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
    public List< Exam > findByFilter( Long medicalRecord, Long medicalRequest, Long patientId, String doctorRequestant ) {

        Session session = em.unwrap( Session.class );

        Criteria criteria = session.createCriteria( Exam.class );

        if ( StringUtils.isBlank( doctorRequestant ) && medicalRequest == null && patientId == null && medicalRecord == null ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        }

        if ( StringUtils.isNotBlank( doctorRequestant ) ) {
            criteria.add( Restrictions.like( "doctorRequestant", "%" + doctorRequestant + "%" ) );
        }
        if ( medicalRecord != null ) {
            criteria.add( Restrictions.eq( "medicalRecord", medicalRecord ) );
        }
        if ( medicalRequest != null ) {
            criteria.add( Restrictions.eq( "medicalRequest", medicalRequest ) );
        }
        if ( patientId != null ) {
            criteria.add( Restrictions.eq( "patient.id", patientId ) );
        }

        List< Exam > list = criteria.list();

        return list;
    }
}
