package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.repository.MedicamentRepository;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;


@Service
public class MedicamentServiceImpl implements MedicamentService {

    @Autowired
    private MedicamentRepository medicamentRepository;


    @Override
    public List< Medicament > findAll() {

        return medicamentRepository.findAll();
    }


    @Override
    public List< Medicament > findByName( String name ) {

        return medicamentRepository.findByName( name );
    }


    @Override
    public Medicament save( Medicament medicament ) {

        medicament.setName( medicament.getName().toUpperCase() );
        List< Medicament > medicamentList = findByName( medicament.getName() );

        if ( medicamentList != null && medicamentList.size() > 0 ) {
            throw new IllegalArgumentException( "Medicamento já cadastrado: " + medicament.getName() );
        } else {
            Medicament medicamentSave = new Medicament();
            medicamentSave.setCreatedAt( new Date() );
            medicamentSave.setCreatedBy( "SYSTEM" );
            medicamentSave.setName( medicament.getName() );
            medicamentSave.setDescription( medicament.getDescription() );
            medicament = medicamentRepository.save( medicamentSave );
        }
        return medicament;
    }

}
