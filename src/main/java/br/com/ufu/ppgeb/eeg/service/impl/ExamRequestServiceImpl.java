package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.repository.ExamRequestRepository;
import br.com.ufu.ppgeb.eeg.service.ExamRequestService;


@Service
public class ExamRequestServiceImpl implements ExamRequestService {

    @Autowired
    private ExamRequestRepository examRequestRepository;


    @Override
    @Transactional( rollbackFor = Exception.class )
    public ExamRequest save( ExamRequest examRequest ) {

        Assert.notNull( examRequest, "examRequest cannot be null." );

        validateExamRequest( examRequest );

        examRequest.setCreatedAt( new Date() );
        // TODO: pegar o usuario logado
        examRequest.setCreatedBy( "system" );

        examRequest.setUpdatedAt( null );
        examRequest.setUpdatedBy( null );

        ExamRequest examRequestSaved = examRequestRepository.save( examRequest );

        return examRequestSaved;
    }


    private void validateExamRequest( ExamRequest examRequest ) {

        Assert.notNull( examRequest, "ExamRequest cannot be null." );
        Assert.notNull( examRequest.getMedicalRecord(), "medicalRecord cannot be empty." );
        Assert.notNull( examRequest.getMedicalRequest(), "medicalRequest cannot be empty." );
        Assert.hasText( examRequest.getSector(), "sector cannot be empty." );
        Assert.hasText( examRequest.getDoctorRequestant(), "doctorRequestant cannot be empty." );
        Assert.hasText( examRequest.getUser(), "user cannot be empty." );
        Assert.notNull( examRequest.getRequestDate(), "requestDate cannot be empty." );
    }


    @Override
    public List< ExamRequest > findAll() {

        return examRequestRepository.findAll();
    }


    @Override
    public ExamRequest findById( Long id ) {

        return examRequestRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< ExamRequest > findByFilter( Long medicalRecord, Long medicalRequest, Long patientId, String doctorRequestant ) {

        if ( StringUtils.isBlank( doctorRequestant ) && medicalRequest == null && patientId == null && medicalRecord == null ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        }

        List< ExamRequest > list = null;

        list = examRequestRepository.findByFilter( medicalRecord, medicalRequest, patientId, doctorRequestant );
        return list;
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        examRequestRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public ExamRequest update( ExamRequest examRequest ) {

        Assert.notNull( examRequest, "examRequest cannot be null." );

        validateExamRequest( examRequest );
        Assert.notNull( examRequest.getId(), "examRequest ID cannot be null." );

        ExamRequest oldExamRequest = examRequestRepository.getOne( examRequest.getId() );

        if ( oldExamRequest == null ) {
            throw new IllegalArgumentException( "Not exist examRequest with this Id=" + examRequest.getId() );
        }

        if ( !oldExamRequest.equals( examRequest ) ) {

            if ( !examRequest.getPatient().getId().equals( oldExamRequest.getPatient().getId() ) ) {
                throw new IllegalArgumentException(
                    "Patient ID is different. Neew=" + examRequest.getPatient().getId() + ", Old=" + oldExamRequest.getPatient().getId() );
            }

            oldExamRequest.setMedicalRecord( examRequest.getMedicalRecord() );
            oldExamRequest.setMedicalRequest( examRequest.getMedicalRequest() );
            oldExamRequest.setAchievementDate( examRequest.getAchievementDate() );
            oldExamRequest.setAgreement( examRequest.getAgreement() );
            oldExamRequest.setCityOrigin( examRequest.getCityOrigin() );
            oldExamRequest.setClinicOrigin( examRequest.getClinicOrigin() );
            oldExamRequest.setDoctorRequestant( examRequest.getDoctorRequestant() );
            oldExamRequest.setRequestDate( examRequest.getRequestDate() );
            oldExamRequest.setSector( examRequest.getSector() );
            oldExamRequest.setUser( examRequest.getUser() );

            oldExamRequest.setUpdatedAt( new Date() );
            // TODO: pegar o usuario logado
            oldExamRequest.setUpdatedBy( "system" );

            examRequest = examRequestRepository.save( oldExamRequest );
        }

        return examRequest;

    }
}
