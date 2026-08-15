package br.com.ufu.ppgeb.eeg.repository.impl;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.repository.ExamRequestRepositoryCustom;


/**
 * Created by joaol on 13/10/18.
 */
@Repository
public class ExamRequestRepositoryImpl implements ExamRequestRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List< ExamRequest > findByFilter( Long medicalRecord, Long medicalRequest, Long patientId, String doctorRequestant ) {

        if ( StringUtils.isBlank( doctorRequestant ) && medicalRequest == null && patientId == null && medicalRecord == null ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery< ExamRequest > cq = cb.createQuery( ExamRequest.class );
        Root< ExamRequest > root = cq.from( ExamRequest.class );

        List< Predicate > predicates = new ArrayList<>();
        if ( StringUtils.isNotBlank( doctorRequestant ) ) {
            predicates.add( cb.like( root.get( "doctorRequestant" ), "%" + doctorRequestant + "%" ) );
        }
        if ( medicalRecord != null ) {
            predicates.add( cb.equal( root.get( "medicalRecord" ), medicalRecord ) );
        }
        if ( medicalRequest != null ) {
            predicates.add( cb.equal( root.get( "medicalRequest" ), medicalRequest ) );
        }
        if ( patientId != null ) {
            predicates.add( cb.equal( root.get( "patient" ).get( "id" ), patientId ) );
        }

        cq.where( predicates.toArray( new Predicate[ 0 ] ) );

        return em.createQuery( cq ).getResultList();
    }
}
