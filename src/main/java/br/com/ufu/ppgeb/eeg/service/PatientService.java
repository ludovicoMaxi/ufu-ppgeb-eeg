package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Patient;


public interface PatientService {

    Patient save( Patient patient );


    Patient findById( Long id );


    List< Patient > findByFilter( String name, String documentNumber );


    List< Patient > findAll();


    void delete( Long id );


    Patient update( Patient patient );

}
