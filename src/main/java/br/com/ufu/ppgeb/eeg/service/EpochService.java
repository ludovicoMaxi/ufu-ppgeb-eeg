package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.view.EpochList;


public interface EpochService {

    Epoch save( Epoch epoch );


    Epoch findById( Long id );


    List< Epoch > findByFilter( Long examId );


    List< Epoch > findAll();


    void delete( Long id );


    List< Epoch > updateList( EpochList epochList );

}
