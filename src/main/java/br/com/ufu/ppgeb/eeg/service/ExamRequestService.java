package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;


public interface ExamRequestService {

    ExamRequest save( ExamRequest examRequest );


    ExamRequest findById( Long id );


    List< ExamRequest > findByFilter( Long medicalRecord, Long medicalRequest, Long patientId, String doctorRequestant );


    List< ExamRequest > findAll();


    void delete( Long id );


    ExamRequest update( ExamRequest examRequest );

}
