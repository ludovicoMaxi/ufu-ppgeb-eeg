package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;


/**
 * Created by joaol on 24/12/18.
 */
public interface ExamRequestRepositoryCustom {

    List< ExamRequest > findByFilter( Long patientId );

}
