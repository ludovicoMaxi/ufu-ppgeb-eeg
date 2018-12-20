package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Patient;


/**
 * Created by joaol on 05/12/18.
 */
public interface PatientRepository extends JpaRepository< Patient, Long >, PatientRepositoryCustom {

    List< Patient > findByDocumentNumber( String documentNumber );
}
