package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.repository.PatientRepository;
import br.com.ufu.ppgeb.eeg.service.PatientService;


@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Patient save( Patient patient ) {

        Assert.notNull( patient, "patient cannot be null." );

        validatePatient( patient );

        patient.setCreatedAt( new Date() );
        // TODO: pegar o usuario logado
        patient.setCreatedBy( "system" );

        patient.setUpdatedAt( null );
        patient.setUpdatedBy( null );

        List< Patient > listPatientSameDocumentNumber = patientRepository.findByDocumentNumber( patient.getDocumentNumber() );

        if ( listPatientSameDocumentNumber != null && listPatientSameDocumentNumber.size() > 0 ) {
            throw new IllegalArgumentException( "CPF já foi cadastrado, por favor informe outro." );
        }

        Patient patientSaved = patientRepository.save( patient );

        return patientSaved;
    }


    private void validatePatient( Patient patient ) {

        Assert.notNull( patient, "Patient cannot be null." );
        Assert.hasText( patient.getName(), "name cannot be empty." );
        Assert.hasText( patient.getDocumentNumber(), "documentNumber cannot be empty." );
        Assert.notNull( patient.getBirthDate(), "birthDate cannot be empty." );
        Assert.notNull( patient.getNacionality(), "nacionality cannot be null." );

        if ( patient.getSex() != 'F' && patient.getSex() != 'M' ) {
            throw new IllegalArgumentException( "sex Invalid" );
        }
    }


    @Override
    public List< Patient > findAll() {

        return patientRepository.findAll();
    }


    @Override
    public Patient findById( Long id ) {

        return patientRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< Patient > findByFilter( String name, String documentNumber ) {

        List< Patient > list = null;
        if ( StringUtils.isBlank( name ) && StringUtils.isBlank( documentNumber ) ) {
            throw new IllegalArgumentException( "Informe pelo menos um campo para consultar!" );
        } else {

            list = patientRepository.findByFilter( name, documentNumber );
        }
        return list;
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        patientRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Patient update( Patient patient ) {

        Assert.notNull( patient, "patient cannot be null." );

        validatePatient( patient );
        Assert.notNull( patient.getId(), "patient ID cannot be null." );

        Patient oldPatient = patientRepository.getOne( patient.getId() );

        if ( oldPatient == null ) {
            throw new IllegalArgumentException( "Not exist patient with this Id=" + patient.getId() );
        }

        if ( !oldPatient.equals( patient ) ) {
            oldPatient.setName( patient.getName() );

            if ( !oldPatient.getDocumentNumber().equals( patient.getDocumentNumber() ) ) {
                throw new IllegalArgumentException( "CPF/CNPJ está divergente." );
            }

            oldPatient.setSex( patient.getSex() );
            oldPatient.setBirthDate( patient.getBirthDate() );
            oldPatient.setNacionality( patient.getNacionality() );
            oldPatient.setCivilStatus( patient.getCivilStatus() );
            oldPatient.setJob( patient.getJob() );

            oldPatient.setUpdatedAt( new Date() );
            // TODO: pegar o usuario logado
            oldPatient.setUpdatedBy( "system" );

            patient = patientRepository.save( oldPatient );
        }

        return patient;

    }
}
