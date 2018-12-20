package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Patient;


/**
 * Created by joaol on 10/12/18.
 */
public interface PatientRepositoryCustom {

    List< Patient > findByFilter( String name, String documentNumber );

}
