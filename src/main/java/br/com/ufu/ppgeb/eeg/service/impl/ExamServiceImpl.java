package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.repository.ExamRepository;
import br.com.ufu.ppgeb.eeg.service.ExamService;


@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Exam save( Exam exam ) {

        Assert.notNull( exam, "exam cannot be null." );

        validateExam( exam );

        exam.setCreatedAt( new Date() );
        // TODO: pegar o usuario logado
        exam.setCreatedBy( "system" );

        exam.setUpdatedAt( null );
        exam.setUpdatedBy( null );

        Exam examSaved = examRepository.save( exam );

        return examSaved;
    }


    private void validateExam( Exam exam ) {

        Assert.notNull( exam, "Exam cannot be null." );
//        Assert.notNull( exam.getMedicalRecord(), "medicalRecord cannot be empty." );
//        Assert.notNull( exam.getMedicalRequest(), "medicalRequest cannot be empty." );
//        Assert.hasText( exam.getSector(), "sector cannot be empty." );
//        Assert.hasText( exam.getDoctorRequestant(), "doctorRequestant cannot be empty." );
//        Assert.hasText( exam.getUser(), "user cannot be empty." );
//        Assert.notNull( exam.getRequestDate(), "requestDate cannot be empty." );
    }


    @Override
    public List< Exam > findAll() {

        return examRepository.findAll();
    }


    @Override
    public Exam findById( Long id ) {

        return examRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< Exam > findByFilter( Long medicalRecord, Long medicalRequest, String doctorRequestant ) {

        if ( StringUtils.isBlank( doctorRequestant ) && medicalRequest == null && medicalRecord == null ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        }

        List< Exam > list = null;

        list = examRepository.findByFilter( medicalRecord, medicalRequest, doctorRequestant );
        return list;
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        examRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Exam update( Exam exam ) {

        Assert.notNull( exam, "exam cannot be null." );

        validateExam( exam );
        Assert.notNull( exam.getId(), "exam ID cannot be null." );

        Exam oldExam = examRepository.getOne( exam.getId() );

        if ( oldExam == null ) {
            throw new IllegalArgumentException( "Not exist exam with this Id=" + exam.getId() );
        }

        if ( !oldExam.equals( exam ) ) {
//            oldExam.setMedicalRecord( exam.getMedicalRecord() );
//            oldExam.setMedicalRequest( exam.getMedicalRequest() );

            if ( !exam.getPatient().getId().equals( oldExam.getPatient().getId() ) ) {
                throw new IllegalArgumentException(
                    "Patient ID is different. Neew=" + exam.getPatient().getId() + ", Old=" + oldExam.getPatient().getId() );
            }

            oldExam.setAchievementDate( exam.getAchievementDate() );
//            oldExam.setAgreement( exam.getAgreement() );
//            oldExam.setCityOrigin( exam.getCityOrigin() );
//            oldExam.setClinicOrigin( exam.getClinicOrigin() );
//            oldExam.setDoctorRequestant( exam.getDoctorRequestant() );
//            oldExam.setRequestDate( exam.getRequestDate() );
//            oldExam.setSector( exam.getSector() );
//            oldExam.setUser( exam.getUser() );

            oldExam.setUpdatedAt( new Date() );
            // TODO: pegar o usuario logado
            oldExam.setUpdatedBy( "system" );

            exam = examRepository.save( oldExam );
        }

        return exam;

    }
}
