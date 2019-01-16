package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Medicament;


public interface MedicamentService {

    List< Medicament > findAll();


    List< Medicament > findByName( String name );


    Medicament save( Medicament medicament );

}
