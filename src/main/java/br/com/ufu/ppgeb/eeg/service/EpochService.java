package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Epoch;


public interface EpochService {

    Epoch save( Epoch epoch );


    Epoch findById( Long id );


    List< Epoch > findByFilter( Long examId );


    List< Epoch > findAll();


    void delete( Long id );


    List< Epoch > updateList( List< Epoch > epoch );

}
