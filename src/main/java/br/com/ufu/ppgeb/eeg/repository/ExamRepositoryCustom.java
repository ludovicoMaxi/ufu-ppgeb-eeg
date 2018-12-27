package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Exam;


/**
 * Created by joaol on 24/12/18.
 */
public interface ExamRepositoryCustom {

    List< Exam > findByFilter( Long medicalRecord, Long medicalRequest, String doctorRequestant );

}
