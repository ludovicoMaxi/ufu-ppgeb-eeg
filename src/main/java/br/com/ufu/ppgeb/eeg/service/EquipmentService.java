package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Equipment;


public interface EquipmentService {

    List< Equipment > findAll();


    List< Equipment > findByName( String name );


    Equipment save( Equipment equipment );

}
