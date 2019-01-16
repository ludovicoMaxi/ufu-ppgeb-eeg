package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Exam;


public interface ExamService {

    Exam save( Exam exam );


    Exam findById( Long id );


    List< Exam > findByFilter( Long medicalRecord, Long medicalRequest, String doctorRequestant );


    List< Exam > findAll();


    void delete( Long id );


    Exam update( Exam exam );

    Exam updateExamMedicament( Exam exam );

}
